package com.mxc.blog.springsecurityauthbasic.configuration.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

  @Override
  public Authentication authenticate(Authentication authentication) {
    if ("iamapi".equals(authentication.getPrincipal())) {
      ArrayList<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_API"));
      return new UsernamePasswordAuthenticationToken("iamapi", null, authorities);
    }
    throw new BadCredentialsException("");
  }

}
