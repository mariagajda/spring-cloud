package pl.training.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration {

    private static final String ROLE_PREFIX = "ROLE_";

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthorityConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(config -> {
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(config -> config.userInfoEndpoint(this::configure))
                .oauth2ResourceServer(config -> config.jwt(this::jwtConfig))
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/actuator").hasRole("ADMIN")
                        .requestMatchers("/**").authenticated()
                )
                .build();
    }

    private void jwtConfig(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthorityConverter());
        jwtConfigurer.jwtAuthenticationConverter(converter);
    }

    private void configure(OAuth2LoginConfigurer.UserInfoEndpointConfig config) {
        config.userAuthoritiesMapper(authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;
            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();

                if (userInfo.hasClaim("realm_access")) {
                    var realmAccess = userInfo.getClaimAsMap("realm_access");
                    var roles = (Collection<String>) realmAccess.get("roles");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey("realm_access")) {
                    var realmAccess = (Map<String, Object>) userAttributes.get("realm_access");
                    var roles = (Collection<String>) realmAccess.get("roles");
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }

            return mappedAuthorities;
        });
    }


    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream()
                .map(role -> ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }


}
