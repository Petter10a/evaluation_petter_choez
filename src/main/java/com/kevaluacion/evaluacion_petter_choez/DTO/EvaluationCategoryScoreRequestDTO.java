package com.kevaluacion.evaluacion_petter_choez.DTO;

import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationCategoryScoreRequestDTO {

    @NotNull(message = "La categoría es obligatoria")
    private CategoryType category;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer score;

}
