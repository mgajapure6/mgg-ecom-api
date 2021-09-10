package com.ecom.user.mapper;

import com.ecom.user.dto.UserDTO;
import com.ecom.user.model.User;

public class UserMapper {

	public static User mapToUser(UserDTO userRequest) {
		return new User(userRequest.getId());
	}

	public static UserDTO mapToUserDTO(User user) {
		return new UserDTO(user.getId());
	}

}
