package com.example.movieappbackend.api.exceptionhandler;

import java.time.OffsetDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
            "Ocorreu um erro interno inesperado no sistema. " +
            "Tente novamente e se o problema persistir, entre em contato " +
            "com o administrador do sistema.";
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorType apiErrorType = ApiErrorType.ERRO_DE_SISTEMA;
        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(
    		EntityNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorType apiErrorType = ApiErrorType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityInUseException(
    		EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorType apiErrorType = ApiErrorType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(
    		BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorType apiErrorType = ApiErrorType.ERRO_DE_NGOCIO;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex,
			Object body,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request
	) {

        if(body == null) {
            body = ApiError.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        } else if(body instanceof String) {
            body = ApiError.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        }

        ex.printStackTrace();

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ApiError.ApiErrorBuilder createApiErrorBuilder(HttpStatus status,
    													ApiErrorType apiErrorType,
                                                        String detail) {
        return ApiError.builder()
                .status(status.value())
                .type(apiErrorType.getUri())
                .title(apiErrorType.getTitle())
                .timestamp(OffsetDateTime.now())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .detail(detail);
    }
}
