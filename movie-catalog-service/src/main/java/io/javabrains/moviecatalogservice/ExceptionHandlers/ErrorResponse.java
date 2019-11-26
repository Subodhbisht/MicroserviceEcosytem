package io.javabrains.moviecatalogservice.ExceptionHandlers;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
public class ErrorResponse {

	public ErrorResponse() {
		// TODO Auto-generated constructor stub
	}
	public ErrorResponse(String message, String details) {
        super();
        this.message = message;
        this.details = details;
    }
 
    //General error message about nature of error
    private String message;
 
    //Specific errors in API request processing
    private String details;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
    
    
}
