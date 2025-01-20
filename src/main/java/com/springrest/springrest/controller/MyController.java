package com.springrest.springrest.controller;

import java.net.http.HttpClient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springrest.springrest.entities.User;
import com.springrest.springrest.services.UserService;

import jakarta.validation.Valid;


@RestController
public class MyController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUserProfile() {
		List<User> users = this.userService.getUserProfile();
		return new ResponseEntity<>(users, HttpStatus.OK);
	    }
		
		@GetMapping("/users/{userId}")
		public ResponseEntity<User> getUserProfile(@PathVariable String userId) {
			try {
	            User user = this.userService.getUserProfileById(Long.parseLong(userId));
	            return new ResponseEntity<>(user, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }		
		}
		
		@PostMapping(path="/users",consumes="application/json")
		public ResponseEntity<User> addUserProfile(@RequestBody @Valid User user) {
			try {
	            User createdUser = this.userService.addUserProfile(user);
	            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
		}
		
		@PutMapping("/users")
		public ResponseEntity<User> updateUserProfile(@RequestBody @Valid User user) {
			try {
	            User updatedUser = this.userService.updateUserProfile(user);
	            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
		}
		
		@DeleteMapping("/users/{userId}")
		public ResponseEntity<HttpClient> deleteUserProfile(@PathVariable String userId){
			try {
				this.userService.deleteUserProfile(Long.parseLong(userId));
				return new ResponseEntity<>(HttpStatus.OK);
			}
			catch(Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
}
