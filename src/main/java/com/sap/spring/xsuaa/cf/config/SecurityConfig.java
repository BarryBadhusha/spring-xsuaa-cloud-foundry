package com.sap.spring.xsuaa.cf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;


@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter{

	@Autowired
	XsuaaServiceConfiguration xsuaaServiceConfiguration;
	
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()	
			.antMatchers("/local/*").permitAll()
			.antMatchers("/manager/**").hasAuthority("Manager")
			.antMatchers("/employee/**").hasAuthority("Employee")
			.anyRequest().authenticated()
			.and()
			.oauth2ResourceServer()
			.jwt()
			.jwtAuthenticationConverter(getJwtAuthenticationConverter());
	}
	
	Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter(){
		TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
		converter.setLocalScopeAsAuthorities(true);
		return converter;
	}
}
