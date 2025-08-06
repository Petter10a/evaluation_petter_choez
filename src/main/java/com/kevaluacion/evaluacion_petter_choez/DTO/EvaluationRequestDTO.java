package com.kevaluacion.evaluacion_petter_choez.DTO;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationRequestDTO {

    @NotNull(message = "El evaluador es obligatorio")
    private Long evaluatorId;

    @NotNull(message = "El evaluado es obligatorio")
    private Long evaluateeId;

    private List<EvaluationCategoryScoreRequestDTO> evaluationCategoryScore;

}