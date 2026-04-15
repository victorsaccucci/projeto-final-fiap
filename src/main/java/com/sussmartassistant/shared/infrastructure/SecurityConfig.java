package com.sussmartassistant.shared.infrastructure;

import com.sussmartassistant.seguranca.infrastructure.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança com JWT e RBAC.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Static resources
                .requestMatchers("/", "/*.html", "/css/**", "/js/**").permitAll()

                // Public endpoints
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                // Paciente: own prontuario only
                .requestMatchers("/api/v1/meu-prontuario", "/api/v1/meu-prontuario/**").hasRole("PACIENTE")

                // Métricas: gestor only
                .requestMatchers("/api/v1/metricas/**").hasRole("GESTOR")

                // Unidades e profissionais management: gestor
                .requestMatchers(HttpMethod.POST, "/api/v1/unidades-saude").hasRole("GESTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/profissionais").hasRole("GESTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/unidades-saude").hasAnyRole("PROFISSIONAL", "GESTOR")

                // Pacientes and prontuarios: profissional
                .requestMatchers("/api/v1/pacientes/**").hasRole("PROFISSIONAL")
                .requestMatchers("/api/v1/prontuarios/**").hasRole("PROFISSIONAL")

                // Assistente: profissional
                .requestMatchers("/api/v1/assistente/**").hasRole("PROFISSIONAL")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
