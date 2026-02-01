package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.NotificationDTO;
import at.qe.skeleton.model.Notification;
import at.qe.skeleton.model.NotificationType;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.NotificationService;
import at.qe.skeleton.mappers.NotificationMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private NotificationMapper notificationMapper;

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllNotificationsForUserSuccess() throws Exception {
        Userx user = new Userx();
        user.setId(1L);
        user.setUsername("user1");

        Product product = new Product();
        product.setId(1L);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(user);
        notification.setProduct(product);
        notification.setTitle("Test Notification");
        notification.setDescription("You got mail");
        notification.setType(NotificationType.RESTOCK);
        notification.setTimestamp(LocalDateTime.now());

        NotificationDTO notificationDTO = new NotificationDTO(
                1L,
                notification.getProduct().getId(),
                notification.getUser().getId(),
                notification.getDescription(),
                notification.getTitle(),
                notification.getTimestamp(),
                notification.getType()
        );

        Mockito.when(notificationService.getNotificationsByUserId(1L))
                .thenReturn(List.of(notification));
        Mockito.when(notificationMapper.mapTo(notification)).thenReturn(notificationDTO);

        mockMvc.perform(get("/api/notifications/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Notification"))
                .andExpect(jsonPath("$[0].description").value("You got mail"))
                .andExpect(jsonPath("$[0].type").value("RESTOCK"))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].productId").value(1L));
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    void getAllNotificationsForUserEmpty() throws Exception {
        Mockito.when(notificationService.getNotificationsByUserId(2L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notifications/{userId}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
