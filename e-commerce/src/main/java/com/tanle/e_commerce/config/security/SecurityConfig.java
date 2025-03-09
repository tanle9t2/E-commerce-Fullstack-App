package com.tanle.e_commerce.config.security;

import com.tanle.e_commerce.exception.CustomAccessDeniedExceptionHandler;
//import com.tanle.e_commerce.filter.ContentCachingFilter;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;
//    @Autowired
//    private ContentCachingFilter contentCachingFilter;
    @Autowired
    private MyAuthorizationManager authorizationManager;
    @Autowired
    private CustomAccessDeniedExceptionHandler accessDeniedException;


    private final String[] URL_PERMIT_ALL = new String[]{

            "/api/v1/user/register"
            ,"/test"
            ,"/api/v1/email/sendOtp"
            ,"/api/v1/email/verify-otp"
            ,"/api/v1/products"
            ,"/api/v1/search"
            ,"/api/v1/tenant/tenant-infor/{tenantId}"
            ,"/api/v1/search-hint"
            ,"/api/v1/filter-search"
            , "/api/v1/user/login"
            , "/api/v1/tenant/login"
            , "/api/v1/user/refreshToken"
            , "/api/v1/payment"
            , "/api/v1/vn-pay-callback"
            , "/api/v1/product_list"
            , "api/v1/tenant/{tenantId}"
            ,"api/v1/product/{productId}"
            ,"api/v1/product/sku/{skuId}"
            ,"api/v1/comment/product/{productId}"
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
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL_PERMIT_ALL).permitAll()
//                        .requestMatchers(URL_ADMIN).hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/user/").authenticated()
////                        .requestMatchers("/api/v1/cart/cartItem").access(authorizationManager)
//                        .requestMatchers("/api/v1/order/status"
//                                , "/api/v1/order/cancelOrder").hasAnyAuthority("ADMIN", "SELLER")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/order").hasAnyAuthority("ADMIN", "SELLER")
//                        .requestMatchers("/api/v1/user/registerToken/**"
//                                , "/api/v1/user/registerToken").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e.accessDeniedHandler(accessDeniedException))
                .logout(l -> {
                    l.logoutUrl("/user/logout");
                    l.logoutSuccessUrl("/login?logout");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(e -> e.authenticationEntryPoint())
        ;

        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type","X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
