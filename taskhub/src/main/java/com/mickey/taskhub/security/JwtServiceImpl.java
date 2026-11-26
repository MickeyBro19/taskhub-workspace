package com.mickey.taskhub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
	private final SecretKey signingKey;
	private final Long expirationInMs;
	
	public JwtServiceImpl(
			@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.access-token-expiration-ms}") Long expirationIn) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
		this.expirationInMs = expirationIn;
	}
	
	@Override
	public String generateAccessToken(UserDetails user) {
		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + expirationInMs);
		
		return Jwts.builder()
				.subject(user.getUsername())
				.issuedAt(issuedAt)
				.expiration(expiration)
				.signWith(signingKey)
				.compact();
	}
	
	@Override
	public String extractSubject(String token) {
		return Jwts
				.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	@Override
	public boolean isTokenValid(String token, UserDetails user) {
		Claims claims = Jwts.parser()
				.verifyWith(signingKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		Date expiration = claims.getExpiration();
		
		return user
				.getUsername()
				.equals(claims.getSubject()) && expiration != null && expiration.after(new Date());
	}
}
