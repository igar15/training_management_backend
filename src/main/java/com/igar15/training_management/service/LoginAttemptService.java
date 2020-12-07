package com.igar15.training_management.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(1000).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    public void addUserToLoginAttemptCache(String userEmail) {
        int attempts = 0;
        try {
            attempts = loginAttemptCache.get(userEmail) + 1;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        loginAttemptCache.put(userEmail, attempts);
    }

    public void evictUserFromLoginAttemptCache(String userEmail) {
        loginAttemptCache.invalidate(userEmail);
    }

    public boolean hasExceededMaxAttempts(String userEmail) {
        try {
            return loginAttemptCache.get(userEmail) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
