package com.springrest.springrest.services;

import com.springrest.springrest.dao.UserDao;
import com.springrest.springrest.entities.User;
import com.springrest.springrest.exception.UserNotFoundException;
import com.springrest.springrest.exception.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserDao userDao;
    
    public UserServiceImpl() {
    }

    @Override
    public List<User> getUserProfile() {
        logger.info("Fetching all user profiles.");
        return userDao.findAll();
    }

    @Override
    public User getUserProfileById(long userId) {
        logger.info("Fetching user profile with ID: " + userId);
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    @Override
    @Transactional
    public User addUserProfile(User user) {
        logger.info("Attempting to add a new user profile.");
        
        Optional<User> existingUser = userDao.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateUserException("User with email " + user.getEmail() + " already exists");
        }

        if (userDao.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new DuplicateUserException("User with phone number " + user.getPhoneNumber() + " already exists");
        }


        try {
            userDao.save(user);
            return user;
        } catch (DataIntegrityViolationException ex) {
            logger.severe("Error while saving user: " + ex.getMessage());
            throw new RuntimeException("Error while saving user. Please check data integrity.");
        }
    }

    @Override
    @Transactional
    public void deleteUserProfile(long userId) {
        logger.info("Attempting to delete user with ID: " + userId);
        
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            userDao.delete(user.get());
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    @Override
    @Transactional
    public User updateUserProfile(User user) {
        logger.info("Attempting to update user profile.");
        
        // Check if the user exists by ID
        Optional<User> existingUser = userDao.findById(user.getId());
        if (!existingUser.isPresent()) {
            throw new UserNotFoundException("User with ID " + user.getId() + " not found.");
        }

        // Check if another user with the same email exists, and it's not the same as the current user
        Optional<User> userWithEmail = userDao.findByEmail(user.getEmail());
        if (userWithEmail.isPresent() && !userWithEmail.get().getEmail().equals(existingUser.get().getEmail())) {
            throw new DuplicateUserException("User with email " + user.getEmail() + " already exists.");
        }

        // Check if another user with the same phone number exists, and it's not the same as the current user
        if (userDao.existsByPhoneNumber(user.getPhoneNumber()) && !user.getPhoneNumber().equals(existingUser.get().getPhoneNumber())) {
            throw new DuplicateUserException("User with phone number " + user.getPhoneNumber() + " already exists.");
        }

        // Update user details
        User userToUpdate = existingUser.get();
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setAddress(user.getAddress());

        try {
            userDao.save(userToUpdate);
            return userToUpdate;
        } catch (DataIntegrityViolationException ex) {
            logger.severe("Error while updating user: " + ex.getMessage());
            throw new RuntimeException("Error while updating user. Please check data integrity.");
        }
    }
}