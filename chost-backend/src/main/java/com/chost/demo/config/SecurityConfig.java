package com.chost.demo.config;

import com.chost.demo.security.UserDetailsService;
import com.chost.demo.security.endpoints.RestAuthEndpoint;
import com.chost.demo.security.jwt.TokenAuthenticationFilter;
import com.chost.demo.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.chost.demo.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.chost.demo.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.chost.demo.security.oauth2.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2UserService auth2UserService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
  @Autowired
    private OAuth2AuthenticationSuccessHandler successHandler;
  @Autowired
    private OAuth2AuthenticationFailureHandler failureHandler;
  @Autowired
  private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(6);
    }
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http. cors().and().

                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and()
                .csrf().disable()
                .formLogin()
                .disable()
                .httpBasic()
                .disable();
        http.exceptionHandling()
                .authenticationEntryPoint(new RestAuthEndpoint());
                http.authorizeRequests().
                antMatchers("/auth/**","/oauth2/**").
                permitAll().antMatchers("/",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js","/img/**")
                .permitAll()
                .anyRequest().
                authenticated().
                and().
                oauth2Login().
                    authorizationEndpoint().
                            baseUri("/oauth2/authorize").
                            authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                        .and().
                    redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and().
                     userInfoEndpoint()
                        .userService(auth2UserService)
                        .and().
                     successHandler(successHandler)
                    .failureHandler(failureHandler);
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
