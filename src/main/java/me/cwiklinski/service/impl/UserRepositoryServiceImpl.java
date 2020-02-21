package me.cwiklinski.service.impl;

import me.cwiklinski.model.User;
import me.cwiklinski.repo.UserRepository;
import me.cwiklinski.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Autowired
    public UserRepositoryServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Long getUserIdByUsername(String name) {
        User user = userRepository.findByUsername(name);
        return user.getId();
    }

    @Override
    public boolean isUserIsUsed(String username) {
        return (userRepository.findByUsername(username) != null);
    }

    @Override
    public void saveUser(String username, String password) {
        userRepository.save(new User(username, passwordEncoder.encode(password), "user"));
    }
}
