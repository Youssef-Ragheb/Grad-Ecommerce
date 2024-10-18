package com.grad.ecommerce_ai.config;

import com.grad.ecommerce_ai.config.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Set CORS configuration
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/login", "/api/signup/client", "/api/signup/create/admin", "/api/signup/company", "/api/fetch", "/swagger-ui/**", "/v3/api-docs/**", "/send-email").permitAll()  // Allow access to public APIs
                        .anyRequest().authenticated()  // Require authentication for all other requests
                ).securityContext(securityContext -> securityContext.requireExplicitSave(false))  // Avoid explicit save in sessions
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Use stateless sessions
                ).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // JWT filter before auth

        return http.build();
    }

    // CORS configuration bean
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");  // Allow localhost
        corsConfiguration.addAllowedOrigin("http://grad-ecommerce-production.up.railway.app");
        corsConfiguration.addAllowedOrigin("http://localhost:8080");// Allow production domain
        corsConfiguration.addAllowedHeader("*");  // Allow all headers
        corsConfiguration.addAllowedMethod("*");  // Allow all methods (GET, POST, etc.)
        corsConfiguration.setAllowCredentials(true);  // Allow credentials (like cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);  // Apply CORS config to all endpoints

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
