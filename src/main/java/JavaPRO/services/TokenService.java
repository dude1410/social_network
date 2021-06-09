package JavaPRO.services;

import JavaPRO.Util.PassEncoder;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    public String getToken(){
        StringBuilder token = new StringBuilder();
        String strMass = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        for (int i = 0; i < 15; i++){
            token.append(strMass.charAt((int) (Math.random() * (strMass.length()))));
        }
        token.append(getTokenTime());
        return token.toString();
    }

    public boolean checkToken(String token)  {
        long timeLong = Long.parseLong(token.substring(15));
        return ((System.currentTimeMillis() - timeLong) <= (60000 * 10));
    }

    private synchronized Long getTokenTime() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Date().getTime();
    }


}
