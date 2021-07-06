package javapro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
@EnableConfigurationProperties
public class MailConfig {

    private String username;

    public String getUsername() {
        return username;
    }

}