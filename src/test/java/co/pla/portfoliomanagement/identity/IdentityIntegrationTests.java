package co.pla.portfoliomanagement.identity;

import co.pla.portfoliomanagement.config.TestConfig;
import co.pla.portfoliomanagement.config.TestSecurityConfig;
import co.pla.portfoliomanagement.fixture.TestData;
import co.pla.portfoliomanagement.fixture.TestDataFixture;
import co.pla.portfoliomanagement.identity.application.dto.*;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import({TestDataFixture.class, TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("test")
public class IdentityIntegrationTests {


    private static UUID testUserUid;
    private static UserDto testUserDto;
    private static Long testUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserFacade userFacade;

    @BeforeAll
    static void setUpOnce(@Autowired TestData testData) {
        testUserUid = testData.user().getUid();
        testUserId = testData.user().getId();
        testUserDto = new UserDto(testUserUid, "user", "user@mail.co");
    }


    @Test
    void shouldGetUserSuccessfully() throws Exception {
        // Arrange
        when(userFacade.getUserByUid(testUserUid)).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(get("/users/{id}", testUserUid).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testUserUid.toString()))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(userFacade, times(1)).getUserByUid(testUserUid);
    }

    @Test
    void shouldGetUsersWithPagination() throws Exception {
        // Arrange
        UsersDto usersDto = new UsersDto(List.of(testUserDto), 1L);
        when(userFacade.getUsers(anyInt(), anyInt())).thenReturn(usersDto);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .param("page", "1")
                        .param("perPage", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users[0].id").value(testUserUid.toString()))
                .andExpect(jsonPath("$.data.total").value(1));

        verify(userFacade, times(1)).getUsers(1, 10);
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        // Arrange
        EditUserDto editUserDto = new EditUserDto(testUserUid, "updateduser", Set.of("ADMIN"));
        when(userFacade.updateUser(any(EditUserDto.class))).thenReturn(testUserDto);

        // Act & Assert
        mockMvc.perform(put("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(testUserUid.toString()));

        verify(userFacade, times(1)).updateUser(any(EditUserDto.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldChangePasswordByAdminSuccessfully() throws Exception {
        // Arrange
        ChangePasswordByAdminDto request = new ChangePasswordByAdminDto(testUserId, "newPassword123");
        when(userFacade.changePasswordByAdmin(any(ChangePasswordByAdminDto.class))).thenReturn("Password changed Successfully");

        // Act & Assert
        mockMvc.perform(put("/users/admin/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Password changed Successfully"));

        verify(userFacade, times(1)).changePasswordByAdmin(any(ChangePasswordByAdminDto.class));
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

        verify(userFacade, never()).changePasswordByAdmin(any(ChangePasswordByAdminDto.class));
    }

    @Test
    void shouldChangePasswordSuccessfully() throws Exception {
        // Arrange
        ChangePasswordDto request = new ChangePasswordDto(testUserUid, "currentPass", "newPass123");
        when(userFacade.changePassword(any(ChangePasswordDto.class))).thenReturn("Password changed Successfully");

        // Act & Assert
        mockMvc.perform(put("/users/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Password changed Successfully"));

        verify(userFacade, times(1)).changePassword(any(ChangePasswordDto.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        // Arrange
        doNothing().when(userFacade).deleteUserByUid(testUserUid);

        // Act & Assert
        mockMvc.perform(delete("/users/{id}", testUserId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));

        verify(userFacade, times(1)).deleteUserByUid(testUserUid);
    }
}
