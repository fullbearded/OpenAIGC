package com.opaigc.server.infrastructure.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.opaigc.server.infrastructure.common.Constants;
import com.opaigc.server.infrastructure.exception.AppException;
import com.opaigc.server.infrastructure.http.CommonResponseCode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.management.relation.Role;

/**
 * @author: Runner.dada
 * @date: 2020/12/13
 * @description: provide JWT token operation
 **/
@Component
public class JwtTokenProvider {

	@Value("${security.jwt.secret-key:secret-key}")
	private String secretKey;

	/**
	 * 24 hours, ms
	 **/
	@Value("${security.jwt.expire-in:86400000}")
	private long expireInMilliseconds = 86400000;

	public JwtTokenProvider() {

	}

	public String createToken(String username, String userCode, List<Role> roles) {

		Claims claims = Jwts.claims().setSubject(username);
		claims.put(Constants.USER_CODE_KEY, userCode);
		if (!Collections.isEmpty(roles)) {
			// TODO: need add authorities
			claims.put("authorities",
				roles.stream().map(s -> new SimpleGrantedAuthority(null)).filter(
					Objects::nonNull).collect(Collectors.toList()));
		}

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expireInMilliseconds))
			.signWith(SignatureAlgorithm.HS256, getSecretKey()).compact();
	}

	public String getSecretKey() {
		return Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(getSecretKey())
			.parseClaimsJws(token).getBody().getSubject();
	}

	public String getUserCode(String token) {
		Claims claims = Jwts.parser()
			.setSigningKey(secretKey.getBytes())
			.parseClaimsJws(token)
			.getBody();
		return (String) claims.get(Constants.USER_CODE_KEY);
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(Constants.AUTHORIZATION_HEADER);
		if (Objects.nonNull(bearerToken) && bearerToken
			.startsWith(Constants.AUTHORIZATION_BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new AppException(CommonResponseCode.ILLEGAL_REQUEST);
		}
	}

	public boolean invalidToken(String token) {
		return !validateToken(token);
	}
}
