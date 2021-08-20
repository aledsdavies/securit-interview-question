package ish.securit.utils;

import ish.securit.configs.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Import(WebSecurityConfig.class)
public abstract class SecurityHelper {
    // Credentials to use for testing auth;
    protected final String NAME = "Secure";
    protected final String PASSWORD = "extremelySecurePassword";

    @Autowired
    protected PasswordEncoder passwordEncoder;


    @MockBean
    protected UserDetailsService userDetailsService;

    @BeforeEach
    protected void setupAuth() {
        when(this.userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(new User(NAME, this.passwordEncoder.encode(PASSWORD), new ArrayList<>()));
    }
}
