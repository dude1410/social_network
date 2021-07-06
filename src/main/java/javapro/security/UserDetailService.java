package javapro.security;

import javapro.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailService")
class UserDetailService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public UserDetailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var person = personRepository.findByEmail(email);
        if (person == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return SecurityUser.fromUser(person);
    }
}
