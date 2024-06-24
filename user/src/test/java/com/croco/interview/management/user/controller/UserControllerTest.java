package com.croco.interview.management.user.controller;

import com.croco.interview.management.user.model.enums.Role;
import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.model.request.UpdateUserRequest;
import com.croco.interview.management.user.model.response.PageableResponse;
import com.croco.interview.management.user.model.response.UserResponse;
import com.croco.interview.management.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Mock
    private Principal principal;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "identifier",
                "firstName",
                "lastName",
                "password",
                "password"
        );

        mockMvc.perform(post("/api/public/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers() throws Exception {
        when(userService.getUsers(any(Optional.class), any(Optional.class)))
                .thenReturn(new PageableResponse<>(0, 20, 0L, List.of()));

        mockMvc.perform(get("/api/user/list")
                        .param("page", "0")
                        .param("size", "20"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPartialContent());

        verify(userService, times(1)).getUsers(any(Optional.class), any(Optional.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUser() throws Exception {
        UserResponse response = new UserResponse("identifier", "firstName", "lastName");
        when(userService.getUser(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/user/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier", is("identifier")))
                .andExpect(jsonPath("$.firstName", is("firstName")))
                .andExpect(jsonPath("$.lastName", is("lastName")));

        verify(userService, times(1)).getUser(anyLong());
    }

    @Test
    void getMe() throws Exception {
        UserResponse response = new UserResponse("identifier", "firstName", "lastName");
        when(principal.getName()).thenReturn("identifier");
        when(userService.getMe(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/user/me").principal(principal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier", is("identifier")))
                .andExpect(jsonPath("$.firstName", is("firstName")))
                .andExpect(jsonPath("$.lastName", is("lastName")));

        verify(userService, times(1)).getMe(anyString());
    }

    @Test
    void updateMe() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("identifier", "firstName", "lastName", null, null);
        when(principal.getName()).thenReturn("identifier");

        mockMvc.perform(put("/api/user/me")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateMe(anyString(), any(UpdateUserRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest("identifier", "firstName", "lastName", null, Role.ADMIN);

        mockMvc.perform(put("/api/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUser(anyLong(), any(UpdateUserRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(anyLong());
    }
}
