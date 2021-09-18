package com.krishna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.krishna.entity.User;

public interface UserService {

	List<User> getAllUsers();

	Optional<User> getUserById(Integer id);

	User createUser(User user);

	User updateUser(User userModel);

	void deleteUserById(Integer id);

	Optional<User> getUserByEmail(String email);

	Page<User> getAllUsers(int page, int size, Direction direction, String firstName);

}
