package com.kevaluacion.evaluacion_petter_choez.Integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@DisplayName("Tests de integración para Employee")
class EmployeeIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IEmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Integración completa: Crear y obtener empleado")
    void testCreateAndGetEmployee_Integration() throws Exception {
        // Given
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFullName("Integration Test User");
        employeeRequestDTO.setEmail("integration.test@empresa.com");

        // When - Crear empleado
        String createResponse = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fullName").value("Integration Test User"))
                .andExpect(jsonPath("$.data.email").value("integration.test@empresa.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extraer ID del empleado creado (simplificado para el test)
        assertTrue(createResponse.contains("Integration Test User"));

        // Verificar que se guardó en la base de datos
        assertTrue(employeeRepository.existsByEmail("integration.test@empresa.com"));

        // When - Obtener lista de empleados
        mockMvc.perform(get("/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    @DisplayName("Integración: Obtener promedios de empleado con datos del DataLoader")
    void testGetEmployeeAverages_WithDataLoader() throws Exception {
        // Given - Los datos del DataLoader ya deberían estar cargados
        // Pedro Ramírez debería tener ID 5 según el DataLoader

        // When - Obtener promedios del empleado Pedro Ramírez
        mockMvc.perform(get("/employees/{id}/summary", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employeeId").value(5L))
                .andExpect(jsonPath("$.data.fullName").value("Pedro Ramírez"))
                .andExpect(jsonPath("$.data.averageScores").exists())
                .andExpect(jsonPath("$.data.averageScores.LIDERAZGO").exists())
                .andExpect(jsonPath("$.data.averageScores.COMUNICACION").exists())
                .andExpect(jsonPath("$.data.averageScores.PROBLEMA").exists())
                .andExpect(jsonPath("$.data.averageScores.TRABAJO_EN_EQUIPO").exists());
    }

    @Test
    @DisplayName("Integración: Obtener empleado por ID inexistente debe retornar 404")
    void testGetEmployeeById_NotFound_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Integración: Crear empleado con email duplicado debe retornar 409")
    void testCreateEmployee_DuplicateEmail_Integration() throws Exception {
        // Given - Intentar crear empleado con email que ya existe del DataLoader
        EmployeeRequestDTO duplicateEmployeeDTO = new EmployeeRequestDTO();
        duplicateEmployeeDTO.setFullName("Duplicate User");
        duplicateEmployeeDTO.setEmail("juan.perez@empresa.com"); // Email del DataLoader

        // When & Then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateEmployeeDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("An employee with the email already exists: juan.perez@empresa.com"));
    }
}
