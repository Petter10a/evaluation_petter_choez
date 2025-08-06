package com.kevaluacion.evaluacion_petter_choez.Config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kevaluacion.evaluacion_petter_choez.Constant.StatusConst;
import com.kevaluacion.evaluacion_petter_choez.Entity.Employee;
import com.kevaluacion.evaluacion_petter_choez.Entity.Evaluation;
import com.kevaluacion.evaluacion_petter_choez.Entity.EvaluationCategoryScore;
import com.kevaluacion.evaluacion_petter_choez.Enum.CategoryType;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEmployeeRepository;
import com.kevaluacion.evaluacion_petter_choez.Repository.IEvaluationRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final IEmployeeRepository employeeRepository;
    private final IEvaluationRepository evaluationRepository;

    public DataLoader(IEmployeeRepository employeeRepository, IEvaluationRepository evaluationRepository) {
        this.employeeRepository = employeeRepository;
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (employeeRepository.count() == 0) {
            logger.info("Cargando datos de prueba...");
            loadSampleData();
            logger.info("Datos de prueba cargados exitosamente!");
        } else {
            logger.info("Los datos ya existen, omitiendo la carga automática.");
        }
    }

    private void loadSampleData() {
        Employee emp1 = new Employee();
        emp1.setFullName("Juan Pérez");
        emp1.setEmail("juan.perez@empresa.com");
        emp1.setStatus(StatusConst.ACTIVE);

        Employee emp2 = new Employee();
        emp2.setFullName("María García");
        emp2.setEmail("maria.garcia@empresa.com");
        emp2.setStatus(StatusConst.ACTIVE);

        Employee emp3 = new Employee();
        emp3.setFullName("Carlos Rodríguez");
        emp3.setEmail("carlos.rodriguez@empresa.com");
        emp3.setStatus(StatusConst.ACTIVE);

        Employee emp4 = new Employee();
        emp4.setFullName("Ana Martínez");
        emp4.setEmail("ana.martinez@empresa.com");
        emp4.setStatus(StatusConst.ACTIVE);

        Employee emp5 = new Employee();
        emp5.setFullName("Pedro Ramírez");
        emp5.setEmail("pedro.ramirez@empresa.com");
        emp5.setStatus(StatusConst.ACTIVE);

        emp1 = employeeRepository.save(emp1);
        emp2 = employeeRepository.save(emp2);
        emp3 = employeeRepository.save(emp3);
        emp4 = employeeRepository.save(emp4);
        emp5 = employeeRepository.save(emp5);

        logger.info("Empleados creados: {}", Arrays.asList(emp1.getFullName(), emp2.getFullName(),
                emp3.getFullName(), emp4.getFullName(), emp5.getFullName()));

        createEvaluation(emp3, emp1, "Evaluación trimestral Q1",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 5.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 4.0),
                        new CategoryScore(CategoryType.COMUNICACION, 4.0),
                        new CategoryScore(CategoryType.PROBLEMA, 5.0)));

        createEvaluation(emp4, emp1, "Evaluación anual 2024",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 4.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 5.0),
                        new CategoryScore(CategoryType.COMUNICACION, 4.0),
                        new CategoryScore(CategoryType.PROBLEMA, 4.0)));

        createEvaluation(emp3, emp2, "Evaluación de desempeño",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 4.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 5.0),
                        new CategoryScore(CategoryType.COMUNICACION, 4.0),
                        new CategoryScore(CategoryType.PROBLEMA, 4.0)));

        createEvaluation(emp1, emp5, "Evaluación mentor-aprendiz",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 3.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 4.0),
                        new CategoryScore(CategoryType.COMUNICACION, 4.0),
                        new CategoryScore(CategoryType.PROBLEMA, 4.0)));

        createEvaluation(emp4, emp3, "Evaluación de liderazgo",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 5.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 5.0),
                        new CategoryScore(CategoryType.COMUNICACION, 5.0),
                        new CategoryScore(CategoryType.PROBLEMA, 5.0)));

        createEvaluation(emp4, emp5, "Evaluación adicional Pedro",
                Arrays.asList(
                        new CategoryScore(CategoryType.LIDERAZGO, 4.0),
                        new CategoryScore(CategoryType.TRABAJO_EN_EQUIPO, 4.0),
                        new CategoryScore(CategoryType.COMUNICACION, 4.0),
                        new CategoryScore(CategoryType.PROBLEMA, 4.0)));

        logger.info("Evaluaciones creadas exitosamente");
    }

    private void createEvaluation(Employee evaluator, Employee evaluatee, String description,
            List<CategoryScore> scores) {
        Evaluation evaluation = Evaluation.builder()
                .evaluator(evaluator)
                .evaluatee(evaluatee)
                .date(LocalDate.now())
                .build();

        evaluation.setStatus(StatusConst.ACTIVE);

        List<EvaluationCategoryScore> categoryScores = new ArrayList<>();
        for (CategoryScore score : scores) {
            EvaluationCategoryScore categoryScore = new EvaluationCategoryScore();
            categoryScore.setEvaluation(evaluation);
            categoryScore.setCategory(score.getCategory());
            categoryScore.setScore(score.getScore().intValue());
            categoryScore.setStatus(StatusConst.ACTIVE);

            categoryScores.add(categoryScore);
        }

        evaluation.setCategories(categoryScores);
        evaluationRepository.save(evaluation);
        logger.info("Evaluación creada: {} evaluó a {} - {}",
                evaluator.getFullName(), evaluatee.getFullName(), description);
    }

    private static class CategoryScore {
        private final CategoryType category;
        private final Double score;

        public CategoryScore(CategoryType category, Double score) {
            this.category = category;
            this.score = score;
        }

        public CategoryType getCategory() {
            return category;
        }

        public Double getScore() {
            return score;
        }
    }
}
