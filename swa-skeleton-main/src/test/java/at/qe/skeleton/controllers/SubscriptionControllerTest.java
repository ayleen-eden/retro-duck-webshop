package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.SubscriptionDTO;
import at.qe.skeleton.mappers.SubscriptionMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Subscription;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.AuthenticatedUserService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private SubscriptionMapper subscriptionMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private AuthenticatedUserService authenticatedUserService;

    @BeforeEach
    void setUp() {
        Userx realUser = new Userx();
        realUser.setId(1L);
        realUser.setUsername("user1");

        Mockito.when(authenticatedUserService.getAuthenticatedUser()).thenReturn(realUser);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void subscribeSuccess() throws Exception {
        Userx user = new Userx();
        user.setId(1L);
        user.setUsername("user1");

        Product product = new Product();
        product.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setProduct(product);

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        Mockito.when(subscriptionService.createSubscription(user, product)).thenReturn(subscription);

        mockMvc.perform(post("/api/subscriptions")
                        .param("productId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.productId").value(1L));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void subscribeFailureProductNotFound() throws Exception {
        Mockito.when(productService.getProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/subscriptions")
                        .param("productId", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void unsubscribeSuccess() throws Exception {
        Userx user = new Userx();
        user.setId(1L);
        user.setUsername("user1");

        Product product = new Product();
        product.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setProduct(product);

        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product));
        Mockito.when(subscriptionService.getSubscriptionByUserIdAndProductId(1L, 1L)).thenReturn(subscription);

        mockMvc.perform(delete("/api/subscriptions")
                        .param("productId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(subscriptionService).deleteSubscription(subscription);
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void unsubscribeFailureSubscriptionNotFound() throws Exception {
        Product product = new Product();
        Mockito.when(productService.getProductById(99L)).thenReturn(Optional.of(product));
        Mockito.when(subscriptionService.getSubscriptionByUserIdAndProductId(1L, 99L)).thenReturn(null);

        mockMvc.perform(delete("/api/subscriptions")
                        .param("productId", "99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllSubscriptionsForUserSuccess() throws Exception {
        Userx user = new Userx();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setProduct(product);

        SubscriptionDTO subscriptionDTO = new SubscriptionDTO(1L, 1L, 1L);

        Mockito.when(subscriptionService.getSubscriptionByUserId(1L)).thenReturn(List.of(subscription));
        Mockito.when(subscriptionMapper.mapTo(subscription)).thenReturn(subscriptionDTO);

        mockMvc.perform(get("/api/subscriptions/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllSubscriptionsForUserEmpty() throws Exception {
        Mockito.when(subscriptionService.getSubscriptionByUserId(2L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/subscriptions/{userId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
