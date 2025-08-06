package com.kevaluacion.evaluacion_petter_choez.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their email.
     *
     * @param email the email of the employee
     * @return the employee with the specified email, or null if not found
     */
    public boolean existsByEmail(String email);

    /**
     * Finds employees by their status.
     *
     * @param statuses the list of statuses to filter employees
     * @param pageable the pagination information
     * @return a page of employees with the specified statuses
     */
    public Page<Employee> findByStatusIn(List<String> statuses, Pageable pageable);

}
