package com.example.customcontrolleradvicewithaop.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
public class ControllersExceptionHandlingAspect {


    /**
     * This aspect get the Exception type as a general type. "generalization".
     * Then it tries to get the specific Exception type/class "specialization".
     * Then it uses `this.getClass().getDeclaredMethods()` to iterate over all Methods annotated with the `@ExceptionHandler`.
     * Then try to find the method for both the `@ExceptionHandler` & the specified Exception thrown.
     * After finding matched method : `isPresent()` it returns to get the `ResponseEntity`.
     */
    @AfterThrowing(
            pointcut = "@within(com.example.customcontrolleradvicewithaop.annotations.ControllerHandler)",
            throwing = "exception"
    )
    public ResponseEntity<?> handleCustomExceptions(Exception exception) {

        Optional<Method> method = findHandlerMethodWithExceptionType(exception);
        if (method.isPresent()) {
            try {
                return (ResponseEntity<?>) method.get().invoke(null, exception);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return getInternalServerErrorResponse();
            }
        }

        return getInternalServerErrorResponse();
    }


    /**
     * Extracted method to use , but it does same work with the full method above
     */
    private Optional<Method> findHandlerMethodWithExceptionType(Exception exception) {
        Class<? extends Exception> exceptionType = exception.getClass();

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
                if (Arrays.asList(annotation.value()).contains(exceptionType)) {
                    return Optional.of(method);
                }
            }
        }

        return Optional.empty();
    }

    private ResponseEntity<?> getInternalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error occurred, we will work on fixing it ..!!");
    }

}

