package com.kevaluacion.evaluacion_petter_choez.Service;

import org.springframework.stereotype.Service;

import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeAverageScoresDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;

@Service
public interface IEmployeeService {

    /**
     * Creates a new employee based on the provided request DTO.
     *
     * @param employeeRequestDTO the DTO containing employee details
     * @return the response DTO of the created employee
     */
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO);

    /**
     * Retrieves all employees with pagination and sorting.
     *
     * @param page          the page number (0-based)
     * @param size          the size of the page
     * @param sortBy        the field to sort by
     * @param sortDirection the direction of sorting (ASC or DESC)
     * @return a paginated response containing employee response DTOs
     */
    public PaginatedResponse<EmployeeResponseDTO> getAllEmployees(int page, int size, String sortBy,
            String sortDirection);

    /**
     * Retrieves an employee by their ID.
     *
     * @param id the ID of the employee
     * @return the response DTO of the employee, or null if not found
     */
    public EmployeeResponseDTO getEmployeeById(Long id);

    /**
     * Retrieves the average scores of an employee by their ID.
     *
     * @param employeeId the ID of the employee
     * @return the DTO containing the average scores of the employee
     */
    public EmployeeAverageScoresDTO getAverageByEmployeeId(Long employeeId);

}
