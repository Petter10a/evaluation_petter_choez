package com.kevaluacion.evaluacion_petter_choez.Util;

import com.kevaluacion.evaluacion_petter_choez.Constant.ApiStatusConst;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic response DTO for API responses.
 *
 * @param <T> the type of data contained in the response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiGenericResponse<T> {

    @Builder.Default
    private int statusCode = ApiStatusConst.SUCCESS_CODE;

    @Builder.Default
    private String status = ApiStatusConst.SUCCESS_STATUS;

    @Builder.Default
    private String message = ApiStatusConst.SUCCESS_MESSAGE;

    private T data;
}
