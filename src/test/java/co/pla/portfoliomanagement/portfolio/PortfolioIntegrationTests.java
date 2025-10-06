package co.pla.portfoliomanagement.portfolio;


import co.pla.portfoliomanagement.fixture.TestConfig;
import co.pla.portfoliomanagement.fixture.TestSecurityConfig;
import co.pla.portfoliomanagement.gateway.dto.PortfolioRequest;
import co.pla.portfoliomanagement.identity.application.dto.CreateUserDto;
import co.pla.portfoliomanagement.identity.application.facade.UserFacade;
import co.pla.portfoliomanagement.portfolio.application.dto.StockPositionDto;
import co.pla.portfoliomanagement.portfolio.application.facade.PortfolioFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import({TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class PortfolioIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PortfolioFacade portfolioFacade;

    private static UUID testUserUid;
    private static UUID testAdminUid;
    private static UUID testPortfolioUid;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void setUpOnce(@Autowired UserFacade userFacade,
                          @Autowired PortfolioFacade portfolioFacade) {
        var user = userFacade.createUser(new CreateUserDto("user", "123", "a@b.com",
                new HashSet<>(Collections.singleton("AUTHORITY_USER"))));
        testUserUid = user.id();

        var admin = userFacade.createUser(new CreateUserDto("admin", "123", "aa@b.com",
                new HashSet<>(Collections.singleton("AUTHORITY_ADMIN"))));
        testAdminUid = admin.id();

        var portfolio = portfolioFacade.create("Test Portfolio", testAdminUid, 10000.0);
        testPortfolioUid = portfolio.id();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldCreatePortfolioSuccessfully() throws Exception {
        // Arrange
        PortfolioRequest request = new PortfolioRequest("new Test Portfolio", testAdminUid, 12300.0);

        // Act & Assert
        mockMvc.perform(post("/portfolios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("new Test Portfolio"))
                .andExpect(jsonPath("$.data.userId").value(testAdminUid.toString()))
                .andExpect(jsonPath("$.data.balance").value(12300.0));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void shouldForbidPortfolioCreationForNonAdmin() throws Exception {
        // Arrange
        PortfolioRequest request = new PortfolioRequest("Test Portfolio", testUserUid, 10000.0);

        // Act & Assert
        mockMvc.perform(post("/portfolios")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void shouldGetUserPortfoliosSuccessfully() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/portfolios/{userUid}", testAdminUid))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldAddPositionSuccessfully() throws Exception {
        // Arrange
        StockPositionDto stockPositionDto = new StockPositionDto("AAPL", 100, 25.0);

        // Act & Assert
        mockMvc.perform(post("/portfolios/{portfolioUid}/positions", testPortfolioUid)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockPositionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stockPositions[0].ticker").value("AAPL"))
                .andExpect(jsonPath("$.data.stockPositions[0].quantity").value(100))
                .andExpect(jsonPath("$.data.stockPositions[0].weight").value(25.0));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldUpdatePositionSuccessfully() throws Exception {
        // Arrange
        StockPositionDto newStockPositionDto = new StockPositionDto("AAPL", 50, 5.0);

        // Act & Assert
        mockMvc.perform(put("/portfolios/{portfolioUid}/positions", testPortfolioUid)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStockPositionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stockPositions[0].quantity").value(150))
                .andExpect(jsonPath("$.data.stockPositions[0].weight").value(30.0));
    }
}