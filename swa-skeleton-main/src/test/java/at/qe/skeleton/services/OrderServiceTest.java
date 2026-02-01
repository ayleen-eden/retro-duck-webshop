package at.qe.skeleton.services;

import at.qe.skeleton.dtos.*;
import at.qe.skeleton.exceptions.InsufficientStockException;
import at.qe.skeleton.exceptions.OrderNotFoundException;
import at.qe.skeleton.exceptions.UnauthorizedOrderAccessException;
import at.qe.skeleton.mappers.*;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OrderService}.
 * <p>
 * This test suite ensures the correctness of order processing logic, including
 * successful checkouts, stock management, and access control for retrieving
 * and deleting orders.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartValidationService cartValidationService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private OrderService orderService;

    private Userx testUser;
    private Product testProduct;
    private CartDTO testCart;
    private CheckoutRequestDTO testCheckoutRequest;

    @BeforeEach
    void setUp() {
        testUser = new Userx();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@uibk.ac.at");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop");
        testProduct.setPrice(1000.0);
        testProduct.setDiscount(0.0); // Kein Rabatt
        testProduct.setStock(5L);

        CartItemDTO item = new CartItemDTO(1L, "Laptop", "image.png", 1000.0, 2);
        testCart = new CartDTO(List.of(item));

        testCheckoutRequest = new CheckoutRequestDTO(
                testCart,
                "Duck McQuack",
                "Quakstreet 404",
                "Ducksbruck",
                "6020",
                "Duckland",
                "DUCK_COINS"
        );
    }

    /**
     * Verifies that an order is successfully placed when stock is sufficient.
     * Checks if the stock is correctly decremented and the order is saved.
     */
    @Test
    void testPlaceOrderSuccess() throws InsufficientStockException {
        when(cartValidationService.validateCart(any())).thenReturn(Optional.of(testCart));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        OrderDTO mappedDto = new OrderDTO(1L, null, OrderStatus.DONE, 2000.0, List.of(), "Duck McQuak", "DUCK_COINS");
        when(orderMapper.mapTo(any(Order.class))).thenReturn(mappedDto);

        OrderDTO result = orderService.placeOrder(testUser, testCheckoutRequest);

        assertThat(result).isNotNull();
        assertThat(testProduct.getStock()).isEqualTo(3);
        verify(productService).saveProduct(testProduct);
        verify(orderRepository).save(any(Order.class));
    }

    /**
     * Ensures that an {@link InsufficientStockException} is thrown when the
     * product stock is lower than the requested quantity.
     */
    @Test
    void testPlaceOrderInsufficientStock() {
        testProduct.setStock(1L); // Zu wenig
        when(cartValidationService.validateCart(any())).thenReturn(Optional.of(testCart));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(() -> orderService.placeOrder(testUser, testCheckoutRequest))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");

        verify(orderRepository, never()).save(any());
    }

    /**
     * Checks if the service throws an error when the cart validation fails.
     */
    @Test
    void testPlaceOrderCartValidationFails() {
        when(cartValidationService.validateCart(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.placeOrder(testUser, testCheckoutRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cart validation failed");

        verify(orderRepository, never()).save(any());
    }

    /**
     * Verifies that the total price of an order is correctly calculated when
     * a product discount is applied.
     *
     * @throws InsufficientStockException if stock validation fails.
     */
    @Test
    void testPlaceOrderCalculatesTotalPriceWithDiscount() throws InsufficientStockException {
        testProduct.setPrice(1000.0);
        testProduct.setDiscount(0.2);
        testProduct.setStock(10L);

        when(cartValidationService.validateCart(any())).thenReturn(Optional.of(testCart));
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(orderCaptor.capture())).thenAnswer(i -> i.getArguments()[0]);

        OrderDTO mappedDto = new OrderDTO(1L, null, OrderStatus.DONE, 1600.0, List.of(), "Duck McQuack", "DUCK_COINS");
        when(orderMapper.mapTo(any(Order.class))).thenReturn(mappedDto);

        orderService.placeOrder(testUser, testCheckoutRequest);

        Order savedOrder = orderCaptor.getValue();
        double expectedTotal = 1000.0 * 0.8 * 2;
        assertThat(savedOrder.getTotalPrice()).isEqualTo(expectedTotal);
    }

    /**
     * Verifies that access is denied when a user tries to retrieve an
     * order that does not belong to them.
     */
    @Test
    void testGetOrderByIdAccessDenied() {
        Userx stranger = new Userx();
        stranger.setId(2L);
        stranger.setUsername("stranger");

        Order order = new Order();
        order.setUser(testUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.getOrderById(1L, stranger))
                .isInstanceOf(UnauthorizedOrderAccessException.class)
                .hasMessageContaining("Access denied");
    }

    /**
     * Checks if an {@link OrderNotFoundException} is thrown when requesting a non-existent ID.
     */
    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(999L, testUser))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found");
    }

    /**
     * Verifies that a user can successfully delete their own order.
     * @throws OrderNotFoundException if order does not exist
     * @throws UnauthorizedOrderAccessException if the user is not authorized to access this order
     */
    @Test
    void testDeleteOrderSuccess() throws OrderNotFoundException, UnauthorizedOrderAccessException {
        Order order = new Order();
        order.setId(10L);
        order.setUser(testUser);

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(10L, testUser);

        verify(orderRepository, times(1)).delete(order);
    }

    /**
     * Verifies that deleting an order is forbidden if the user is not the owner.
     */
    @Test
    void testDeleteOrderAccessDenied() {
        Userx stranger = new Userx();
        stranger.setId(2L);
        stranger.setUsername("stranger");

        Order order = new Order();
        order.setId(10L);
        order.setUser(testUser);

        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.deleteOrder(10L, stranger))
                .isInstanceOf(UnauthorizedOrderAccessException.class)
                .hasMessageContaining("Access denied");

        verify(orderRepository, never()).delete(any());
    }

    /**
     * Ensures correct handling when attempting to delete a non-existent order.
     */
    @Test
    void testDeleteOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.deleteOrder(999L, testUser))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Order not found");

        verify(orderRepository, never()).delete(any());
    }

    /**
     * Verifies that the order history retrieval returns the correct collection size.
     */
    @Test
    void testGetOrderHistory() {
        Order order1 = new Order();
        order1.setUser(testUser);
        Order order2 = new Order();
        order2.setUser(testUser);

        List<Order> orders = List.of(order1, order2);
        when(orderRepository.findByUser(testUser)).thenReturn(orders);

        when(orderMapper.mapTo(any(Order.class))).thenReturn(new OrderDTO(null, null, null, 0.0, List.of(), null, null));

        Collection<OrderDTO> result = orderService.getOrderHistory(testUser);

        assertThat(result).hasSize(2);
        verify(orderRepository).findByUser(testUser);
    }
}