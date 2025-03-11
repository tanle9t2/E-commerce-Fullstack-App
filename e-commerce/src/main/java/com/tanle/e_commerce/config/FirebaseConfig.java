package com.tanle.e_commerce.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Value("firebase.path")
    private String path;

    @Bean
    public FirebaseDatabase initializeFirebase() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(path);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ecommerce-website-2f133-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .build();
        FirebaseApp.initializeApp(options);
        return FirebaseDatabase.getInstance();
    }
}
