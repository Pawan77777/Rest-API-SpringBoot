package com.springrest.springrest.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springrest.springrest.dao.UserDao;
import com.springrest.springrest.entities.User;

@Service
public class UserServiceImpl implements UserService {
		
	@Autowired
	private UserDao userDao;
	
	public UserServiceImpl() {

	}

	@Override
	public List<User> getUserProfile() {
		return userDao.findAll();
	}
	
	public User getUserProfileById(long userId) {
		return userDao.getReferenceById(userId);		
	}

	public User addUserProfile(User user) {
		userDao.save(user);
		return user;
	}

	@Override
	public void deleteUserProfile(long userId) {
		User entity=userDao.getReferenceById(userId);
		userDao.delete(entity);
	}

	@Override
	public User updateUserProfile(User user) {
		userDao.save(user);
		return user; 
	}

	
}
