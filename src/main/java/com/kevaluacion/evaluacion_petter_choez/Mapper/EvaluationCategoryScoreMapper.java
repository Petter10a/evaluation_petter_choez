package com.kevaluacion.evaluacion_petter_choez.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationCategoryScoreRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationCategoryScoreResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.EvaluationCategoryScore;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EvaluationCategoryScoreMapper {

    /**
     * Converts an EvaluationCategoryScoreRequestDTO to an EvaluationCategoryScore
     * entity.
     *
     * @param dto the DTO containing evaluation category score details
     * @return the EvaluationCategoryScore entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "evaluation", ignore = true)
    EvaluationCategoryScore toEntity(EvaluationCategoryScoreRequestDTO dto);

    /**
     * Converts a list of EvaluationCategoryScoreRequestDTO to a list of
     * EvaluationCategoryScore entities.
     *
     * @param dto the list of DTOs containing evaluation category score details
     * @return the list of EvaluationCategoryScore entities
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "evaluation", ignore = true)
    List<EvaluationCategoryScore> toEntity(List<EvaluationCategoryScoreRequestDTO> dto);

    /**
     * Converts an EvaluationCategoryScore entity to an
     * EvaluationCategoryScoreResponseDTO.
     *
     * @param entity the EvaluationCategoryScore entity
     * @return the EvaluationCategoryScoreResponseDTO
     */
    EvaluationCategoryScoreResponseDTO toResponseDTO(EvaluationCategoryScore entity);

    /**
     * Converts a list of EvaluationCategoryScore entities to a list of
     * EvaluationCategoryScoreResponseDTO.
     *
     * @param entities the list of EvaluationCategoryScore entities
     * @return the list of EvaluationCategoryScoreResponseDTO
     */
    List<EvaluationCategoryScoreResponseDTO> toResponseDTOList(List<EvaluationCategoryScore> entities);
}
