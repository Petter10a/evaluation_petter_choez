package com.kevaluacion.evaluacion_petter_choez.DTO;

import java.util.Map;

import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;

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
public class EmployeeAverageScoresDTO {

    private Long employeeId;

    private String fullName;

    private Map<CategoryType, Double> averageScores;
}
