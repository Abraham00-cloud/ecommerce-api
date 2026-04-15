package com.AAA.e_commerce.config;

import com.AAA.e_commerce.exception.EntryPointExceptionHandler;
import com.AAA.e_commerce.user.service.CustomUserDetailService;
import com.AAA.e_commerce.user.service.JwtAuthFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtAuthFilter jwtAuthFilter;
    private final EntryPointExceptionHandler entryPointExceptionHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(
                                                "/api/v1/users/register", "/api/v1/users/login")
                                        .permitAll()
                                        .requestMatchers(
                                                "/v3/api-docs/**",
                                                "/swagger-ui/**",
                                                "/swagger-ui.html")
                                        .permitAll()
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/v1/products/**",
                                                "/api/v1/categories/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.PUT,
                                                "/api/v1/products/**",
                                                "/api/v1/categories/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.PATCH,
                                                "/api/v1/products/**",
                                                "/api/v1/categories/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.DELETE,
                                                "/api/v1/products/**",
                                                "/api/v1/categories/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.POST, "/api/v1/products/*/images/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.DELETE, "/api/v1/products/*/images/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/v1/products/**",
                                                "/api/v1/categories/**")
                                        .permitAll()
                                        .requestMatchers("/api/v1/users/update/**")
                                        .authenticated()
                                        .requestMatchers(
                                                "/api/v1/cart/**",
                                                "/api/v1/cartItem/**",
                                                "/api/v1/order/**")
                                        .authenticated()
                                        .anyRequest()
                                        .authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint(entryPointExceptionHandler))
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("JavaBearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "JavaBearerAuth",
                                        new SecurityScheme()
                                                .name("JavaBearerAuth")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
