package com.cb.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.cb.service.UserDetailsServiceImpl;
import com.cb.util.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String BEARER_TOKEN = "Bearer ";

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		request.getMethod();
		String token = extractTokenFromHeader(request);
		if (Objects.nonNull(token) && jwtUtil.isTokenExpired(token)) {
			String subject = jwtUtil.getTokenSuject(token);
			subject = new String(Base64Utils.decode(subject.getBytes()));
			
			UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(subject);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());

			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
	}

	private String extractTokenFromHeader(HttpServletRequest request) {
		if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			return request.getHeader(HttpHeaders.AUTHORIZATION).substring(BEARER_TOKEN.length());
		}
		return "";
	}

}
