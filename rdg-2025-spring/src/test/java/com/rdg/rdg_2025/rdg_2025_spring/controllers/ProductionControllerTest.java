package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import com.rdg.rdg_2025.rdg_2025_spring.models.Production;
import com.rdg.rdg_2025.rdg_2025_spring.models.Venue;
import com.rdg.rdg_2025.rdg_2025_spring.payload.request.production.NewProductionRequest;
import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductionController.class)
public class ProductionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ProductionService productionService;

    @InjectMocks
    private ProductionController productionController;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private Production testProduction = new Production(
            "Test Production",
            new Venue(),
            "Test Author",
            "Test Description",
            LocalDateTime.parse("2025-10-10T10:00:00", formatter),
            false,
            false,
            "Test File String"
    );



    @Nested
    @DisplayName("addNewProduction controller tests")
    class addNewProductionControllerTests {

        @Test
        @WithMockUser(roles="ADMIN")
        void test201StatusWhenServiceSuccessfullySavesProduction() throws Exception {
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(testProduction);

            // Act & Assert
            mockMvc.perform(post("/productions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                    "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                    ))
                    .andExpect(status().isCreated()
                    );

        }

        @Test
        void testExpectedUriWhenSuccessfullySavesProduction() throws Exception {
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(testProduction);
            int testProductionId = testProduction.getId();

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(header().string("Location", "/productions" + "/" + testProductionId)

                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testExpectedReturnedProductionWhenServiceSuccessfullySavesProduction() throws Exception{
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(testProduction);

            String formattedAuditionDate = testProduction.getAuditionDate().format(formatter);

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(jsonPath("$.production.id").isNotEmpty())
                    .andExpect(jsonPath("$.production.name").value(testProduction.getName()))
                    .andExpect(jsonPath("$.production.venue").isNotEmpty())
                    .andExpect(jsonPath("$.production.author").value(testProduction.getAuthor()))
                    .andExpect(jsonPath("$.production.description").value(testProduction.getDescription()))
                    .andExpect(jsonPath("$.production.auditionDate").value(formattedAuditionDate))
                    .andExpect(jsonPath("$.production.sundowners").value(testProduction.isSundowners()))
                    .andExpect(jsonPath("$.production.notConfirmed").value(testProduction.isNotConfirmed()))
                    .andExpect(jsonPath("$.production.flyerFile").value(testProduction.getFlyerFile()))
                    .andExpect(jsonPath("$.production.createdAt").isNotEmpty())
                    .andExpect(jsonPath("$.production.updatedAt").isNotEmpty())
                    .andExpect(jsonPath("$.production.slug").isNotEmpty()

                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataIntegrityViolationExceptionReturns409Error() throws Exception{
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isConflict()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testDataBaseExceptionReturns500Error() throws Exception{
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenThrow(new DatabaseException("Database Exception"));

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isInternalServerError()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testInvalidVenueIdReturns400BadRequest() throws Exception{
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenThrow(new EntityNotFoundException("Invalid Venue Id"));

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNameWithEmptyOtherValuesReturns201() throws Exception{
            // Arrange

            Production emptyNonNameValuesProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );

            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(emptyNonNameValuesProduction);

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\", \"venueId\": \"\", \"author\": \"\", \"description\": \"\", " +
                                            "\"auditionDate\": \"\", \"sundowners\": \"\", \"notConfirmed\": \"\", \"flyerFile\": \"\"}"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNameWithMissingOtherValuesReturns201() throws Exception{
            // Arrange
            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(testProduction);

            Production emptyNonNameValuesProduction = new Production(
                    "Test Production",
                    null, null, null, null, false, false, null
            );

            when(productionService.addNewProduction(any(NewProductionRequest.class))).thenReturn(emptyNonNameValuesProduction);

            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{ \"name\": \"Test Production\"}"
                            ))
                    .andExpect(status().isCreated()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testEmptyNameReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testMissingNameReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testVenueIdNotIntReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"Test Production\", \"venueId\": \"Bad Venue Id\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testAuditionDateNotDateReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"Bad Date\", \"sundowners\": false, \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testSundownersNotBooleanReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": \"Bad Boolean\", \"notConfirmed\": false, \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }

        @Test
        @WithMockUser(roles="ADMIN")
        void testNotConfirmedNotBooleanReturns400BadRequest() throws Exception{
            // Act & Assert
            mockMvc.perform(post("/productions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(
                                    "{\"name\":\"Test Production\", \"venueId\": \"1\", \"author\": \"Test Author\", \"description\": \"Test Description\", " +
                                            "\"auditionDate\": \"2025-10-10T10:00:00\", \"sundowners\": false, \"notConfirmed\": \"Bad Boolean\", \"flyerFile\": \"Test Flyer File\" }"
                            ))
                    .andExpect(status().isBadRequest()
                    );
        }



    }

    @Nested
    @DisplayName("getAllProductions controller tests")
    class getAllProductionsControllerTests {

        @Test
        void testSuccessfulGetReturns200Code() throws Exception {
            // Arrange
            ArrayList<Production> productions = new ArrayList<>();
            when(productionService.getAllProductions()).thenReturn(productions);
            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(status().isOk());

        }

        @Test
        void testSuccessfulGetResponseContainsProductionObject() throws Exception {
            // Arrange
            ArrayList<Production> productions = new ArrayList<>();
            productions.add(testProduction);
            when(productionService.getAllProductions()).thenReturn(productions);
            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(jsonPath("$.productions[0].name").value("Test Production"));

        }

        @Test
        void testDatabaseExceptionReturns500Error() throws Exception {
            // Arrange
            when(productionService.getAllProductions()).thenThrow(new DatabaseException("Database Error"));

            // Act & Assert
            mockMvc.perform(get("/productions"))
                    .andExpect(status().isInternalServerError());
        }

    }

    @Nested
    @DisplayName("getProductionById controller tests")
    class getProductionByIdControllerTests {

        @Test
        void testSuccessfulGetResponds200() throws Exception {
            // Arrange
            when(productionService.getProductionById(anyInt())).thenReturn(testProduction);
            // Act & Assert
            mockMvc.perform(get("/productions/1"))
                    .andExpect(status().isOk());
        }

    }
}
