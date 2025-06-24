package br.senac.tads.oficina.resourceserverapp;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Desabilita CSRF, pois estamos usando JWT e não sessões
                .csrf(csrf -> csrf.disable())

                // Define a política de gerenciamento de sessão como STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as regras de autorização para os endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos arquivos estáticos e à raiz
                        .requestMatchers("/", "/index.html", "/estilos.css", "/app.js").permitAll()
                        // Exige a role 'ROLE_PEAO' para o endpoint /api/peao
                        .requestMatchers("/api/peao").hasRole("PEAO")
                        // Exige a role 'ROLE_GERENTE' para o endpoint /api/gerente
                        .requestMatchers("/api/gerente").hasRole("GERENTE")
                        // Exige a role 'ROLE_DIRETOR' para o endpoint /api/diretor
                        .requestMatchers("/api/diretor").hasRole("DIRETOR")
                        // Qualquer outra requisição deve ser autenticada
                        .anyRequest().authenticated())

                // Habilita a configuração de Resource Server com JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter())));

        return http.build();
    }

    // Configuração do CORS para permitir que o front-end (mesmo na mesma origem)
    // chame a API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite requisições da origem onde nosso front-end está
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
