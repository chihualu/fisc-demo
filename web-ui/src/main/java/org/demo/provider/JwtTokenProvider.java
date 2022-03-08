package org.demo.provider;


import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@Log4j2
@Configurable
public class JwtTokenProvider {

    @Value("${security.jwt.token.expire-length:432000000}")
    private final long validityInMilliseconds = 1 * 24 * 60 * 60 * 1000L; // 1Day
    @Value("${security.jwt.token.secret-key}")
    private String sk;

    @PostConstruct
    protected void init() {
        sk = Base64.getEncoder().encodeToString(sk.getBytes());
    }

    public String createToken(String username, String roleId) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roleId);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder().setClaims(claims).setIssuedAt(now)
                .setExpiration(validity).signWith(SignatureAlgorithm.HS256, sk)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetails(String token) {
        Claims claims = Jwts.parser().setSigningKey(sk).parseClaimsJws(token).getBody();
        if(claims.getExpiration().before(new Date())){
            throw new BadCredentialsException("token is expired");
        }
        UserDetails userDetails = new User(claims.getSubject(), "default", AuthorityUtils.createAuthorityList((String) claims.get("auth")));
        return userDetails;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}