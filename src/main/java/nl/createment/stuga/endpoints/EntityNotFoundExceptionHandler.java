package nl.createment.stuga.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import nl.createment.stuga.db.config.EntityNotFoundException;

@ControllerAdvice
public class EntityNotFoundExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public String handleEntityNotFound(EntityNotFoundException e) {
	    return e.getMessage();
	}
	
}
