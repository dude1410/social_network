package javapro.services;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Random;

@Service
public class TokenService {

    //длина токена без учета части с временной меткой
    private static final int tokenPartLength = 20;

    public String getToken() throws InterruptedException {
        return getRandomString(tokenPartLength)
                .append(new Date().getTime())
                .toString();
    }

    private synchronized StringBuilder getRandomString(int length) throws InterruptedException {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++){
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        Thread.sleep(10);
        return sb;
    }

    public Boolean checkToken(String token){
        long tokenTime = Long.parseLong(token.substring(20));
        return (new Date().getTime() - tokenTime) <= 1200000;
    }
}
