package com.example.AppUsers.Service;

import com.example.AppUsers.entity.User;

public interface UserService {

	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
		
}
