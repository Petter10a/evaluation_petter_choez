package com.kevaluacion.evaluacion_petter_choez.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kevaluacion.evaluacion_petter_choez.Constant.StatusConst;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;
import com.kevaluacion.evaluacion_petter_choez.Entity.Evaluation;
import com.kevaluacion.evaluacion_petter_choez.Entity.EvaluationCategoryScore;
import com.kevaluacion.evaluacion_petter_choez.Exception.ResourceNotFoundException;
import com.kevaluacion.evaluacion_petter_choez.Mapper.EvaluationCategoryScoreMapper;
import com.kevaluacion.evaluacion_petter_choez.Mapper.EvaluationMapper;
import com.kevaluacion.evaluacion_petter_choez.Mapper.PageableMapper;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEvaluationRepository;
import com.kevaluacion.evaluacion_petter_choez.Service.IEvaluationService;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;
import com.kevaluacion.evaluacion_petter_choez.Validation.EvaluationValidation;

@Service
public class EvaluationServiceImpl implements IEvaluationService {

    private final IEvaluationRepository evaluationRepository;

    private final IEmployeeRepository employeeRepository;

    private final EvaluationValidation evaluationValidation;

    private final EvaluationMapper evaluationMapper;

    private final EvaluationCategoryScoreMapper evaluationCategoryScoreMapper;

    private final PageableMapper pageableMapper;

    public EvaluationServiceImpl(EvaluationValidation evaluationValidation, EvaluationMapper evaluationMapper,
            PageableMapper pageableMapper, IEvaluationRepository evaluationRepository,
            EvaluationCategoryScoreMapper evaluationCategoryScoreMapper, IEmployeeRepository employeeRepository) {
        this.evaluationValidation = evaluationValidation;
        this.evaluationMapper = evaluationMapper;
        this.pageableMapper = pageableMapper;
        this.evaluationRepository = evaluationRepository;
        this.employeeRepository = employeeRepository;
        this.evaluationCategoryScoreMapper = evaluationCategoryScoreMapper;
    }

    @Override
    public EvaluationResponseDTO createEvaluation(EvaluationRequestDTO evaluationRequestDTO) {
        evaluationValidation.createEvaluationValidation(evaluationRequestDTO);

        Employee evaluator = employeeRepository.findById(evaluationRequestDTO.getEvaluatorId()).get();
        Employee evaluatee = employeeRepository.findById(evaluationRequestDTO.getEvaluateeId()).get();

        List<EvaluationCategoryScore> evaluationCategoryScore = evaluationCategoryScoreMapper
                .toEntity(evaluationRequestDTO.getEvaluationCategoryScore());

        Evaluation evaluationEntity = Evaluation.builder()
                .evaluator(evaluator)
                .evaluatee(evaluatee)
                .date(LocalDate.now())
                .build();

        evaluationEntity.setCategories(evaluationCategoryScore);
        evaluationCategoryScore.forEach(category -> {
            category.setEvaluation(evaluationEntity);
            category.setStatus(StatusConst.ACTIVE);
        });

        Evaluation savedEvaluation = evaluationRepository.save(evaluationEntity);

        return evaluationMapper.toResponseDTO(savedEvaluation);
    }

    @Override
    public PaginatedResponse<EvaluationResponseDTO> getAllEvaluations(int page, int size, String sortBy,
            String sortDirection) {
        Pageable pageable = pageableMapper.toPageable(page, size, sortBy, sortDirection);
        Page<Evaluation> paginatedEvaluations = evaluationRepository
                .findByStatusIn(StatusConst.GENERIC_STATUS, pageable);
        Page<EvaluationResponseDTO> paginatedResponse = paginatedEvaluations.map(evaluationMapper::toResponseDTO);
        return PaginatedResponse.of(paginatedResponse);
    }

    @Override
    public EvaluationResponseDTO getEvaluationById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + id));
        return evaluationMapper.toResponseDTO(evaluation);
    }

    @Override
    public PaginatedResponse<EvaluationResponseDTO> getAllEvaluationsByCollaboratorId(Long collaboratorId, int page,
            int size, String sortBy, String sortDirection) {
        Pageable pageable = pageableMapper.toPageable(page, size, sortBy, sortDirection);
        Page<Evaluation> paginatedEvaluations = evaluationRepository
                .findByEvaluateeIdAndStatusIn(collaboratorId, StatusConst.GENERIC_STATUS, pageable);
        Page<EvaluationResponseDTO> paginatedResponse = paginatedEvaluations.map(evaluationMapper::toResponseDTO);
        return PaginatedResponse.of(paginatedResponse);
    }

}
