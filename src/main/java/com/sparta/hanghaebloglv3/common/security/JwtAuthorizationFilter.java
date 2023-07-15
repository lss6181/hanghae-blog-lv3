package com.sparta.hanghaebloglv3.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.RestApiException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private final MessageSource messageSource;

	public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, MessageSource messageSource) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.messageSource = messageSource;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

		String tokenValue = jwtUtil.getJwtFromHeader(req);

		/**
		 * 토큰값이 헤더에 존재하지 않으면 '토큰을 찾을 수 없습니다'라는 메세지 반환 목적
		 * !problem! : security config 에서 검증없이 접근 허용했던 api주소인 "/api/user/**" 에서까지 발동됨..
 		 */
//		if (!StringUtils.hasText(tokenValue)) {
//			log.error("Token Error");
//			// !problem! -> 콘솔쪽에 메세지가 띄워지고 Http body에는 예외처리 내용 반환이 안됨
////			throw new IllegalArgumentException(
////					messageSource.getMessage(
////							"not.found.token",
////							null,
////							"Not Found Token",
////							Locale.getDefault()
////					)
////			);
//			ObjectMapper objectMapper = new ObjectMapper();
//			res.setContentType("application/json; charset=UTF-8");
//			res.getWriter().print(objectMapper.writeValueAsString(new ApiResult(ProjConst.NOT_FOUND_TOKEN, HttpStatus.NOT_FOUND.value())));
//			return;
//		}

		if (StringUtils.hasText(tokenValue)) {

			if (!jwtUtil.validateToken(tokenValue)) {
				log.error("Token Error");
//				// !problem! -> 콘솔쪽에 메세지가 띄워지고 Http body에는 예외처리 내용 반환이 안됨
//				throw new IllegalArgumentException(
//						messageSource.getMessage(
//								"invalid.token",
//								null,
//								"Invalid Token",
//								Locale.getDefault()
//						)
//				);
				ObjectMapper objectMapper = new ObjectMapper();
				res.setContentType("application/json; charset=UTF-8");
				res.getWriter().print(objectMapper.writeValueAsString(new ApiResult(ProjConst.INVALID_TOKEN, HttpStatus.BAD_REQUEST.value())));
				return;
			}

			Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

			try {
				setAuthentication(info.getSubject());
			} catch (Exception e) {
				log.error(e.getMessage());
				return;
			}
		}

		filterChain.doFilter(req, res);
	}

	// 인증 처리
	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<RestApiException> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
		RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(
				// HTTP body
				restApiException,
				// HTTP status code
				HttpStatus.BAD_REQUEST
		);
	}
}
