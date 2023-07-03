package com.sparta.hanghaebloglv3.common.jwt;

import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * JwtUtil.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

	// Header Authorization KEY 값
	public static final String AUTHORIZATION_HEADER = "Authorization";
	//Token 식별자,  토큰을 만들 때 앞에 붙어서 들어감
	private static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private static final long TOKEN_TIME = 60 * 60 * 1000L;

	@Value("${jwt.secret.key}") // Application.properties 에 넣어놓은 값을 가져올 수 있음
	private String secretKey;
	private Key key; // Token을 만들 때 넣어줄 Key 값
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private final UserRepository userRepository;

	/**
	 * PostConstruct.
	 */
	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey); // 디코드 하는 과정
		key = Keys.hmacShaKeyFor(bytes);
	}

	/**
	 * Resolve token.
	 */
	private String resolveToken(HttpServletRequest request) { // HttpServletRequset 안에는 우리가 가져와야 할 토큰이 헤더에 들어있음

		String resolvedToken = null;

		String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 파라미터로 가져올 값을 넣어주면 됨
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 코드가 있는지, BEARER로 시작하는지 확인
			resolvedToken = bearerToken.substring(7); // 앞에 7글자를 지워줌 BEARER가 6글자이고 한칸이 띄어져있기때문
		}
		return resolvedToken;
	}

	/**
	 * Create token.
	 */
	// 토큰 생성
	public String createToken(String username) {
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.setSubject(username) // 공간에 username을 넣음
						.setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰을 언제까지 유효하게 할 것인지 getTime으로 현재 시간을 가지고 오며 현재 시간으로부터 우리가 설정한 시간동안 토큰 유효
						.setIssuedAt(date) // 토큰이 언제 만들어졌는가
						.signWith(key, signatureAlgorithm) // 어떤 알고리즘을 사용하여 암호화 할 것인가
						.compact(); // String 형식의 JWT 토큰으로 반환 됨
	}

	/**
	 * Validate token.
	 */
	// 토큰 검증
	private boolean validateToken(String token) {

		// todo: OnlyOneReturn PMD 제거 방법 보여주기

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	/**
	 * Get user information from token.
	 */
	// 토큰에서 사용자 정보 가져오기
	private Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); // 마지막에 getBody를 통하여 그 안에 들어있는 정보를 가져옴
	}

	/**
	 * Check token.
	 */
	public UserEntity checkToken(HttpServletRequest request) {

		String token = this.resolveToken(request);
		Claims claims;

		UserEntity userEntity = null;

		if (token != null) {
			if (this.validateToken(token)) {
				// 토큰에서 사용자 정보 가져오기
				claims = this.getUserInfoFromToken(token);

			} else {
				throw new HanghaeBlogException(HanghaeBlogErrorCode.INVALID_TOKEN, null);
			}

			// 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
			userEntity = userRepository.findById(claims.getSubject()).orElseThrow(
					() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null)
			);
		}
		return userEntity;
	}
}