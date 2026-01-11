package at.qe.skeleton.controllers;

import at.qe.skeleton.configs.JwtConfig;
import at.qe.skeleton.configs.JwtTokenProvider;
import at.qe.skeleton.configs.WebSecurityConfig;
import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.mappers.ProductMapper; // <--- WICHTIG
import at.qe.skeleton.mappers.ProductCreateMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.UserxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(WebSecurityConfig.class)
public class ProductControllerTest {
    // ===== Setup =====
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private UserxService userxService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtConfig jwtConfig;

    @MockitoBean
    private ProductMapper productMapper;

    @MockitoBean
    private ProductCreateMapper productCreateMapper;

    @Autowired
    private ObjectMapper objectMapper;


    // ===== Tests =====

    // GET
    @Test
    public void testGetAllProducts() throws Exception {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("My supercool Product");

        Mockito.when(productService.getAllProducts()).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/products/"))
                .andExpect(status().isOk());
    }

    // POST (Only for ADMIN/MANAGER)
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testCreateProduct_AsAdmin() throws Exception {

        ProductCreateDTO dto = new ProductCreateDTO(
                "My supernew Product",
                "Desc",
                99.90,
                10L,
                0.0,
                "url",
                Collections.emptySet()
        );

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("My supernew Product");
        Mockito.when(productCreateMapper.mapFrom(dto)).thenReturn(savedProduct);
        Mockito.when(productService.saveProduct(Mockito.any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/api/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());
    }

    // DELETE (Only for ADMIN/MANAGER)
    @Test
    @WithMockUser(username = "manager", authorities = {"MANAGER"})
    public void testDeleteProduct_AsManager() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(new Product()));

        mockMvc.perform(delete("/api/products/1")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    // DELETE (Without Login -> Should be forbidden)
    @Test
    public void testDeleteProduct_Unauthorized() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }
}