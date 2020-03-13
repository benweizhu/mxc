package com.mxc.blog.springsecurityauthbasic.configuration.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class CustomAuthenticationConverter implements AuthenticationConverter {

  @Override
  public Authentication convert(HttpServletRequest request) {
    String username = request.getHeader("username");
    if (!isEmpty(username)) {
      return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
    }
    return null;
  }

}
