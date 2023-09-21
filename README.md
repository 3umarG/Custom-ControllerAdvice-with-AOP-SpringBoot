# Custom Exception Handling with Spring Boot and Aspect-Oriented Programming (AOP)

This project demonstrates how to implement custom exception handling in a Spring Boot web application using Aspect-Oriented Programming (AOP). Instead of using Spring Boot's built-in `@ControllerAdvice`, this approach allows for dynamic handling of exceptions using custom annotations and an Aspect.
<br>
***Reinventing the wheel makes you better driver !!***

## What is AOP in Spring Boot ??
[GitHub Repository](https://github.com/3umarG/AOP-SpringBoot)
## Project Components

### Custom Exceptions

Two custom exception classes are defined for demonstration purposes:

- [CustomBadRequestException](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fcustomcontrolleradvicewithaop%2Fexceptions%2FCustomBadRequestException.java): Represents a custom bad request exception.
- [CustomNotFoundException](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fcustomcontrolleradvicewithaop%2Fexceptions%2FCustomNotFoundException.java) : Represents a custom not found exception.

```java
public class CustomBadRequestException extends RuntimeException {
    // ...
}

public class CustomNotFoundException extends RuntimeException {
    // ...
}
```
### Custom Annotations
Two custom annotations are used to mark classes and methods for handling exceptions:

- `@ControllerHandler`: Marks a controller class for exception handling.

```Java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ControllerHandler {
}
 
```

### Controller
A sample controller is used to throw custom exceptions. The controller methods are also annotated with `@ExceptionHandler` for the same exceptions, as it would be done in a typical Spring Boot application.

```Java
@RestController
@RequestMapping("/api/test")
@ControllerHandler // new custom handler
public class SimpleController {

    // endpoints
    @GetMapping("/not-found")
    public ResponseEntity<?> getNotFoundTest() {
        throw new CustomNotFoundException("Not Found Resources");
    }
    
    @GetMapping("/bad-request")
    public ResponseEntity<?> getBadRequestTest() {
        throw new CustomBadRequestException("Bad Request");
    }

    
    // methods handler for custom exceptions
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(CustomBadRequestException e) {
        // Handle bad request exception
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(CustomNotFoundException e) {
        // Handle not found exception
    }
}

```

****

### Exception Handling Aspect
The [ControllersExceptionHandlingAspect.java](src%2Fmain%2Fjava%2Fcom%2Fexample%2Fcustomcontrolleradvicewithaop%2Faspects%2FControllersExceptionHandlingAspect.java) class is an Aspect that intercepts exceptions thrown within classes marked with `@ControllerHandler` and handles them dynamically based on the `@ExceptionHandler` annotations.

```Java
@Aspect
@Component
public class ControllersExceptionHandlingAspect {

    @AfterThrowing(
            pointcut = "@within(com.example.customcontrolleradvicewithaop.annotations.ControllerHandler)",
            throwing = "exception"
    )
    public ResponseEntity<?> handleCustomExceptions(Exception exception) {
        // Handles custom exceptions dynamically
    }

    // ...
}

```

### Dependencies
- **Spring Boot :**  Used for building the web application.
- **Spring AOP :** Used for Aspect-Oriented Programming.
- **Java 8 or higher :** used `17`.

### Usage

1. Clone this repository.
2. Ensure you have Maven and Java installed.
3. Build and run the project using Maven:
    ```mvn spring-boot:run```
4. Access the following endpoints in your browser or API tool (e.g., Postman) to see exception handling in action:
    ```Http
   http://localhost:8080/api/test/not-found
   http://localhost:8080/api/test/bad-request 
      ```
   
