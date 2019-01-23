package com.nc.airport.backend.config;

import com.nc.airport.backend.security.JwtAuthenticationEntryPoint;
import com.nc.airport.backend.security.JwtAuthorizationTokenFilter;
import com.nc.airport.backend.security.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthorizationTokenFilter authenticationTokenFilter;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(jwtUserDetailsService)
                .passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // we don't need CSRF because our token is invulnerable
                .cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // Un-secure H2 Database
                .antMatchers("/h2-console/**/**").permitAll()

                .antMatchers("/auth/**").permitAll()
                //Users access
                .antMatchers(HttpMethod.GET, "/users").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/users").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/users/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/users/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ROLE_ADMIN")
                //Airlines access
                .antMatchers(HttpMethod.GET, "/airlines").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/airlines").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/airlines/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/airlines/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/airlines/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/airlines/**").hasAuthority("ROLE_ADMIN")
                //Airplanes access
                .antMatchers(HttpMethod.GET, "/airplanes").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/airplanes").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/airplanes/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/airplanes/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/airplanes/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/airplanes/**").hasAuthority("ROLE_ADMIN")
                //Countries access
                .antMatchers(HttpMethod.GET, "/countries").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/countries").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/countries/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/countries/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/countries/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/countries/**").hasAuthority("ROLE_ADMIN")
                //User's flight access
                .antMatchers(HttpMethod.GET, "/user-flights").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/user-flights").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/user-flights/**").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/user-flights/**").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/user-flights/**").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/user-flights/**").hasAnyAuthority("ROLE_USER")
                //Flight's access
                .antMatchers(HttpMethod.GET, "/flights").hasAnyAuthority("ROLE_CONTROLLER")
                .antMatchers(HttpMethod.POST, "/flights").hasAnyAuthority("ROLE_CONTROLLER")
                .antMatchers(HttpMethod.POST, "/flights/**").hasAnyAuthority("ROLE_CONTROLLER")
                .antMatchers(HttpMethod.GET, "/flights/**").hasAnyAuthority("ROLE_CONTROLLER")
                .antMatchers(HttpMethod.PUT, "/flights/**").hasAnyAuthority("ROLE_CONTROLLER")
                .antMatchers(HttpMethod.DELETE, "/flights/**").hasAnyAuthority("ROLE_CONTROLLER")
                //Passengers access
                .antMatchers(HttpMethod.GET, "/passengers").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/passengers").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/passengers/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/passengers/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.PUT, "/passengers/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/passengers/**").hasAuthority("ROLE_USER")
                //Tickets access
                .antMatchers(HttpMethod.GET, "/tickets").hasAuthority("ROLE_CASHIER")
                .antMatchers(HttpMethod.POST, "/tickets").hasAuthority("ROLE_CASHIER")
                .antMatchers(HttpMethod.POST, "/tickets/**").hasAuthority("ROLE_CASHIER")
                .antMatchers(HttpMethod.GET, "/tickets/**").hasAuthority("ROLE_CASHIER")
                .antMatchers(HttpMethod.PUT, "/tickets/**").hasAuthority("ROLE_CASHIER")
                .antMatchers(HttpMethod.DELETE, "/tickets/**").hasAuthority("ROLE_CASHIER")
                .anyRequest().authenticated();

        http
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        authenticationPath
                )

                // allow anonymous resource requests
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/"
                ).and()

                .ignoring()
                .antMatchers(HttpMethod.POST, "/register")

                // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
