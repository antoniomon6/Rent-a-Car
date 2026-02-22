package com.rentacar.config;

import com.rentacar.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // Recursos estáticos y Documentación - PÚBLICOS
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        
                        // API REST Login - PÚBLICA
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Página de Login MVC - PÚBLICA
                        .requestMatchers("/login").permitAll()

                        // --- REGLAS DE NEGOCIO ESPECÍFICAS (API REST) ---
                        .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**").hasRole("ADMIN")
                        
                        // --- REGLAS DE NEGOCIO ESPECÍFICAS (MVC) ---
                        .requestMatchers("/vehiculos/eliminar/**", "/clientes/eliminar/**").hasRole("ADMIN")

                        // Cualquier usuario autenticado (USER o ADMIN) puede hacer todo lo demás
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .defaultSuccessUrl("/alquileres", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        // Configuración específica para la API REST (JSON)
                        .defaultAuthenticationEntryPointFor((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                        }, request -> request.getRequestURI().startsWith("/api/"))
                        
                        .defaultAccessDeniedHandlerFor((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Insufficient permissions\"}");
                        }, request -> request.getRequestURI().startsWith("/api/"))
                        
                        // NOTA: No definimos un entry point global manual. 
                        // Spring Security usará automáticamente LoginUrlAuthenticationEntryPoint para MVC (lo que arregla el bucle).
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
