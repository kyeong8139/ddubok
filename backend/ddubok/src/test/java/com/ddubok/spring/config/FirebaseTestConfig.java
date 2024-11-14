package com.ddubok.spring.config;

import static org.mockito.Mockito.mock;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles("test")
public class FirebaseTestConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            return mock(FirebaseApp.class);
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return mock(FirebaseMessaging.class);
    }
}
