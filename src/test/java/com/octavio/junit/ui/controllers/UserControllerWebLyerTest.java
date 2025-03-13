package com.octavio.junit.ui.controllers;

import com.octavio.junit.service.UsersService;
import com.octavio.junit.shared.UserDto;
import com.octavio.junit.ui.request.UserDetailsRequestModel;
import com.octavio.junit.ui.response.UserRest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

@WebMvcTest(controllers = UsersController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
//@MockBean({UsersServiceImpl.class})

public class UserControllerWebLyerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UsersService usersService;

    UserDetailsRequestModel userDetailsRequestModel;

    @BeforeEach
    void setup(){
        userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("jorge");
        userDetailsRequestModel.setLastName("Martinez");
        userDetailsRequestModel.setEmail("jorge@yomail.com");
        userDetailsRequestModel.setPassword("algo12234");
        userDetailsRequestModel.setRepeatPassword("algo12234");

    }

    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidUserDetailProvider_returnCreateUserDetails() throws Exception {

        // Arrange

        ObjectMapper objectMapper = new ObjectMapper();

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());


        Mockito.when(usersService.createUser(Mockito.any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetailsRequestModel));



        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getRequest().getContentAsString();

        JsonNode map = objectMapper.readTree(responseBodyAsString);

        // delete propertied not used for mapper UserRest
        ((ObjectNode) map).remove("password");
        ((ObjectNode) map).remove("repeatPassword");

        UserRest createdUser = objectMapper.readValue(map.toString(), UserRest.class);

        // Assert
        Assertions.assertEquals(userDetailsRequestModel.getFirstName(),
                createdUser.getFirstName(), "The returned user first name is most likely incorrect");

        Assertions.assertEquals(userDetailsRequestModel.getLastName(),
                createdUser.getLastName(), "The returned user last name is incorrect");

        Assertions.assertEquals(userDetailsRequestModel.getEmail(),
                createdUser.getEmail(), "The returned user email is incorrect");

        //Assertions.assertFalse(createdUser.getUserId().isEmpty(), "userId should not be empty");

    }

    @Test
    @DisplayName("First name is not empty")
    void testCreateUser_whenFirstNameNotProvider_returns400StateCode() throws Exception {

        // Arrange

        UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("");
        userDetailsRequestModel.setLastName("Martinez");
        userDetailsRequestModel.setEmail("jorge@yomail.com");
        userDetailsRequestModel.setPassword("algo12234");
        userDetailsRequestModel.setRepeatPassword("algo12234");

        ObjectMapper objectMapper = new ObjectMapper();

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());


        Mockito.when(usersService.createUser(Mockito.any(UserDto.class))).thenReturn(userDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetailsRequestModel));



        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status code returned");
    }
}
