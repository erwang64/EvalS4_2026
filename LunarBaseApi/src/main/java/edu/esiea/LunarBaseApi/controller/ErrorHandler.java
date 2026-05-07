package edu.esiea.LunarBaseApi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.esiea.LunarBaseApi.controller.dto.ErrorResponse;
import edu.esiea.LunarBaseApi.controller.dto.error.EndPointException;
import edu.esiea.LunarBaseApi.controller.dto.mapper.ErrorMapper;


@ControllerAdvice
public class ErrorHandler {
	
	// Le Logger permet d'écrire les erreurs dans la console de votre serveur (très utile pour débugger)
		private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);
		
		// 1. Attrape vos erreurs personnalisées
		@ExceptionHandler(EndPointException.class)
		public ResponseEntity<ErrorResponse> handleException(EndPointException e){
			ErrorResponse resp = ErrorMapper.endPointExceptionToErrorDTO(e);
			LOGGER.error(resp.getResourceType().toString().concat(" : ").concat(resp.getMessage()));
			return ResponseEntity.status(e.getStatus()).body(resp);
		}

		// 2. Attrape les erreurs de validation (quand le JSON reçu est incomplet)
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex){
			ErrorResponse resp = ErrorMapper.methodArgumentNotValideExceptionToErrorDTO(ex);
			LOGGER.warn(resp.getResourceType().toString().concat(" : ").concat(resp.getMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		}
		
		/* * 3. Attrape les erreurs de sécurité (À décommenter quand vous ferez Spring Security)
		 *
		import org.springframework.security.access.AccessDeniedException;
		
		@ExceptionHandler(AccessDeniedException.class)
		public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e){
			ErrorResponse resp = new ErrorResponse();
			resp.setMessage("Access denied");
			resp.setResourceType(ResourceType.USER); // Ou créer un ResourceType spécifique
			LOGGER.error("Access denied : ".concat(e.getMessage()));
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
		}
		*/

}
