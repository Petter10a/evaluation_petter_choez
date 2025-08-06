package com.kevaluacion.evaluacion_petter_choez.Validation;

import org.springframework.stereotype.Component;

import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.Exception.DuplicateResourceException;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;

@Component
public class EmployeeValidation {
    private final IEmployeeRepository employeeRepository;

    public EmployeeValidation(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Validates the employee request before creating an employee.
     *
     * @param employeeRequestDTO the DTO containing employee details
     * @throws IllegalArgumentException if the email is null, empty, or already
     *                                  exists
     */
    public void createEmployeeValidation(EmployeeRequestDTO employeeRequestDTO) {
        String email = employeeRequestDTO.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("The email cannot be null or empty");
        }
        if (employeeRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("An employee with the email already exists: " + email);
        }
    }

}
