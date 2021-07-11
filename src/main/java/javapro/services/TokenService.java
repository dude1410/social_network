package javapro.services;

import javapro.model.Person;
import javapro.model.Token;
import javapro.repository.TokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Random;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    public TokenService(TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean checkToken(String tokenIn) {
        Token token = tokenRepository.findByToken(tokenIn);
        if (token == null) {
            return false;
        }
        return new Date().getTime() - token.getDate().getTime() <= 600000;
    }

    public Token setNewPersonToken(Person person){
        Token token = tokenRepository.findByPerson(person);
        setToken(token, person);
        return tokenRepository.findByPerson(person);
    }

    public Token generatePersonToken(Person person){
        Token token = new Token();
        setToken(token, person);
        return tokenRepository.findByPerson(person);
    }

    public Token findToken(String strToken){
        return tokenRepository.findByToken(strToken);
    }

    private String getNewToken(String email){
        return passwordEncoder.encode(email + (new Date()).getTime());
    }

    private void setToken(Token token, Person person){
        token.setPerson(person);
        token.setToken(getNewToken(person.getEmail()));
        token.setDate(new Date());
        tokenRepository.save(token);
    }





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
}
