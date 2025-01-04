package com.tanle.e_commerce.config.security;

import com.tanle.e_commerce.exception.CustomAccessDeniedExceptionHandler;
import com.tanle.e_commerce.filter.ContentCachingFilter;
import com.tanle.e_commerce.filter.JwtFilter;
import com.tanle.e_commerce.service.authorization.MyAuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private ContentCachingFilter contentCachingFilter;
    @Autowired
    private MyAuthorizationManager authorizationManager;
    @Autowired
    private CustomAccessDeniedExceptionHandler accessDeniedException;

    private final String[] URL_ADMIN = new String[]{
            "/api/v1/cart/{cartId:[0-9]+}",
            "/api/v1/order/{orderId:[0-9]+}"
    };
    private final String[] URL_PERMIT_ALL = new String[]{
            "/api/v1/user/register"
            , "/api/v1/user/login"
            , "/api/v1/tenant/login"
            , "/api/v1/user/logout"
            , "/api/v1/user/refreshToken"
            , "/api/v1/payment"
            , "/api/v1/product_list"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService);

        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL_PERMIT_ALL).permitAll()
                        .requestMatchers(URL_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/user/{userId}"
                                , "/api/v1/tenant/**"
                                , "/api/v1/cart/**"
                                , "/api/v1/order/status"
                                , "/api/v1/order/cancelOrder"
                                , "/api/v1/tenant/register"
                        ).access(authorizationManager)
                        .requestMatchers("/api/v1/cart/cartItem").access(authorizationManager)
                        .requestMatchers("/api/v1/order/status"
                                , "/api/v1/order/cancelOrder").hasAnyAuthority("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/order").hasAnyAuthority("ADMIN", "SELLER")
                        .requestMatchers("/api/v1/user/registerToken/**"
                                , "/api/v1/user/registerToken").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedException))
                .logout(l -> {
                    l.logoutUrl("/user/logout");
                    l.logoutSuccessUrl("/login?logout");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
