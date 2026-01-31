package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CheckoutRequestDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.dtos.OrderItemDTO;
import at.qe.skeleton.model.OrderStatus;
import at.qe.skeleton.model.Userx;

import at.qe.skeleton.services.OrderService;
import at.qe.skeleton.services.AuthenticatedUserService;
import at.qe.skeleton.exceptions.OrderNotFoundException;
import at.qe.skeleton.exceptions.UnauthorizedOrderAccessException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private AuthenticatedUserService authenticatedUserService;

    private Userx testUser;

    @BeforeEach
    void setUp() {
        testUser = new Userx();
        testUser.setId(1L);
        testUser.setUsername("shopping_king");

        Mockito.when(authenticatedUserService.getAuthenticatedUser()).thenReturn(testUser);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void createOrder() throws Exception {
        CartDTO mockCart = new CartDTO(List.of());

        CheckoutRequestDTO request = new CheckoutRequestDTO(
                mockCart,
                "Link of Hyrule",
                "Zelda Lane 123",
                "Kakariko",
                "6020",
                "Austria",
                "MasterCard"
        );

        OrderDTO responseOrder = new OrderDTO(
                101L,
                LocalDateTime.now(),
                OrderStatus.NEW,
                42.0,
                List.of(),
                "Link of Hyrule",
                "MasterCard"
        );

        Mockito.when(orderService.placeOrder(eq(testUser), any(CheckoutRequestDTO.class)))
                .thenReturn(responseOrder);

        mockMvc.perform(post("/api/orders/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.shippingName").value("Link of Hyrule"))
                .andExpect(jsonPath("$.paymentMethod").value("MasterCard"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getOrderById() throws Exception {
        Long orderId = 101L;

        OrderItemDTO item = new OrderItemDTO(5L, "Power Glove", 1, 80.0, 0.0);

        OrderDTO expectedOrder = new OrderDTO(
                orderId,
                LocalDateTime.now(),
                OrderStatus.NEW,
                80.0,
                List.of(item),
                "Link",
                "Rupees"
        );

        Mockito.when(orderService.getOrderById(orderId, testUser)).thenReturn(expectedOrder);

        mockMvc.perform(get("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.totalPrice").value(80.0))
                .andExpect(jsonPath("$.shippingName").value("Link"))
                .andExpect(jsonPath("$.items[0].productName").value("Power Glove"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getOrderByIdNotFound() throws Exception {
        Long orderId = 999L;

        Mockito.when(orderService.getOrderById(orderId, testUser))
                .thenThrow(new OrderNotFoundException("Order not found!"));

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found!"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void deleteOrderForbidden() throws Exception {
        Long orderId = 50L;

        Mockito.doThrow(new UnauthorizedOrderAccessException("That's not yours!"))
                .when(orderService).deleteOrder(orderId, testUser);

        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isForbidden())
                .andExpect(content().string("That's not yours!"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllOrders() throws Exception {
        OrderDTO order1 = new OrderDTO(1L, LocalDateTime.now(), OrderStatus.NEW, 20.0, List.of(), "User", "Card");

        Mockito.when(orderService.getOrderHistory(testUser)).thenReturn(List.of(order1));

        mockMvc.perform(get("/api/orders/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void deleteOrder() throws Exception {
        Long orderId = 101L;

        mockMvc.perform(delete("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService).deleteOrder(orderId, testUser);
    }
}