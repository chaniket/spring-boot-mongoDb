package com.cb.util;

import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.cb.document.LoginEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	public String generateToken(LoginEntity loginEntity) {

		// byte[] keyBytes =Base64Utils.decode(secretKey);
		String keyBytes = new String(Base64Utils.encode(loginEntity.getUsername().getBytes()));
		return Jwts.builder().setId("AniketPatil").setSubject(keyBytes).claim("upn", loginEntity.getId())
				.setIssuer("chavan").setAudience("XYZ_Ltd").setId(UUID.randomUUID().toString()).setIssuedAt(new Date())// date
																														// of
																														// generated
																														// token
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))// valid till this time
				.signWith(SignatureAlgorithm.HS512, getSignInKey()).compact();
	}

	private byte[] getSignInKey() {
		return Base64.getEncoder().encode(secretKey.getBytes());
	}

	public String getTokenSuject(String token) {
		Claims cleam = Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();

		return cleam.getSubject();
	}

	public boolean isTokenExpired(String token) {
		boolean flag = false;
		try {
			Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token);
			flag = true;
		} catch (IllegalArgumentException | UnsupportedJwtException | MalformedJwtException e) {
			e.printStackTrace();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
		}
		return flag;
	}
//
//	public String extractUsername(String token) {
//		return extractClaim(token, Claims::getSubject);
//	}
//
//	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//		final Claims claims = extractAllClaims(token);
//		return claimsResolver.apply(claims);
//	}
//
//	public String generateToken(UserDetails userDetails) {
//		return generateToken(new HashMap<>(), userDetails);
//	}
//
//	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//		return buildToken(extraClaims, userDetails, jwtExpiration);
//	}
//
//	public long getExpirationTime() {
//		return jwtExpiration;
//	}
//
//	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
//		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + expiration))
//				.signWith(SignatureAlgorithm.HS256, getSignInKey()).compact();
//	}
//
//	public boolean isTokenValid(String token, UserDetails userDetails) {
//		final String username = extractUsername(token);
//		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//	}
//
//	public boolean isTokenExpired(String token) {
//		return extractExpiration(token).before(new Date());
//	}
//
//	private Date extractExpiration(String token) {
//		return extractClaim(token, Claims::getExpiration);
//	}
//
//	private Claims extractAllClaims(String token) {
//		return Jwts.parser().setSigningKey(getSignInKey())
//				// .build()
//				.parseClaimsJws(token).getBody();
//	}
//
////    private Key getSignInKey() {
////        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
////        return Keys.hmacShaKeyFor(keyBytes);
////    }

}
