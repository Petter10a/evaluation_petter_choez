package com.kevaluacion.evaluacion_petter_choez.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Service.IEvaluationService;
import com.kevaluacion.evaluacion_petter_choez.Util.ApiGenericResponse;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;
import com.kevaluacion.evaluacion_petter_choez.Util.ResponseUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    private final IEvaluationService evaluationService;

    public EvaluationController(IEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<ApiGenericResponse<EvaluationResponseDTO>> createEvaluation(
            @Valid @RequestBody EvaluationRequestDTO evaluationRequestDTO) {
        EvaluationResponseDTO evaluation = evaluationService.createEvaluation(evaluationRequestDTO);
        return ResponseUtil.created("Evaluation created successfully", evaluation);
    }

    @GetMapping
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<EvaluationResponseDTO>>> getAllEvaluations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        PaginatedResponse<EvaluationResponseDTO> evaluations = evaluationService.getAllEvaluations(page, size, sortBy,
                sortDirection);
        return ResponseUtil.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiGenericResponse<EvaluationResponseDTO>> getEvaluationById(@PathVariable Long id) {
        EvaluationResponseDTO evaluation = evaluationService.getEvaluationById(id);
        return ResponseUtil.ok(evaluation);
    }

    @GetMapping("/employee/{collaboratorId}")
    public ResponseEntity<ApiGenericResponse<PaginatedResponse<EvaluationResponseDTO>>> getAllEvaluationsByCollaboratorId(
            @PathVariable Long collaboratorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        PaginatedResponse<EvaluationResponseDTO> evaluations = evaluationService
                .getAllEvaluationsByCollaboratorId(collaboratorId, page, size, sortBy, sortDirection);
        return ResponseUtil.ok(evaluations);
    }
}
