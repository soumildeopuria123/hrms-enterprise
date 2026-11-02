package com.example.hrms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
public class HrmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmsApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "hrms")
    public HrmsProperties hrmsProperties() {
        return new HrmsProperties();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}

@ConfigurationProperties(prefix = "hrms")
class HrmsProperties {

    private boolean demoMode = false;
    private String defaultTenantId = "00000000-0000-0000-0000-000000000001";
    private int leaveGraceDays = 0;

    public boolean isDemoMode() { return demoMode; }
    public void setDemoMode(boolean demoMode) { this.demoMode = demoMode; }

    public String getDefaultTenantId() { return defaultTenantId; }
    public void setDefaultTenantId(String defaultTenantId) { this.defaultTenantId = defaultTenantId; }

    public int getLeaveGraceDays() { return leaveGraceDays; }
    public void setLeaveGraceDays(int leaveGraceDays) { this.leaveGraceDays = leaveGraceDays; }
}
