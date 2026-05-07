package edu.esiea.LunarBaseApi.controller.dto.mapper;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import edu.esiea.LunarBaseApi.controller.dto.AbstractRequest;
import edu.esiea.LunarBaseApi.controller.dto.ErrorResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.error.ResourceType;

public class ErrorMapper {
	
	public static ErrorResponse endPointExceptionToErrorDTO(EndPointException e) {
		ErrorResponse ret = new ErrorResponse();
		ret.setId(e.getId());
		ret.setMessage(e.getMessage());
		ret.setResourceType(e.getResourceType());
		return ret;
	}
	
	public static ErrorResponse methodArgumentNotValideExceptionToErrorDTO (MethodArgumentNotValidException e) {
		BindingResult result = e.getBindingResult();
		
		ResourceType resourceType = getResourceTypeFromTarget(result.getTarget());
		
		String msg = "Erreur(s) de validation : ";
		boolean first = true;
		for (FieldError field : result.getFieldErrors()) {
			if (first) {
				first = false;
			} else {
				msg = msg.concat(" / ");
			}
			msg = msg.concat(field.getField()).concat(" -> ").concat(field.getDefaultMessage());
		}
		ErrorResponse dto = new ErrorResponse();
		dto.setMessage(msg);
		dto.setId(0);
		dto.setResourceType(resourceType);
		return dto;
	}
	
	private static ResourceType getResourceTypeFromTarget(Object target) {
		if(AbstractRequest.class.isAssignableFrom(target.getClass())) {
			AbstractRequest req = (AbstractRequest) target;
			return req.getResourceType();
		}
		return null;
	}

}
