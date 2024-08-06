package com.EmployeeTracking.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/index.html"
                                )
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"/employees/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/employees/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/employees/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/employees/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/comment/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/comment/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/comment/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/comment/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/performance/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/performance/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/performance/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/performance/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/project_roles/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/project_roles/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/project_roles/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/project_roles/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/project/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/project/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/project/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/project/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/status/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/status/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/status/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/status/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/task/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/task/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/task/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/task/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/team/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/team/**").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/team/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/team/**").hasAnyAuthority("ADMIN","SUPER_USER")
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}