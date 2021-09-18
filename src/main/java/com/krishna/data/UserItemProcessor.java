package com.krishna.data;
import org.springframework.batch.item.ItemProcessor;

import com.krishna.entity.User;
import com.krishna.model.UserModel;

public class UserItemProcessor implements ItemProcessor<UserModel, User> {

	@Override
	public User process(final UserModel userModel) throws Exception {

		return User.builder().id(userModel.getId()).firstName(userModel.getFirstName()).
				lastName(userModel.getLastName()).email(userModel.getEmail()).
				role(userModel.getRole()).build();
	}

}