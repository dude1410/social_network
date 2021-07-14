package javapro.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MainTestConfig {

    @Bean
    Logger logger(){
        return LoggerFactory.getLogger("TestsLogger");
    }
}
