package com.ddubok.spring.config;

import static org.mockito.Mockito.mock;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
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
