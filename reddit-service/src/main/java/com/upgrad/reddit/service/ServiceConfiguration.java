package com.upgrad.reddit.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Enabling the component scan and entity scan of classes in the below mentioned
 *           "com.upgrad.reddit.service"
 *           "com.upgrad.reddit.service.entity"
 * packages respectively.
 */
@Configuration
@ComponentScan("com.upgrad.reddit.service")
@EntityScan("com.upgrad.reddit.service.entity")
public class ServiceConfiguration {
}
