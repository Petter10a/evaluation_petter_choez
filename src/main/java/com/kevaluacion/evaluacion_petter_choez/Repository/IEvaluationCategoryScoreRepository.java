package com.kevaluacion.evaluacion_petter_choez.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kevaluacion.evaluacion_petter_choez.Entity.EvaluationCategoryScore;

@Repository
public interface IEvaluationCategoryScoreRepository extends JpaRepository<EvaluationCategoryScore, Long> {

    /**
     * Obtiene los promedios de puntuaciones por categoría para un empleado
     * específico
     * 
     * @param evaluateeId ID del empleado evaluado
     * @return Lista de arrays [CategoryType, Double] con categoría y promedio
     */
    @Query("""
            SELECT ecs.category, AVG(ecs.score)
            FROM EvaluationCategoryScore ecs
            INNER JOIN ecs.evaluation e
            WHERE e.evaluatee.id = :evaluateeId
            AND e.status = 'ACTIVE'
            AND ecs.status = 'ACTIVE'
            GROUP BY ecs.category
            """)
    List<Object[]> findAverageScoresByEmployeeId(@Param("evaluateeId") Long evaluateeId);
}
