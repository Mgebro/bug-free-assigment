package com.croco.interview.management.user.service;

import com.croco.interview.core.security.kafka.UserRegistered;
import com.croco.interview.management.user.model.entity.User;
import com.croco.interview.management.user.model.enums.Role;
import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.model.request.UpdateUserRequest;
import com.croco.interview.management.user.model.response.PageableResponse;
import com.croco.interview.management.user.model.response.UserResponse;
import com.croco.interview.management.user.model.response.UsersResponse;
import com.croco.interview.management.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private KafkaTemplate<String, UserRegistered> kafkaTemplate;
    @InjectMocks
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> userCaptor;

    @NotNull
    private static User buildUser() {
        return User.builder()
                .withId(1L)
                .withIdentifier("identifier")
                .withFirstName("firstName")
                .withLastName("lastName")
                .withRole(Role.USER)
                .build();
    }

    @Test
    void createUser() {
        CreateUserRequest request = new CreateUserRequest(
                "identifier",
                "firstName",
                "lastName",
                "password",
                "password"
        );
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(kafkaTemplate.send(anyString(), any(UserRegistered.class))).thenReturn(CompletableFuture.completedFuture(null));

        userService.createUser(request);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("identifier", savedUser.getIdentifier());
        assertEquals("firstName", savedUser.getFirstName());
        assertEquals("lastName", savedUser.getLastName());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void getUsers() {
        Page<User> mockPage = mock(Page.class);
        Pageable mockPageable = PageRequest.of(0, 20);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(mockPage.getPageable()).thenReturn(mockPageable);
        when(mockPage.getTotalElements()).thenReturn(0L);
        when(mockPage.getContent()).thenReturn(List.of());

        PageableResponse<UsersResponse> response = userService.getUsers(Optional.of(0), Optional.of(20));

        assertNotNull(response);
        assertEquals(0, response.page());
        assertEquals(20, response.size());
        assertEquals(0, response.total());
        assertEquals(new ArrayList<>(), response.content());
        verify(userRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getUser() {
        when(userRepository.findUserById(anyLong())).thenReturn(buildUser());

        UserResponse response = userService.getUser(1L);

        assertEquals("identifier", response.identifier());
        assertEquals("firstName", response.firstName());
        assertEquals("lastName", response.lastName());
    }

    @Test
    void getMe() {
        when(userRepository.findUserByIdentifier(anyString())).thenReturn(buildUser());

        UserResponse response = userService.getMe("identifier");

        assertEquals("identifier", response.identifier());
        assertEquals("firstName", response.firstName());
        assertEquals("lastName", response.lastName());
    }

    @Test
    void updateUser() {
        when(userRepository.findUserById(anyLong())).thenReturn(buildUser());

        UpdateUserRequest request = new UpdateUserRequest(
                "identifier1",
                "firstName1",
                "lastName1",
                null,
                Role.ADMIN
        );

        userService.updateUser(1L, request);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertEquals("identifier1", updatedUser.getIdentifier());
        assertEquals("firstName1", updatedUser.getFirstName());
        assertEquals("lastName1", updatedUser.getLastName());
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    void updateMe() {
        when(userRepository.findUserByIdentifier(anyString())).thenReturn(buildUser());

        UpdateUserRequest request = new UpdateUserRequest(
                "newIdentifier",
                "newFirstName",
                "newLastName",
                null,
                null);

        userService.updateMe("identifier", request);

        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertEquals("newIdentifier", updatedUser.getIdentifier());
        assertEquals("newFirstName", updatedUser.getFirstName());
        assertEquals("newLastName", updatedUser.getLastName());
    }

    @Test
    void deleteUser() {
        when(userRepository.findUserById(anyLong())).thenReturn(buildUser());

        userService.deleteUser(1L);

        verify(userRepository).delete(userCaptor.capture());
        User deletedUser = userCaptor.getValue();

        assertEquals(1L, deletedUser.getId());
    }
}
