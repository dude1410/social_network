package JavaPRO.Services;

import JavaPRO.Requests.RegisterRequest;
import JavaPRO.Responses.Register.RegisterResponse;
import JavaPRO.Responses.Register.RegisterResponseData;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    public RegisterResponse registerNewUser(RegisterRequest userInfo){
        if (checkUserInDB(userInfo.getEmail())){
            return new RegisterResponse("null", 1234567L, new RegisterResponseData("new user registered successfully"));
        }
        else {
            // TODO: 21.05.2021
            return new RegisterResponse("null", 1234567L, new RegisterResponseData("user already in DB"));
        }
    }

    // TODO: 20.05.2021
    private boolean checkUserInDB(String email){
        return true;
    }
}
