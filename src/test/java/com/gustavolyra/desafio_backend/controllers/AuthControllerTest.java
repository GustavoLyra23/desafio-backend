package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.controllers.handlers.CustomExceptionHandler;
import com.gustavolyra.desafio_backend.models.dto.user.UserLoginDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserResponseDto;
import com.gustavolyra.desafio_backend.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    void testRegister_Success() {
        // Arrange
        UserRequestDto userRequestDto = new UserRequestDto("John Doe", "john.doe@example.com", "pass123", "123.456.789-09");
        UserResponseDto userResponseDto = new UserResponseDto(1L, "John Doe", "john.doe@example.com", Set.of());
        when(authService.register(userRequestDto)).thenReturn(userResponseDto);

        // Act
        ResponseEntity<UserResponseDto> response = authController.register(userRequestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/v1/auth/register/" + userResponseDto.getId()), response.getHeaders().getLocation());
        assertEquals(userResponseDto, response.getBody());
        verify(authService, times(1)).register(userRequestDto);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto("john.doe@example.com", "pass123");
        String token = "sampleToken";
        when(authService.login(userLoginDto)).thenReturn(token);

        // Act
        ResponseEntity<Map<String, String>> response = authController.login(userLoginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("token", token), response.getBody());
        verify(authService, times(1)).login(userLoginDto);
    }

    @Test
    void testRegister_Failure_InvalidInput() throws Exception {
        // Act and Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(APPLICATION_JSON)
                        .content("{ \"name\": \"\", \"email\": \"invalid email\", \"password\": \"pw\", \"cpf\": \"123\" }"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.path").value("/v1/auth/register"))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'fullName')].message").exists())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'email')].message").exists())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'password')].message").exists())
                .andExpect(jsonPath("$.fieldErrors[?(@.field == 'cpf')].message").exists());
        verify(authService, times(0)).register(any(UserRequestDto.class));
    }


    @Test
    void testLogin_Failure_InvalidCredentials() {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto("wrong@example.com", "wrongpass");
        when(authService.login(userLoginDto)).thenThrow(new IllegalArgumentException("Invalid credentials"));

        // Act and Assert
        try {
            authController.login(userLoginDto);
        } catch (Exception e) {
            assertEquals("Invalid credentials", e.getMessage());
        }
        verify(authService, times(1)).login(userLoginDto);
    }
}
