package com.kevaluacion.evaluacion_petter_choez.Enum;

/**
 * Enum representing different categories of evaluation.
 */
public enum CategoryType {
    LIDERAZGO("Liderazgo"),
    COMUNICACION("Comunicación"),
    PROBLEMA("Resolución de Problemas"),
    TRABAJO_EN_EQUIPO("Trabajo en Equipo");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
