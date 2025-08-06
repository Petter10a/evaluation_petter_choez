package com.kevaluacion.evaluacion_petter_choez.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.Evaluation;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { EvaluationCategoryScoreMapper.class })
public interface EvaluationMapper {

    /**
     * Converts an EvaluationRequestDTO to an Evaluation entity.
     *
     * @param evaluationRequestDTO the DTO containing evaluation details
     * @return the Evaluation entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evaluator", ignore = true)
    @Mapping(target = "evaluatee", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Evaluation toEntity(EvaluationRequestDTO evaluationRequestDTO);

    /**
     * Converts an Evaluation entity to an EvaluationResponseDTO.
     *
     * @param evaluation the Evaluation entity
     * @return the EvaluationResponseDTO
     */
    @Mapping(source = "evaluator.fullName", target = "evaluator")
    @Mapping(source = "evaluatee.fullName", target = "evaluatee")
    @Mapping(source = "categories", target = "evaluationCategoryScore")
    @Mapping(source = "status", target = "status")
    EvaluationResponseDTO toResponseDTO(Evaluation evaluation);
}
