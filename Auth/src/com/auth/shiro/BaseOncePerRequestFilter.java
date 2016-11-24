package com.auth.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;

public class BaseOncePerRequestFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws ServletException, IOException {
		// TODO Auto-generated method stub
		arg2.doFilter(arg0, arg1); 
	}

}
