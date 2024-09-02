package com.scm.smartContactManager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name}")
    String cloudName;
    @Value("${cloudinary.api_key}")
    String apiKey;
    @Value("${cloudinary.api_secret}")
    String apiSecret;

    @Bean
    public Cloudinary getCloudinary() {

        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret

                ));
    }

}
