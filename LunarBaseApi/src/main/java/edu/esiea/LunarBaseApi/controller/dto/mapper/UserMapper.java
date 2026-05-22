package edu.esiea.LunarBaseApi.controller.dto.mapper;

import edu.esiea.LunarBaseApi.controller.dto.UserRequest;
import edu.esiea.LunarBaseApi.controller.dto.UserResponse;
import edu.esiea.LunarBaseApi.model.User;

public class UserMapper {
	

		public static User toEntity(UserRequest req) {
			User user = new User();
			user.setUsername(req.getUsername());
			user.setPassword(req.getPassword());
			user.setEmail(req.getEmail());
			user.setRole(req.getRole());
			return user;
		}
		
		
		public static UserResponse toResponse(User user) {
			UserResponse resp = new UserResponse();
			resp.setUserId(user.getUserId());
			resp.setUsername(user.getUsername());
			resp.setEmail(user.getEmail());
			resp.setRole(user.getRole());
			return resp;
		}

}
