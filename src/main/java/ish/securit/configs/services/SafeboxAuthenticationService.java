package ish.securit.configs.services;

import ish.securit.repositories.SafeboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SafeboxAuthenticationService implements UserDetailsService {

    private final SafeboxRepository safeboxRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var dbResult = this.safeboxRepository.getByName(username);

        if (dbResult.isEmpty()) {
            throw new UsernameNotFoundException("Safebox <" + username + "> does not exist");
        }

        var safebox = dbResult.get();

        return new User(safebox.getName(), safebox.getPassword(), new ArrayList<>());
    }
}
