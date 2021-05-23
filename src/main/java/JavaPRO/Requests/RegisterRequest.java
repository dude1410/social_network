package JavaPRO.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest {

    @JsonProperty
    private String email;
    @JsonProperty
    private String passwd1;
    @JsonProperty
    private String passwd2;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String code;

    public RegisterRequest(String email, String passwd1, String passwd2, String firstName, String lastName, String code) {
        this.email = email;
        this.passwd1 = passwd1;
        this.passwd2 = passwd2;
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd1() {
        return passwd1;
    }

    public String getPasswd2() {
        return passwd2;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCode() {
        return code;
    }
}
