package com.sivalabs.techbuzz.users.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.sivalabs.techbuzz.common.AbstractIntegrationTest;
import com.sivalabs.techbuzz.users.domain.dtos.CreateUserRequest;
import com.sivalabs.techbuzz.users.domain.dtos.UserDTO;
import com.sivalabs.techbuzz.users.domain.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailVerificationControllerTest extends AbstractIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    void shouldVerifyEmailSuccessfully() throws Exception {
        String email = RandomStringUtils.random(15, true, false) + "@gmail.com";
        CreateUserRequest request = new CreateUserRequest("name", email, "secret");
        UserDTO user = userService.createUser(request);
        mockMvc.perform(get("/verify-email")
                        .with(csrf())
                        .param("email", email)
                        .param("token", user.verificationToken()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", true))
                .andExpect(view().name("users/emailVerification"));
    }

    @Test
    void emailVerificationShouldFailWhenEmailAndTokenNotMatched() throws Exception {
        mockMvc.perform(get("/verify-email")
                        .with(csrf())
                        .param("email", "dummy@mail.com")
                        .param("token", "secretToken"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("success", false))
                .andExpect(view().name("users/emailVerification"));
    }
}
