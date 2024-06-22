package com.croco.interview.management.user.security.service;

import com.croco.interview.management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {
        var user = userRepository.findUserByIdentifier(userIdentifier);
        if (user != null) {
            return User.builder()
                    .username(user.getIdentifier())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("Cannot login invalid username or password!");
        }
    }
}
