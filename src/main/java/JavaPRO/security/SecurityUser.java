package JavaPRO.security;

import JavaPRO.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {

    private final String userName;
    private final String password;
    private final List<SimpleGrantedAuthority> authority;

    public SecurityUser(String userName, String password, List<SimpleGrantedAuthority> authority) {
        this.userName = userName;
        this.password = password;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails fromUser(Person person){
        return new org.springframework.security.core.userdetails.User(
                person.getEmail(),
                person.getPassword(),
                true,
                true,
                true,
                true,
                person.getRole().getAuthorites());
    }
}
