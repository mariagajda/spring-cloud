package pl.training.payments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration {

    private static final String REALM_CLAIM = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

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
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, List<String>> realm = jwt.getClaim(REALM_CLAIM);
            return mapAuthorities(realm.get(ROLES));
        });
        jwtConfigurer.jwtAuthenticationConverter(converter);
    }

    // Requires Client scopes -> Client scope details -> Mapper details -> Add to userinfo enabled (Keycloak Admin console)
    @SuppressWarnings("unchecked")
    private void configure(UserInfoEndpointConfig config) {
        config.userAuthoritiesMapper(authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;
            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();
                var realmAccess = userInfo.getClaimAsMap(REALM_CLAIM);
                var roles = (Collection<String>) realmAccess.get(ROLES);
                mappedAuthorities.addAll(mapAuthorities(roles));
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                var userAttributes = oauth2UserAuthority.getAttributes();
                var realmAccess = (Map<String, Object>) userAttributes.get(REALM_CLAIM);
                var roles = (Collection<String>) realmAccess.get(ROLES);
                mappedAuthorities.addAll(mapAuthorities(roles));
            }
            return mappedAuthorities;
        });
    }

    public Collection<GrantedAuthority> mapAuthorities(Collection<String> roles) {
        return roles.stream()
                .map(role -> ROLE_PREFIX + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
