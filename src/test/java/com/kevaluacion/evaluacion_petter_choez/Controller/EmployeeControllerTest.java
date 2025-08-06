package com.kevaluacion.evaluacion_petter_choez.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevaluacion.evaluacion_petter_choez.Constant.StatusConst;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeAverageScoresDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;
import com.kevaluacion.evaluacion_petter_choez.Exception.DuplicateResourceException;
import com.kevaluacion.evaluacion_petter_choez.Exception.ResourceNotFoundException;
import com.kevaluacion.evaluacion_petter_choez.Service.IEmployeeService;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;

@WebMvcTest(EmployeeController.class)
@DisplayName("Tests para EmployeeController")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeRequestDTO employeeRequestDTO;
    private EmployeeResponseDTO employeeResponseDTO;
    private EmployeeAverageScoresDTO employeeAverageScoresDTO;

    @BeforeEach
    void setUp() {
        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFullName("Juan Pérez");
        employeeRequestDTO.setEmail("juan.perez@empresa.com");

        employeeResponseDTO = EmployeeResponseDTO.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan.perez@empresa.com")
                .status(StatusConst.ACTIVE)
                .build();

        Map<CategoryType, Double> averageScores = new HashMap<>();
        averageScores.put(CategoryType.LIDERAZGO, 4.5);
        averageScores.put(CategoryType.COMUNICACION, 4.0);
        averageScores.put(CategoryType.PROBLEMA, 3.8);
        averageScores.put(CategoryType.TRABAJO_EN_EQUIPO, 4.2);

        employeeAverageScoresDTO = EmployeeAverageScoresDTO.builder()
                .employeeId(1L)
                .fullName("Juan Pérez")
                .averageScores(averageScores)
                .build();
    }

    @Test
    @DisplayName("Crear empleado exitosamente")
    void testCreateEmployee_Success() throws Exception {
        // Given
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(employeeResponseDTO);

        // When & Then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.email").value("juan.perez@empresa.com"))
                .andExpect(jsonPath("$.data.status").value(StatusConst.ACTIVE));

        verify(employeeService).createEmployee(any(EmployeeRequestDTO.class));
    }

    @Test
    @DisplayName("Crear empleado con datos inválidos debe retornar 400")
    void testCreateEmployee_InvalidData() throws Exception {
        // Given
        EmployeeRequestDTO invalidDTO = new EmployeeRequestDTO();
        invalidDTO.setFullName(""); // Nombre vacío
        invalidDTO.setEmail("invalid-email"); // Email inválido

        // When & Then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(employeeService, never()).createEmployee(any());
    }

    @Test
    @DisplayName("Crear empleado con email duplicado debe retornar 409")
    void testCreateEmployee_DuplicateEmail() throws Exception {
        // Given
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class)))
                .thenThrow(
                        new DuplicateResourceException("Ya existe un empleado con el email: juan.perez@empresa.com"));

        // When & Then
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ya existe un empleado con el email: juan.perez@empresa.com"));
    }

    @Test
    @DisplayName("Obtener empleado por ID exitosamente")
    void testGetEmployeeById_Success() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeResponseDTO);

        // When & Then
        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.email").value("juan.perez@empresa.com"));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    @DisplayName("Obtener empleado por ID inexistente debe retornar 404")
    void testGetEmployeeById_NotFound() throws Exception {
        // Given
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException("Empleado no encontrado con ID: 999"));

        // When & Then
        mockMvc.perform(get("/employees/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Empleado no encontrado con ID: 999"));
    }

    @Test
    @DisplayName("Obtener todos los empleados paginados")
    void testGetAllEmployees_Success() throws Exception {
        // Given
        List<EmployeeResponseDTO> employees = Arrays.asList(employeeResponseDTO);
        PaginatedResponse<EmployeeResponseDTO> paginatedResponse = new PaginatedResponse<>(
                new org.springframework.data.domain.PageImpl<>(employees));

        when(employeeService.getAllEmployees(0, 10, "id", "ASC")).thenReturn(paginatedResponse);

        // When & Then
        mockMvc.perform(get("/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.total").value(1));

        verify(employeeService).getAllEmployees(0, 10, "id", "ASC");
    }

    @Test
    @DisplayName("Obtener promedios de empleado por ID exitosamente")
    void testGetEmployeeAverageScores_Success() throws Exception {
        // Given
        when(employeeService.getAverageByEmployeeId(1L)).thenReturn(employeeAverageScoresDTO);

        // When & Then
        mockMvc.perform(get("/employees/{id}/summary", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employeeId").value(1L))
                .andExpect(jsonPath("$.data.fullName").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.averageScores.LIDERAZGO").value(4.5))
                .andExpect(jsonPath("$.data.averageScores.COMUNICACION").value(4.0))
                .andExpect(jsonPath("$.data.averageScores.PROBLEMA").value(3.8))
                .andExpect(jsonPath("$.data.averageScores.TRABAJO_EN_EQUIPO").value(4.2));

        verify(employeeService).getAverageByEmployeeId(1L);
    }

    @Test
    @DisplayName("Obtener promedios de empleado inexistente debe retornar 404")
    void testGetEmployeeAverageScores_NotFound() throws Exception {
        // Given
        when(employeeService.getAverageByEmployeeId(999L))
                .thenThrow(new ResourceNotFoundException("Empleado no encontrado con ID: 999"));

        // When & Then
        mockMvc.perform(get("/employees/{id}/summary", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Empleado no encontrado con ID: 999"));
    }
}
