package com.corda.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import javax.annotation.PostConstruct;

@Configuration
public class CordaServiceConfiguration {
    private static final String CORDA_SERVICE_HOSTNAME = "config.rpc.username";
    private static final String CORDA_SERVICE_PORT = "config.rpc.password";

    @Value("${corda.service.host}")
    private String host;

    @Value("${corda.service.port}")
    private String port;

    private static String nodeServiceAddress;

    @PostConstruct
    public void init() {
        System.out.println("Initializing ");
        try {
            // custom initialization if required in future
            nodeServiceAddress = "http://"+host +":"+port+"/";

        } catch (Exception e) {
            System.out.println("Unable to initialize ");
            System.out.println(e.getMessage());
        }

    }

    public  String getNodeServiceAddress() {
        return nodeServiceAddress;
    }

    @Override
    public String toString() {
        return "CordaServiceConfiguration{" +
            "host='" + host + '\'' +
            ", port='" + port + '\'' +
            '}';
    }
}
