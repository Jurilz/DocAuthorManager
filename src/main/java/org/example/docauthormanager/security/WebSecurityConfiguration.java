package org.example.docauthormanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfiguration {

    protected static final String ADMIN_ROLE = "ADMIN";
    protected static final String ADMIN_USERNAME = "admin";
    protected static final String ADMIN_PASSWORD = "admin";
    protected static final String DOCUMENTS_PATH_PREFIX = "/documents/**";
    protected static final String AUTHORS_PATH_PREFIX = "/authors/**";
    protected static final String SWAGGER_UI = "/swagger-ui/**";
    protected static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    protected static final String V3_API_DOCS = "/v3/api-docs/**";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(DOCUMENTS_PATH_PREFIX).hasAnyRole(ADMIN_ROLE)
                        .requestMatchers(AUTHORS_PATH_PREFIX).hasAnyRole(ADMIN_ROLE)
                        .requestMatchers(SWAGGER_UI, SWAGGER_UI_HTML, V3_API_DOCS).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername(ADMIN_USERNAME)
                .password(bCryptPasswordEncoder.encode(ADMIN_PASSWORD))
                .roles(WebSecurityConfiguration.ADMIN_ROLE)
                .build());
        return manager;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
