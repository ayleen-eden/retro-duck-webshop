package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.services.CartValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CartValidationService cartValidationService;

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void validateCartSuccess() throws Exception {
        CartItemDTO item = new CartItemDTO(1L, "Product 1", "img.jpg", 100.0, 2);
        CartDTO cart = new CartDTO(List.of(item));

        CartItemDTO updatedItem = new CartItemDTO(1L, "Product 1", "img.jpg", 80.0, 2);
        CartDTO updatedCart = new CartDTO(List.of(updatedItem));

        Mockito.when(cartValidationService.validateCart(Mockito.any(CartDTO.class)))
                .thenReturn(Optional.of(updatedCart));

        mockMvc.perform(post("/api/cart/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].pricePerUnit").value(80.0))
                .andExpect(jsonPath("$.items[0].amount").value(2))
                .andExpect(jsonPath("$.items[0].productName").value("Product 1"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void validateCartFailure() throws Exception {
        CartItemDTO item = new CartItemDTO(1L, "Product 1", "img.jpg", 100.0, 20);
        CartDTO cart = new CartDTO(List.of(item));

        Mockito.when(cartValidationService.validateCart(Mockito.any(CartDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/cart/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isBadRequest());
    }
}
