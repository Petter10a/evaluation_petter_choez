package com.kevaluacion.evaluacion_petter_choez.Service;

import org.springframework.stereotype.Service;

import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationResponseDTO;
import com.kevaluacion.evaluacion_petter_choez.Util.PaginatedResponse;

@Service
public interface IEvaluationService {

        /**
         * Creates a new evaluation based on the provided request DTO.
         *
         * @param evaluationRequestDTO the DTO containing evaluation details
         * @return the created evaluation response DTO
         */
        public EvaluationResponseDTO createEvaluation(EvaluationRequestDTO evaluationRequestDTO);

        /**
         * Retrieves all evaluations with pagination and sorting.
         *
         * @param page          the page number (0-based)
         * @param size          the size of the page
         * @param sortBy        the field to sort by
         * @param sortDirection the direction of sorting (ASC or DESC)
         * @return a paginated response containing evaluation response DTOs
         */
        public PaginatedResponse<EvaluationResponseDTO> getAllEvaluations(int page, int size, String sortBy,
                        String sortDirection);

        /**
         * Retrieves an evaluation by its ID.
         *
         * @param id the ID of the evaluation
         * @return the response DTO of the evaluation, or null if not found
         */
        public EvaluationResponseDTO getEvaluationById(Long id);

        /**
         * Retrieves all evaluations for a specific employee (collaborator) by their ID
         * with pagination and sorting.
         *
         * @param collaboratorId the ID of the employee being evaluated
         * @param page           the page number (0-based)
         * @param size           the size of the page
         * @param sortBy         the field to sort by
         * @param sortDirection  the direction of sorting (ASC or DESC)
         * @return a paginated response containing evaluation response DTOs for the
         *         specified employee
         */
        public PaginatedResponse<EvaluationResponseDTO> getAllEvaluationsByCollaboratorId(Long collaboratorId, int page,
                        int size, String sortBy, String sortDirection);
}
