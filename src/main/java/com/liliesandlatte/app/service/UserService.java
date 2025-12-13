package com.liliesandlatte.app.service;

import com.liliesandlatte.app.model.User;
import com.liliesandlatte.app.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CUSTOMER);
        return userRepository.save(user);
    }

    public User updateUserProfile(User updatedUser) {
        if (updatedUser == null || updatedUser.getId() == null) {
            throw new IllegalArgumentException("Updated user must include an id");
        }
        User existing = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + updatedUser.getId()));

        // Only update allowed profile fields
        existing.setFullName(updatedUser.getFullName());
        existing.setPhoneNumber(updatedUser.getPhoneNumber());
        existing.setProfileImageUrl(updatedUser.getProfileImageUrl());
        existing.setVisaCardNumber(updatedUser.getVisaCardNumber());

        return userRepository.save(existing);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
    }
}
