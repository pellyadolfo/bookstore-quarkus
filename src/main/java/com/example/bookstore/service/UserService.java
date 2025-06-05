package com.example.bookstore.service;

import org.modelmapper.ModelMapper;

import com.example.bookstore.dto.UserDTO;
import com.example.bookstore.exception.ResourceAlreadyExistsException;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {
	
	@Inject
    UserRepository userRepository;
	
	@Inject
    ModelMapper modelMapper;
        
    public void registerUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Email is already in use");
        }
        
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(BcryptUtil.bcryptHash(userDTO.getPassword()));
        userRepository.persist(user);
    }
}
