package co.pla.portfoliomanagement.portfolio;


import co.pla.portfoliomanagement.config.TestConfig;
import co.pla.portfoliomanagement.config.TestSecurityConfig;
import co.pla.portfoliomanagement.fixture.BaseIntegrationTest;
import co.pla.portfoliomanagement.fixture.TestData;
import co.pla.portfoliomanagement.fixture.TestDataFixture;
import co.pla.portfoliomanagement.gateway.dto.PortfolioRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import({TestDataFixture.class, TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("test")
class PortfolioIntegrationTests extends BaseIntegrationTest {

    private static UUID testUserUid;
    private static UUID testAdminUid;
    private static UUID testPortfolioUid;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PortfolioFacade portfolioFacade;

    @BeforeAll
    static void setUpOnce(@Autowired TestData testData) {
        testUserUid = testData.user().getUid();
        testAdminUid = testData.admin().getUid();
        testPortfolioUid = testData.portfolio().getUid();
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