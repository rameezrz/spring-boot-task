package com.rzeee.mailSender.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rzeee.mailSender.dto.MailRequest;
import com.rzeee.mailSender.services.MailService;
import com.rzeee.mailSender.services.RateLimitService;
import com.rzeee.mailSender.utils.OtpUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
public class MailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpUtils otpUtils;

    @MockBean
    private MailService mailService;

    @MockBean
    private RateLimitService rateLimitService;

    private MailRequest mailRequest;

    @BeforeEach
    public void setUp() {
        mailRequest = new MailRequest();
        mailRequest.setToEmail("johnambala21@gmail.com");
    }

    @Test
    public void testSendEmailRateLimitExceeded() throws Exception {
        when(rateLimitService.tryConsume("johnambala21@gmail.com")).thenReturn(false);

        mockMvc.perform(post("/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mailRequest)))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    public void testSendEmailSuccess() throws Exception {
        when(rateLimitService.tryConsume("johnambala21@gmail.com")).thenReturn(true);
        when(otpUtils.generateOtp()).thenReturn("123456");

        mockMvc.perform(post("/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mailRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSendEmailBadRequest() throws Exception {
        mailRequest.setToEmail(null);

        mockMvc.perform(post("/mail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mailRequest)))
                .andExpect(status().isBadRequest());
    }
}