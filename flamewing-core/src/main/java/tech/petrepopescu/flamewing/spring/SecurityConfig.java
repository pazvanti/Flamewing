package tech.petrepopescu.flamewing.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {
    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }
}
