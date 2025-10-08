package co.pla.portfoliomanagement.identity;

import co.pla.portfoliomanagement.config.TestConfig;
import co.pla.portfoliomanagement.config.TestSecurityConfig;
import co.pla.portfoliomanagement.fixture.BaseIntegrationTest;
import co.pla.portfoliomanagement.fixture.TestData;
import co.pla.portfoliomanagement.fixture.TestDataFixture;
import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({TestDataFixture.class, TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("test")
public class IdentityIntegrationTests extends BaseIntegrationTest {

    private static UUID testUserUid;
    private static UUID testAdminId;
    private static UserDto testUserDto;
    private static Long testUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserFacade userFacade;

    @Autowired
    private TestData testData;

    @PostConstruct
    void initializeTestData() {
        testUserUid = testData.user().getUid();
        testUserId = testData.user().getId();
        testAdminId = testData.admin().getUid();
        testUserDto = new UserDto(testUserUid, "user", "user@mail.co");
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetUserSuccessfully() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/users/{id}", testUserUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testUserUid.toString()))
                .andExpect(jsonPath("$.data.username").value("user"))
                .andExpect(jsonPath("$.data.email").value("user@mail.co"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldGetUsersWithPagination() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/users")
                        .param("page", "1")
                        .param("perPage", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users.length()").value(2))
                .andExpect(jsonPath("$.data.total").value(2))
                .andExpect(jsonPath("$.data.users[*].id",
                        containsInAnyOrder(testData.admin().getUid().toString(), testData.user().getUid().toString())))
                .andExpect(jsonPath("$.data.users[*].username",
                        containsInAnyOrder("admin", "user")));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldUpdateUserSuccessfully() throws Exception {
        // Arrange
        EditUserDto editUserDto = new EditUserDto(testUserUid, "updateduser", Set.of("AUTHORITY_ADMIN"));

        // Act & Assert
        mockMvc.perform(put("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testUserUid.toString()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldChangePasswordByAdminSuccessfully() throws Exception {
        // Arrange
        ChangePasswordByAdminDto request = new ChangePasswordByAdminDto(testUserId, "newPassword123");

        // Act & Assert
        mockMvc.perform(put("/users/admin/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Password changed Successfully"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void shouldForbidChangePasswordByAdminForNonAdmin() throws Exception {
        // Arrange
        ChangePasswordByAdminDto request = new ChangePasswordByAdminDto(testUserId, "newPassword123");

        // Act & Assert
        mockMvc.perform(put("/users/admin/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "admin", authorities = "USER")
    void shouldChangePasswordSuccessfully() throws Exception {
        // Arrange
        ChangePasswordDto request = new ChangePasswordDto(testAdminId, "pass@admin", "new@pass@admin");

        // Act & Assert
        mockMvc.perform(put("/users/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Password changed Successfully"));

    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    void shouldDeleteUserSuccessfully() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(delete("/users/{id}", testAdminId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));

    }
}
