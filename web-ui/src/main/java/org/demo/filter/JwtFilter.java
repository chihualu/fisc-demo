package org.demo.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.http.entity.ContentType;
import org.demo.provider.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String url = request.getServletPath();
        try {
            if (url.startsWith("/api/")) {
                String token = jwtTokenProvider.resolveToken(request);
                if(token == null || "".equals(token)) {
                    throw new BadCredentialsException("no JWT token");
                }
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("", e);
            if(request.getContentType() == null) {
                response.sendRedirect("common/error-500");
                return;
            }
            String contentType = ContentType.create(request.getContentType()).toString().toLowerCase();
            if (contentType.indexOf("Json") > -1){
                response.sendRedirect("common/error-500");
            }else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getOutputStream().write("UNAUTHORIZED".getBytes(StandardCharsets.UTF_8));
            }
        }

    }
}
