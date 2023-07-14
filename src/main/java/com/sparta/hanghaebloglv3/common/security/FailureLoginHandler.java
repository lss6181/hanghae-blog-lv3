package com.sparta.hanghaebloglv3.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class FailureLoginHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		ApiResult apiResult = new ApiResult("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value());

		// json 키밸류값만 나오게 설정
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResult = objectMapper.writeValueAsString(apiResult);

		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(jsonResult);
	}
}
