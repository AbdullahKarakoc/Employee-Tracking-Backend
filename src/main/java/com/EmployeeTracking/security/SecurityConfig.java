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
                                .requestMatchers(HttpMethod.GET,"/employees/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/employees").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/employees/me").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/employees/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/employees/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/comments/{id}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/comments").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/comments").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/comments/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/comments/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/performances/{id}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/performances").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/performances").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/performances/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/performances/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/projects/{id}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/projects/{projectId}/roles").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/projects/{projectId}/roles/{roleId}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/projects/").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/projects/").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/projects/{projectId}/roles").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/projects/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/projects/{projectId}/roles/{roleId}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/projects/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/projects/{projectId}/roles/{roleId}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/tasks/{id}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/tasks").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/tasks").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/tasks/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/tasks/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/teams/{id}").hasAnyAuthority("USER","ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.GET,"/teams").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.POST,"/teams").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.PUT,"/teams/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
                                .requestMatchers(HttpMethod.DELETE,"/teams/{id}").hasAnyAuthority("ADMIN","SUPER_USER")
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