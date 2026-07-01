package com.bond.librarymanager.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    OAuthSecurityService  oAuthSecurityService;

    public SecurityConfiguration(OAuthSecurityService oAuthSecurityService) {
        this.oAuthSecurityService =  oAuthSecurityService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**").permitAll()
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_USER",  "ROLE_ADMIN", "ROLE_LIBRARIAN", "ROLE_OWNER")
                        .requestMatchers("/librarian/**").hasAnyAuthority("ROLE_LIBRARIAN", "ROLE_OWNER")
                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_OWNER")
                        .requestMatchers("/owner/**").hasAnyAuthority("ROLE_OWNER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                                .userInfoEndpoint(userInfo-> userInfo
                                                .userService(oAuthSecurityService)
                                        )
                        )
                .logout(auth-> auth
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}
