package com.kevaluacion.evaluacion_petter_choez.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationResponseDTO {

    private Long id;

    private String evaluator;

    private String evaluatee;

    private String date;

    private List<EvaluationCategoryScoreResponseDTO> evaluationCategoryScore;

    private String status;
}
