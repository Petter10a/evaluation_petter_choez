package com.kevaluacion.evaluacion_petter_choez.Validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationCategoryScoreRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.DTO.EvaluationRequestDTO;
import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;
import com.kevaluacion.evaluacion_petter_choez.Exception.DuplicateResourceException;
import com.kevaluacion.evaluacion_petter_choez.Exception.ResourceNotFoundException;
import com.kevaluacion.evaluacion_petter_choez.Service.IEmployeeService;

@Component
public class EvaluationValidation {

    private final IEmployeeService employeeService;

    public EvaluationValidation(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Validates the evaluation request before creating an evaluation.
     *
     * @param evaluationRequestDTO the DTO containing evaluation details
     * @throws ResourceNotFoundException  if any required employee is not found
     * @throws DuplicateResourceException if the evaluator and evaluatee are the
     *                                    same
     */
    public void createEvaluationValidation(EvaluationRequestDTO evaluationRequestDTO) {
        validateEmployeesExist(evaluationRequestDTO);
        validateNoDuplicateCategories(evaluationRequestDTO.getEvaluationCategoryScore());
        validateAllCategoriesPresent(evaluationRequestDTO.getEvaluationCategoryScore());
    }

    private void validateEmployeesExist(EvaluationRequestDTO evaluationRequestDTO) {
        try {
            employeeService.getEmployeeById(evaluationRequestDTO.getEvaluatorId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "Evaluator not found with id: " + evaluationRequestDTO.getEvaluatorId());
        }

        try {
            employeeService.getEmployeeById(evaluationRequestDTO.getEvaluateeId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "Evaluatee not found with id: " + evaluationRequestDTO.getEvaluateeId());
        }

        if (evaluationRequestDTO.getEvaluatorId().equals(evaluationRequestDTO.getEvaluateeId())) {
            throw new DuplicateResourceException("The evaluator and evaluatee cannot be the same employee");
        }
    }

    private void validateNoDuplicateCategories(List<EvaluationCategoryScoreRequestDTO> categories) {
        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("The evaluation categories are required");
        }

        Set<CategoryType> seenCategories = new HashSet<>();
        for (EvaluationCategoryScoreRequestDTO categoryScore : categories) {
            if (categoryScore.getCategory() == null) {
                throw new ResourceNotFoundException("The category cannot be null");
            }

            if (seenCategories.contains(categoryScore.getCategory())) {
                throw new DuplicateResourceException(
                        "Duplicate category found: " + categoryScore.getCategory().getDisplayName());
            }
            seenCategories.add(categoryScore.getCategory());
        }
    }

    private void validateAllCategoriesPresent(List<EvaluationCategoryScoreRequestDTO> categories) {
        Set<CategoryType> providedCategories = new HashSet<>();
        categories.forEach(cat -> providedCategories.add(cat.getCategory()));

        Set<CategoryType> requiredCategories = Set.of(CategoryType.values());

        if (!providedCategories.equals(requiredCategories)) {
            throw new ResourceNotFoundException(
                    "Missing required categories. All categories are required: LEADERSHIP, COMMUNICATION, PROBLEM_SOLVING, TEAMWORK");
        }
    }
}
