package com.rzeee.mailSender.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OtpUtilsTest {

    private final OtpUtils otpUtils = new OtpUtils();

    @Test
    public void testGenerateOtp() {
        String otp = otpUtils.generateOtp();
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));
    }
}
