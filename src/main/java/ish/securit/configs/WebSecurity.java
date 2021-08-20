package ish.securit.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ish.securit.dtos.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Using a Custom exception handlers to handle auth exceptions in a message format specified in the swagger spec
                .exceptionHandling(handler -> handler.authenticationEntryPoint((request, response, authenticationException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().println(
                            objectMapper.writeValueAsString(
                                    ErrorMessage.builder()
                                            .message("Specified Basic Auth does not match")
                                            .ex(authenticationException)
                                            .build()
                            )
                    );
                }))
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/safebox").permitAll()
                // Secure all URL's not in the antMatchers above
                .anyRequest().authenticated()

                // Use Http Basic as the Authentication method
                .and().httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("Secure")
                .password(passwordEncoder().encode("extremelySecurePassword"))
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
