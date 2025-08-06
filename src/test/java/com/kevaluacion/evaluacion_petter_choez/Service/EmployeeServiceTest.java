package com.kevaluacion.evaluacion_petter_choez.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.kevaluacion.evaluacion_petter_choez.Constant.StatusConst;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeAverageScoresDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;
import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;
import com.kevaluacion.evaluacion_petter_choez.Exception.DuplicateResourceException;
import com.kevaluacion.evaluacion_petter_choez.Mapper.EmployeeMapper;
import com.kevaluacion.evaluacion_petter_choez.Mapper.PageableMapper;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEvaluationCategoryScoreRepository;
import com.kevaluacion.evaluacion_petter_choez.Service.Impl.EmployeeServiceImpl;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;
import com.kevaluacion.evaluacion_petter_choez.Validation.EmployeeValidation;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para EmployeeService")
class EmployeeServiceTest {

    @Mock
    private IEmployeeRepository employeeRepository;

    @Mock
    private IEvaluationCategoryScoreRepository evaluationCategoryScoreRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeValidation employeeValidation;

    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDTO employeeRequestDTO;
    private EmployeeResponseDTO employeeResponseDTO;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan.perez@empresa.com")
                .build();
        employee.setStatus(StatusConst.ACTIVE);

        employeeRequestDTO = new EmployeeRequestDTO();
        employeeRequestDTO.setFullName("Juan Pérez");
        employeeRequestDTO.setEmail("juan.perez@empresa.com");

        employeeResponseDTO = EmployeeResponseDTO.builder()
                .id(1L)
                .fullName("Juan Pérez")
                .email("juan.perez@empresa.com")
                .status(StatusConst.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Crear empleado exitosamente")
    void testCreateEmployee_Success() {
        // Given
        doNothing().when(employeeValidation).createEmployeeValidation(any(EmployeeRequestDTO.class));
        when(employeeMapper.toEntity(any(EmployeeRequestDTO.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponseDTO(any(Employee.class))).thenReturn(employeeResponseDTO);

        // When
        EmployeeResponseDTO result = employeeService.createEmployee(employeeRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getFullName());
        assertEquals("juan.perez@empresa.com", result.getEmail());
        verify(employeeValidation).createEmployeeValidation(employeeRequestDTO);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Crear empleado con email duplicado debe lanzar excepción")
    void testCreateEmployee_DuplicateEmail() {
        // Given
        doThrow(new DuplicateResourceException("Ya existe un empleado con el email: juan.perez@empresa.com"))
                .when(employeeValidation).createEmployeeValidation(any(EmployeeRequestDTO.class));

        // When & Then
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> employeeService.createEmployee(employeeRequestDTO));

        assertEquals("Ya existe un empleado con el email: juan.perez@empresa.com", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Obtener empleado por ID exitosamente")
    void testGetEmployeeById_Success() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        when(employeeMapper.toResponseDTO(any(Employee.class))).thenReturn(employeeResponseDTO);

        // When
        EmployeeResponseDTO result = employeeService.getEmployeeById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getFullName());
        verify(employeeRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener todos los empleados paginados")
    void testGetAllEmployees_Success() {
        // Given
        List<Employee> employees = Arrays.asList(employee);
        Page<Employee> employeePage = new PageImpl<>(employees);
        List<String> statuses = Arrays.asList(StatusConst.ACTIVE, StatusConst.INACTIVE);
        Pageable pageable = PageRequest.of(0, 10);

        when(pageableMapper.toPageable(0, 10, "id", "ASC")).thenReturn(pageable);
        when(employeeRepository.findByStatusIn(statuses, pageable)).thenReturn(employeePage);
        when(employeeMapper.toResponseDTO(any(Employee.class))).thenReturn(employeeResponseDTO);

        // When
        PaginatedResponse<EmployeeResponseDTO> result = employeeService.getAllEmployees(0, 10, "id", "ASC");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getContent().size());
        assertEquals("Juan Pérez", result.getContent().get(0).getFullName());
    }

    @Test
    @DisplayName("Obtener promedios de evaluación por empleado")
    void testGetAverageByEmployeeId_Success() {
        // Given
        Long employeeId = 1L;
        List<Object[]> mockResults = Arrays.asList(
                new Object[] { "LIDERAZGO", 4.5 },
                new Object[] { "COMUNICACION", 4.0 },
                new Object[] { "PROBLEMA", 3.8 },
                new Object[] { "TRABAJO_EN_EQUIPO", 4.2 });

        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employee));
        when(evaluationCategoryScoreRepository.findAverageScoresByEmployeeId(employeeId)).thenReturn(mockResults);

        // When
        EmployeeAverageScoresDTO result = employeeService.getAverageByEmployeeId(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("Juan Pérez", result.getFullName());
        assertNotNull(result.getAverageScores());
        assertEquals(4, result.getAverageScores().size());

        // Verificar promedios específicos
        assertEquals(4.5, result.getAverageScores().get(CategoryType.LIDERAZGO));
        assertEquals(4.0, result.getAverageScores().get(CategoryType.COMUNICACION));
        assertEquals(3.8, result.getAverageScores().get(CategoryType.PROBLEMA));
        assertEquals(4.2, result.getAverageScores().get(CategoryType.TRABAJO_EN_EQUIPO));

        verify(employeeRepository).findById(employeeId);
        verify(evaluationCategoryScoreRepository).findAverageScoresByEmployeeId(employeeId);
    }

    @Test
    @DisplayName("Obtener promedios para empleado sin evaluaciones")
    void testGetAverageByEmployeeId_NoEvaluations() {
        // Given
        Long employeeId = 1L;
        List<Object[]> emptyResults = Arrays.asList();

        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employee));
        when(evaluationCategoryScoreRepository.findAverageScoresByEmployeeId(employeeId)).thenReturn(emptyResults);

        // When
        EmployeeAverageScoresDTO result = employeeService.getAverageByEmployeeId(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("Juan Pérez", result.getFullName());
        assertNotNull(result.getAverageScores());
        assertEquals(4, result.getAverageScores().size());

        // Verificar que todas las categorías tienen 0.0 como valor por defecto
        for (CategoryType category : CategoryType.values()) {
            assertEquals(0.0, result.getAverageScores().get(category));
        }
    }
}
