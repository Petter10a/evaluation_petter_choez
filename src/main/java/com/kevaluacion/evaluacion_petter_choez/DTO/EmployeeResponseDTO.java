package com.kevaluacion.evaluacion_petter_choez.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private Long id;

    private String fullName;

    private String email;

    private String status;
}
