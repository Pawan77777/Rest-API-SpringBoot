package com.springrest.springrest.services;

import java.util.List;

import com.springrest.springrest.entities.User;


public interface UserService {
	
	public List<User> getUserProfile();
	
	public User getUserProfileById(long userId);
    
	public User addUserProfile(User user);
	
	public User updateUserProfile(User course);
	
	public void deleteUserProfile(long userId);

}
