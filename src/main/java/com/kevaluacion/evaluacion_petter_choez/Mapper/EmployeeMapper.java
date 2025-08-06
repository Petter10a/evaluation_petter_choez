package com.kevaluacion.evaluacion_petter_choez.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EmployeeResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    /**
     * Converts an EmployeeRequestDTO to an Employee entity.
     *
     * @param employeeRequestDTO the DTO containing employee details
     * @return the Employee entity
     */
    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeRequestDTO employeeRequestDTO);

    /**
     * Converts an Employee entity to an EmployeeResponseDTO.
     *
     * @param employee the Employee entity
     * @return the EmployeeResponseDTO
     */
    EmployeeResponseDTO toResponseDTO(Employee employee);
}
