package com.kevaluacion.evaluacion_petter_choez.Service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kevaluacion.evaluacion_petter_choez.Constant.StatusConst;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeAverageScoresDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;
import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;
import com.kevaluacion.evaluacion_petter_choez.Exception.ResourceNotFoundException;
import com.kevaluacion.evaluacion_petter_choez.Mapper.EmployeeMapper;
import com.kevaluacion.evaluacion_petter_choez.Mapper.PageableMapper;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEvaluationCategoryScoreRepository;
import com.kevaluacion.evaluacion_petter_choez.Service.IEmployeeService;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;
import com.kevaluacion.evaluacion_petter_choez.Validation.EmployeeValidation;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    private final IEvaluationCategoryScoreRepository evaluationCategoryScoreRepository;

    private final EmployeeValidation employeeValidation;

    private final EmployeeMapper employeeMapper;

    private final PageableMapper pageableMapper;

    public EmployeeServiceImpl(IEmployeeRepository employeeRepository, EmployeeValidation employeeValidation,
            EmployeeMapper employeeMapper, PageableMapper pageableMapper,
            IEvaluationCategoryScoreRepository evaluationCategoryScoreRepository) {
        this.employeeRepository = employeeRepository;
        this.evaluationCategoryScoreRepository = evaluationCategoryScoreRepository;
        this.employeeValidation = employeeValidation;
        this.employeeMapper = employeeMapper;
        this.pageableMapper = pageableMapper;
    }

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        employeeValidation.createEmployeeValidation(employeeRequestDTO);
        Employee employee = employeeMapper.toEntity(employeeRequestDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(savedEmployee);
    }

    @Override
    public PaginatedResponse<EmployeeResponseDTO> getAllEmployees(int page, int size, String sortBy,
            String sortDirection) {
        Pageable pageable = pageableMapper.toPageable(page, size, sortBy, sortDirection);
        Page<Employee> paginatedEmployees = employeeRepository.findByStatusIn(StatusConst.GENERIC_STATUS, pageable);
        Page<EmployeeResponseDTO> paginatedResponse = paginatedEmployees.map(employeeMapper::toResponseDTO);
        return PaginatedResponse.of(paginatedResponse);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public EmployeeAverageScoresDTO getAverageByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        List<Object[]> averageResults = evaluationCategoryScoreRepository.findAverageScoresByEmployeeId(employeeId);

        Map<CategoryType, Double> averageScores = new HashMap<>();
        for (Object[] result : averageResults) {
            CategoryType category;
            if (result[0] instanceof CategoryType) {
                category = (CategoryType) result[0];
            } else {
                category = CategoryType.valueOf((String) result[0]);
            }
            Double average = (Double) result[1];
            averageScores.put(category, Math.round(average * 100.0) / 100.0);
        }

        if (averageScores.isEmpty()) {
            for (CategoryType category : CategoryType.values()) {
                averageScores.put(category, 0.0);
            }
        }

        return EmployeeAverageScoresDTO.builder()
                .employeeId(employee.getId())
                .fullName(employee.getFullName())
                .averageScores(averageScores)
                .build();
    }
}
