package javapro.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTemplate {

    @Bean(name = "RegisterTemplateMessage")
    public String getRegisterTemplateMessage() {
        return "Hello, to complete the registration follow to link " +
                "<a href=\"%s/registration/complete?token=%s\">Confirm registration</a>";
    }

    @Bean(name = "PassRecoveryTemplateMessage")
    public String getPassRecoveryMessage(){
        return "Hello, to recovery your password follow to link " +
                "<a href=\"%s/change-password?token=%s\">Password recovery</a>";
    }
}
