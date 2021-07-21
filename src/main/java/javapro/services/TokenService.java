package javapro.services;

import javapro.model.Person;
import javapro.model.Token;
import javapro.repository.TokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

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
        if (token == null) {
            token = new Token();
        }
        setToken(token, person);
        return token;
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
}
