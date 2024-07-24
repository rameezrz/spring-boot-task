package com.rzeee.mailSender.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class RateLimitServiceTest {

    private RateLimitService rateLimitService;

    @BeforeEach
    public void setUp() {
        rateLimitService = new RateLimitService();
    }

    @Test
    public void testTryConsume() {
        String identifier = "johnambala21@gmail.com";

        assertTrue(rateLimitService.tryConsume(identifier));
        assertTrue(rateLimitService.tryConsume(identifier));
        assertTrue(rateLimitService.tryConsume(identifier));

        assertFalse(rateLimitService.tryConsume(identifier));
    }

    @Test
    public void testBucketCreation() {
        String identifier = "johnambala21@gmail.com";
        assertTrue(rateLimitService.tryConsume(identifier));
    }
}
