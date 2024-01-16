package com.example.assessment.service;

import com.example.assessment.domain.UserInformation;
import com.example.assessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserInformation createUser(UserInformation userInformation) {
        return userRepository.save(userInformation);
    }
}
