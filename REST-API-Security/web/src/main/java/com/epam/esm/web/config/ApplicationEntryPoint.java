package com.epam.esm.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EntityScan("com.epam.esm.model.entity")
@EnableJpaRepositories("com.epam.esm.model.repository")
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class ApplicationEntryPoint implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationEntryPoint.class, args);
    }

    @Bean
    public ResourceBundleMessageSource getResourceBundleMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("errorMessage");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean("bcryptPasswordEncoder")
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
