package com.springboot_template.springboot_template.integrationtests.springboottemplate.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@TestConfiguration
public class WireMockTestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    WireMockServer wireMockServer() {
        return new WireMockServer(options().dynamicPort());
    }
}
