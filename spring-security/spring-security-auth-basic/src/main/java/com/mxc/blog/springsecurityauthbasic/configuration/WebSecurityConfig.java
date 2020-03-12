package com.mxc.blog.springsecurityauthbasic.configuration;

import com.mxc.blog.springsecurityauthbasic.configuration.authentication.CustomAuthenticationConverter;
import com.mxc.blog.springsecurityauthbasic.configuration.authentication.CustomAuthenticationManager;
import com.mxc.blog.springsecurityauthbasic.configuration.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Configuration
  @Order(1)
  public static class WebPageSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/greeting")
          .authorizeRequests()
          .antMatchers("/greeting").hasAnyRole("USER", "ADMIN")
          .and()
          .formLogin();
    }

  }

  @Configuration
  @Order(2)
  public static class AdminPageSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/admin")
          .authorizeRequests()
          .antMatchers("/admin").hasRole("ADMIN")
          .and()
          .formLogin();
    }

  }

  @Configuration
  @Order(3)
  public static class APISecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    private CustomAuthenticationConverter customAuthenticationConverter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      AuthenticationFilter filter = new AuthenticationFilter(customAuthenticationManager, customAuthenticationConverter);
      filter.setSuccessHandler(new CustomAuthenticationSuccessHandler());
      filter.setRequestMatcher(new AntPathRequestMatcher("/api/**"));
      http.antMatcher("/api/**")
          .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
          .authorizeRequests()
          .antMatchers("/api/**").hasRole("API");
    }

  }

  @Configuration
  @Order(4)
  public static class FallbackSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
      UserDetails admin = User.builder().username("admin").password(encoder.encode("password")).roles("ADMIN").build();
      UserDetails user = User.builder().username("user").password(encoder.encode("password")).roles("USER").build();
      UserDetails api = User.builder().username("api").password(encoder.encode("password")).roles("API").build();
      auth.inMemoryAuthentication().withUser(admin).withUser(user).withUser(api);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.antMatcher("/**").authorizeRequests().anyRequest().permitAll().and().formLogin();
    }

  }
}
