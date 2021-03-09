package kskowronski.security;

import kskowronski.data.service.global.GlobalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import kskowronski.data.entity.inap.User;
import kskowronski.data.service.inap.UserRepo;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService
{

    @Autowired
    private UserRepo userRepo;

    //@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //BigDecimal.valueOf(Long.parseLong(username))
        Optional<User> user = userRepo.findByUsername(username);
        if (user.get() == null) {
            throw new UsernameNotFoundException("Could not find user with this username and pass");
        }

        return new MyUserDetails(user.get());
    }

}
