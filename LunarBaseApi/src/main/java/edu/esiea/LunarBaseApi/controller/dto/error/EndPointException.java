package edu.esiea.LunarBaseApi.controller.dto.error;

import org.springframework.http.HttpStatus;

public class EndPointException extends Exception{
	
	private final HttpStatus status;
	private final ResourceType resourceType;
	private int id = 0;
	
	public EndPointException(HttpStatus status, String message, ResourceType resourceType, Throwable cause, int id) {
		this(status, message, resourceType, cause);
		this.id = id;
	}
	
	public EndPointException(HttpStatus status, String message, ResourceType resourceType, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.resourceType = resourceType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

}
