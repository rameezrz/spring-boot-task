package com.rzeee.mailSender.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private final Cache<String, Bucket> cache;

    public RateLimitService(){
        this.cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();
    }

    private Bucket createNewBucket(){
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(5)));
        return Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume(String identifier){
        Bucket bucket = cache.get(identifier, k-> createNewBucket());
        return bucket.tryConsume(1);
    }
}
