package at.qe.skeleton.controllers;

import at.qe.skeleton.model.RatingScale;
import at.qe.skeleton.services.RatingService;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.mappers.RatingMapper;
import at.qe.skeleton.mappers.RatingCreateMapper;
import at.qe.skeleton.dtos.RatingDTO;
import at.qe.skeleton.dtos.RatingCreateDTO;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Product;


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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RatingService ratingService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private RatingMapper ratingMapper;

    @MockitoBean
    private RatingCreateMapper ratingCreateMapper;

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void createRatingSuccess() throws Exception {
        Long productId = 1L;
        Long authorId = 1L;

        RatingCreateDTO createDTO = new RatingCreateDTO(
                RatingScale.FIVE_STARS,
                "Einfach nur episch!",
                authorId,
                "pro_gamer",
                productId
        );

        Product product = new Product();
        product.setId(productId);

        Rating rating = new Rating();
        rating.setId(500L);

        RatingDTO responseDTO = new RatingDTO(
                500L,
                LocalDateTime.now(),
                RatingScale.FIVE_STARS,
                "Einfach nur episch!",
                authorId,
                "pro_gamer",
                productId
        );

        Mockito.when(productService.getProductById(productId)).thenReturn(Optional.of(product));
        Mockito.when(ratingCreateMapper.mapFrom(Mockito.any(RatingCreateDTO.class))).thenReturn(rating);
        Mockito.when(ratingService.saveRating(Mockito.any(Rating.class))).thenReturn(rating);
        Mockito.when(ratingMapper.mapTo(Mockito.any(Rating.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/products/{productId}/ratings", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(500L))
                .andExpect(jsonPath("$.comment").value("Einfach nur episch!"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void deleteRatingNotFound() throws Exception {
        Long productId = 1L;
        Long ratingId = 404L;

        Mockito.when(ratingService.loadRating(productId, ratingId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/products/{productId}/ratings/{ratingId}", productId, ratingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllRatingsSuccess() throws Exception {
        Long productId = 1L;
        Rating rating = new Rating();

        RatingDTO dto = new RatingDTO(
                1L,
                LocalDateTime.now(),
                RatingScale.FOUR_STARS,
                "Ganz okay",
                1L,
                "user1",
                productId
        );

        Mockito.when(ratingService.getAllRatingsByProduct(productId)).thenReturn(List.of(rating));
        Mockito.when(ratingMapper.mapTo(rating)).thenReturn(dto);

        mockMvc.perform(get("/api/products/{productId}/ratings", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comment").value("Ganz okay"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void updateRatingSuccess() throws Exception {
        Long productId = 1L;
        Long ratingId = 500L;
        Long authorId = 1L;

        RatingDTO updateDTO = new RatingDTO(
                ratingId,
                LocalDateTime.now(),
                RatingScale.FIVE_STARS,
                "Actually, it's even better now!",
                authorId,
                "pro_gamer",
                productId
        );

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);

        Rating updatedRating = new Rating();
        updatedRating.setId(ratingId);

        Mockito.when(ratingService.loadRating(productId, ratingId)).thenReturn(Optional.of(existingRating));
        Mockito.when(ratingMapper.mapFrom(Mockito.any(RatingDTO.class))).thenReturn(updatedRating);
        Mockito.when(ratingService.saveRating(Mockito.any(Rating.class))).thenReturn(updatedRating);
        Mockito.when(ratingMapper.mapTo(Mockito.any(Rating.class))).thenReturn(updateDTO);

        mockMvc.perform(patch("/api/products/{productId}/ratings/{ratingId}", productId, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ratingId))
                .andExpect(jsonPath("$.comment").value("Actually, it's even better now!"))
                .andExpect(jsonPath("$.ratingScale").value("FIVE_STARS"));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void updateRatingNotFound() throws Exception {
        Long productId = 1L;
        Long ratingId = 999L;

        RatingDTO updateDTO = new RatingDTO(ratingId, LocalDateTime.now(), RatingScale.ONE_STAR, "Bad", 1L, "u", 1L);

        Mockito.when(ratingService.loadRating(productId, ratingId)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/products/{productId}/ratings/{ratingId}", productId, ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
}