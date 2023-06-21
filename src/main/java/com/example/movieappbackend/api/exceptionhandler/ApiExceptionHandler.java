package com.example.movieappbackend.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.movieappbackend.domain.exception.BusinessException;
import com.example.movieappbackend.domain.exception.EntityInUseException;
import com.example.movieappbackend.domain.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String GENERIC_ERROR_MESSAGE = "An unexpected internal system "
			+ "error has occurred. Try again and if the problem persists, contact us with your "
			+ "system administrator.";
	
    private final MessageSource messageSource;
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(
            Exception ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiErrorType apiErrorType = ApiErrorType.SYSTEM_ERROR;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	 @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

	 	ApiErrorType problemType = ApiErrorType.RESOURCE_NOT_FOUND;
        String detail = String.format(
                "no resource found for %s", ex.getRequestURL()
        );

        ApiError apiError = createApiErrorBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if(rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause,
                    headers, status, request);
        } else if(rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause,
                    headers, status, request);
        }

        ApiErrorType apiErrorType = ApiErrorType.INCOMPREHENSIBLE_MESSAGE;
        String detail = "The request body is invalid. Check syntax error.";
        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
    
    @Override
    public ResponseEntity<Object> handleBindException(
            BindException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return handleValidationType(ex, ex.getBindingResult(), headers, status, request);
    }
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(
    		EntityNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiErrorType apiErrorType = ApiErrorType.RESOURCE_NOT_FOUND;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityInUseException(
    		EntityInUseException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorType apiErrorType = ApiErrorType.ENTITY_IN_USE;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }
	
	@ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(
    		BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiErrorType apiErrorType = ApiErrorType.BUSINESS_ERROR;
        String detail = ex.getMessage();

        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status, request);
    }

    public ResponseEntity<Object> handleValidationType(
            Exception ex,
            BindingResult bindingResult,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ApiErrorType problemType = ApiErrorType.INVALID_DATA;
        String detail = "One or more fields are invalid.";

        List<ApiError.Object> problemObjects = bindingResult.getAllErrors()
                .stream().map(objectError -> {
                    String message = messageSource.getMessage(objectError,
                            LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if(objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return ApiError.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                }).collect(Collectors.toList());

        ApiError apiError = createApiErrorBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemObjects)
                .build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }
    
    private ResponseEntity<Object> handlePropertyBinding(
            PropertyBindingException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));


        ApiErrorType apiErrorType = ApiErrorType.INCOMPREHENSIBLE_MESSAGE;
        String detail = String.format("The property '%s' does not exist.", path);
        ApiError apiError = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(
            InvalidFormatException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        ApiErrorType apiErrorType = ApiErrorType.INCOMPREHENSIBLE_MESSAGE;
        String detail = String.format(
                "The property '%s' received the value '%s'," +
                        " that is an invalid type. Correct and enter a compatible value.",
                path, ex.getValue(), ex.getTargetType().getSimpleName()
        );

        ApiError problem = createApiErrorBuilder(status, apiErrorType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
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
                    .userMessage(GENERIC_ERROR_MESSAGE)
                    .build();
        } else if(body instanceof String) {
            body = ApiError.builder()
                    .timestamp(OffsetDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(GENERIC_ERROR_MESSAGE)
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
                .userMessage(GENERIC_ERROR_MESSAGE)
                .detail(detail);
    }
}
