package com.gustavolyra.desafio_backend.controllers;

import com.gustavolyra.desafio_backend.factory.UserFactory;
import com.gustavolyra.desafio_backend.models.dto.user.UserRequestDto;
import com.gustavolyra.desafio_backend.models.dto.user.UserResponseDto;
import com.gustavolyra.desafio_backend.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {


    @Mock
    private AuthService authService;

    @InjectMocks
    AuthController authController;

    UserRequestDto userRequestDto;
    UserResponseDto userResponseDto;

    @Captor
    ArgumentCaptor<UserRequestDto> userCaptor;


    @BeforeEach
    public void beforeEach() {
        //ARRANGE
        userResponseDto = UserFactory.createUserResponseDto();
        userRequestDto = UserFactory.createUserRequestDto();
        Mockito.doReturn(userResponseDto).when(authService).register(userCaptor.capture());
    }

    @Test
    void registerShouldReturnUserResponseDtoWhenValidRequest() {
        //ACT
        var response = authController.register(userRequestDto);
        //ASSERT
        Assertions.assertEquals(response.getBody().getEmail(), userRequestDto.email());
        Assertions.assertEquals(response.getBody().getFullName(), userRequestDto.fullName());
        Assertions.assertEquals(1, response.getBody().getRoles().size());
        Assertions.assertEquals(userRequestDto, userCaptor.getValue());
    }


}
