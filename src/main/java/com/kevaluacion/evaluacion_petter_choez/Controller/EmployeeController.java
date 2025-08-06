package com.kevaluacion.evaluacion_petter_choez.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeAverageScoresDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Service.IEmployeeService;
import com.kevaluacion.evaluacion_petter_choez.Util.ApiGenericResponse;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;
import com.kevaluacion.evaluacion_petter_choez.Util.ResponseUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final IEmployeeService employeeService;

    public EmployeeController(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<EmployeeResponseDTO>> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        EmployeeResponseDTO createdEmployee = employeeService.createEmployee(employeeRequestDTO);
        return ResponseUtil.created("Employee created successfully", createdEmployee);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<EmployeeResponseDTO>>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        PaginatedResponse<EmployeeResponseDTO> employees = employeeService.getAllEmployees(page, size, sortBy,
                sortDirection);
        return ResponseUtil.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<EmployeeResponseDTO>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponseDTO employee = employeeService.getEmployeeById(id);
        return ResponseUtil.ok(employee);
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiGenericResponse<EmployeeAverageScoresDTO>> getAverageByEmployeeId(@PathVariable Long id) {
        EmployeeAverageScoresDTO employeeSummary = employeeService.getAverageByEmployeeId(id);
        return ResponseUtil.ok(employeeSummary);
    }

}
