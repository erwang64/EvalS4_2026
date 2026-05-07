package edu.esiea.LunarBaseApi.controller.dto;

import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;

public class AbstractRequest {
	
	private final ResourceType resourceType;
	
	public AbstractRequest(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	
	public ResourceType getResourceType() {
		return this.resourceType;
	}

}
