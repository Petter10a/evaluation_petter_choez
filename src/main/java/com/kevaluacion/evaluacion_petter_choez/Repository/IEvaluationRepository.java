package com.kevaluacion.evaluacion_petter_choez.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kevaluacion.evaluacion_petter_choez.Entity.Evaluation;

@Repository
public interface IEvaluationRepository extends JpaRepository<Evaluation, Long> {

    /**
     * Finds evaluations by their status.
     *
     * @param genericStatus the list of statuses to filter evaluations
     * @param pageable      the pagination information
     * @return a page of evaluations with the specified statuses
     */
    Page<Evaluation> findByStatusIn(List<String> genericStatus, Pageable pageable);

    /**
     * Finds evaluations by the ID of the employee being evaluated and their status.
     *
     * @param collaboratorId the ID of the employee being evaluated
     * @param genericStatus  the list of statuses to filter evaluations
     * @param pageable       the pagination information
     * @return a page of evaluations for the specified employee and statuses
     */
    Page<Evaluation> findByEvaluateeIdAndStatusIn(Long collaboratorId, List<String> genericStatus, Pageable pageable);

}
