package javapro.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppLogger {

    @Bean(name = "registerLogger")
    public Logger getRegisterLogger(){
        return LogManager.getLogger("registerLogger");
    }

    @Bean(name = "passRecoveryLogger")
    public Logger getPassRecoveryLogger(){
        return LogManager.getLogger("passRecoveryLogger");
    }

    @Bean(name = "authorizationLogger")
    public Logger getAuthorizationLogger(){
        return LogManager.getLogger("authorizationLogger");
    }

    @Bean(name = "mailSenderLogger")
    public Logger getMailSenderLogger(){
        return LogManager.getLogger("mailSenderLogger");
    }

    @Bean(name = "mailChangeLogger")
    public Logger getMailChangeLogger(){
        return LogManager.getLogger("mailChangeLogger");
    }

    @Bean(name = "friendsServiceLogger")
    public Logger getFriendsServiceLogger(){
        return LogManager.getLogger("friendsServiceLogger");
    }

    @Bean(name = "postCommentLogger")
    public Logger getPostCommentLogger(){
        return LogManager.getLogger("postCommentLogger");
    }

    @Bean(name = "postLogger")
    public Logger getPostLogger(){
        return LogManager.getLogger("postLogger");
    }

    @Bean(name = "searchLogger")
    public Logger getSearchLogger(){
        return LogManager.getLogger("searchLogger");
    }
}
