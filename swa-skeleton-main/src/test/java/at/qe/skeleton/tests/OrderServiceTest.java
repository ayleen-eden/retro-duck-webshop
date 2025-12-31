package at.qe.skeleton.tests;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.mappers.OrderMapper;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.OrderRepository;
import at.qe.skeleton.repositories.ProductRepository;
import at.qe.skeleton.services.CartValidationService;
import at.qe.skeleton.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartValidationService cartValidationService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Userx testUser;
    private Product testProduct;
    private CartDTO testCart;

    @BeforeEach
    void setUp() {
        testUser = new Userx();
        testUser.setUsername("testuser");
        testUser.setEmail("test@uibk.ac.at");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop");
        testProduct.setPrice(1000.0);
        testProduct.setDiscount(1.0); // Kein Rabatt
        testProduct.setStock(5);

        CartItemDTO item = new CartItemDTO(1L, "Laptop", "image.png", 1000.0, 2);
        testCart = new CartDTO(List.of(item));
    }

    @Test
    void testPlaceOrderSuccess() {
        when(cartValidationService.validateCart(any())).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDTO(1L, null, OrderStatus.DONE, 2000.0, List.of()));

        OrderDTO result = orderService.placeOrder(testUser, testCart);

        assertThat(result).isNotNull();
        assertThat(testProduct.getStock()).isEqualTo(3);
        verify(productRepository).save(testProduct);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testPlaceOrderInsufficientStock() {
        testProduct.setStock(1); // Zu wenig
        when(cartValidationService.validateCart(any())).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThatThrownBy(() -> orderService.placeOrder(testUser, testCart))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void testGetOrderByIdAccessDenied() {
        Userx stranger = new Userx();
        stranger.setUsername("stranger");

        Order order = new Order();
        order.setUser(testUser);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.getOrderById(1L, stranger))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Access denied");
    }
}
