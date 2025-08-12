package com.example.taskmanagement.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "azure.enabled", havingValue = "true", matchIfMissing = false)
public class AzureConfiguration {

    // Azure-specific configuration beans can be added here
    // This configuration is only active when azure.enabled=true

}
