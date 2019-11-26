package io.javabrains.moviecatalogservice.ExceptionHandlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("Inside this methosd");
		ErrorResponse error = new ErrorResponse("Validation Failed", "Resource not found");
        return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}
//	@ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse("Server Error", details);
//        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
