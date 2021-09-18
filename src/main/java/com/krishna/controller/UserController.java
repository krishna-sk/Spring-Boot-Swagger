package com.krishna.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krishna.entity.User;
import com.krishna.exception.ErrorMessage;
import com.krishna.exception.UserNotFoundException;
import com.krishna.model.UserModel;
import com.krishna.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	UUID uuid;

	@Operation(summary = "Get All Users With Page, Size, Sort By specific field")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully Fetched All Users", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) })})
	@GetMapping("/limit/users")
	public List<UserModel> getUsers(@RequestParam(value = "page", defaultValue = "0") int pageNo,
			@RequestParam(value = "size", defaultValue = "25") int size,
			@RequestParam(value = "sort", defaultValue = "ASC", required = false) String dir,
			@RequestParam(value = "field", defaultValue = "id", required = false) String field) {
		Direction direction = dir.toString().equalsIgnoreCase("DESC") ? Direction.DESC : Direction.ASC;

		Page<User> page = userService.getAllUsers(pageNo, size, direction, field);
		if (page.hasContent()) {
			List<User> allUsers = page.getContent();
			return allUsers.stream().map(user -> convertDaoToDto(user)).collect(Collectors.toList());
		}
		throw new UserNotFoundException("Page : " + pageNo + " has No Contenet", new Date());
	}

	@Operation(summary = "Get All Users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully Fetched All Users", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) }) })
	@GetMapping("/users")
	public List<UserModel> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		return allUsers.stream().map(user -> convertDaoToDto(user)).collect(Collectors.toList());
	}

	@Operation(summary = "Get User By Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully Fetched User", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found with given Id", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping("/users/{id}")
	public ResponseEntity<UserModel> getUserById(@PathVariable Integer id) {
		Optional<User> OptionalUser = userService.getUserById(id);
		if (OptionalUser.isPresent()) {
			return new ResponseEntity<UserModel>(convertDaoToDto(OptionalUser.get()), HttpStatus.OK);
		}
		throw new UserNotFoundException("User Not Found with given id :" + id, new Date());
	}

	@Operation(summary = "Create User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Created User Successfully ", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) }),
			@ApiResponse(responseCode = "404", description = "User is Not Created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping("/users")
	public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel userModel) {
		Optional<User> OptionalUser = userService.getUserByEmail(userModel.getEmail());
		if (OptionalUser.isPresent()) {
			throw new UserNotFoundException("Email Id is alredy Registered", new Date());
		}

		User user = userService.createUser(convertDtoToDao(userModel));
		return new ResponseEntity<UserModel>(convertDaoToDto(user), HttpStatus.CREATED);
	}

	@Operation(summary = "Update User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated User Successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) }),
			@ApiResponse(responseCode = "404", description = "User is not updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PutMapping("/users")
	public ResponseEntity<UserModel> updateUser(@Valid @RequestBody UserModel userModel) {

		Optional<User> OptionalUser = userService.getUserById(userModel.getId());
		if (OptionalUser.isPresent()) {
			User user = userService.updateUser(convertDtoToDao(userModel));
			return new ResponseEntity<UserModel>(convertDaoToDto(user), HttpStatus.OK);
		}
		throw new UserNotFoundException("User Not Found", new Date());
	}

	@Operation(summary = "Delete User By Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Deleted User Successfully", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserModel.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found with given Id", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping("/users/{id}")
	public ResponseEntity<UserModel> deleteUserById(@PathVariable Integer id) {
		Optional<User> OptionalUser = userService.getUserById(id);
		if (OptionalUser.isPresent()) {
			userService.deleteUserById(id);
			return new ResponseEntity<UserModel>(convertDaoToDto(OptionalUser.get()), HttpStatus.OK);
		}
		throw new UserNotFoundException("User Not Found with given id :" + id, new Date());
	}

	private User convertDtoToDao(UserModel userModel) {
		return modelMapper.map(userModel, User.class);
	}

	private UserModel convertDaoToDto(User user) {
		UserModel userModel = modelMapper.map(user, UserModel.class);
		userModel.setToken(uuid.toString());
		return userModel;
	}

}
