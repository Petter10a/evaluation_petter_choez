package com.kevaluacion.evaluacion_petter_choez.DTO;

import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationCategoryScoreResponseDTO {

    private Long id;

    private CategoryType category;

    private Integer score;

    private String status;
}
