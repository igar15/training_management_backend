package com.igar15.training_management.service;

import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginAttemptServiceTest {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void cleanCache() throws NoSuchFieldException, IllegalAccessException {
        getCache().invalidateAll();
    }

    @Test
    void addUserToLoginAttemptCache() throws ExecutionException, NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            loginAttemptService.addUserToLoginAttemptCache("user@test.com");
        }
        Integer userLoginAttemptsAmount = getCache().get("user@test.com");
        Assertions.assertEquals(3, userLoginAttemptsAmount);
    }

    @Test
    void evictUserFromLoginAttemptCache() throws ExecutionException, NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < 3; i++) {
            loginAttemptService.addUserToLoginAttemptCache("user@test.com");
        }
        loginAttemptService.evictUserFromLoginAttemptCache("user@test.com");
        Integer userLoginAttemptsAmount = getCache().get("user@test.com");
        Assertions.assertEquals(0, userLoginAttemptsAmount);
    }

    @Test
    void hasExceededMaxAttemptsWhenExceeded() {
        for (int i = 0; i < 5; i++) {
            loginAttemptService.addUserToLoginAttemptCache("user@test.com");
        }
        boolean isExceeded = loginAttemptService.hasExceededMaxAttempts("user@test.com");
        Assertions.assertTrue(isExceeded);
    }

    @Test
    void hasExceededMaxAttemptsWhenNotExceeded() {
        for (int i = 0; i < 4; i++) {
            loginAttemptService.addUserToLoginAttemptCache("user@test.com");
        }
        boolean isExceeded = loginAttemptService.hasExceededMaxAttempts("user@test.com");
        Assertions.assertFalse(isExceeded);
    }



    private LoadingCache<String, Integer> getCache() throws NoSuchFieldException, IllegalAccessException {
        Field loginAttemptCacheField = LoginAttemptService.class.getDeclaredField("loginAttemptCache");
        loginAttemptCacheField.setAccessible(true);
        LoadingCache<String, Integer> cache = (LoadingCache<String, Integer>) loginAttemptCacheField.get(loginAttemptService);
        return cache;
    }
}