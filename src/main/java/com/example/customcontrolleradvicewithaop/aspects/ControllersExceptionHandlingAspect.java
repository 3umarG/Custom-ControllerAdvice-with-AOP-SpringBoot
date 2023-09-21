package com.example.customcontrolleradvicewithaop.aspects;

import com.example.customcontrolleradvicewithaop.annotations.CustomExceptionHandler;
import com.example.customcontrolleradvicewithaop.exceptions.CustomBadRequestException;
import com.example.customcontrolleradvicewithaop.exceptions.CustomNotFoundException;
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

        // specialization : get the specific type of thrown exception
        Class<? extends Exception> exceptionType = exception.getClass();

        // trying to get the method for the exception type and perform it...
        for (Method method : exceptionType.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);
                Class<?>[] exceptionTypes = annotation.value(); // get the list of all exceptions putted in the annotation
                for (Class<?> type : exceptionTypes) { // we can get rid of this loop by ensuring we are put only one exception type per annotated method
                    if (type.isAssignableFrom(exceptionType)) {
                        try {
                            return (ResponseEntity<?>) method.invoke(null, exception);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            return getInternalServerErrorResponse();
                        }
                    }
                }
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
            if (method.isAnnotationPresent(CustomExceptionHandler.class)) {
                CustomExceptionHandler annotation = method.getAnnotation(CustomExceptionHandler.class);
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

