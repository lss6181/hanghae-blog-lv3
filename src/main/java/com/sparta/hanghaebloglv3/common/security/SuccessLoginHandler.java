package com.sparta.hanghaebloglv3.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class SuccessLoginHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		ApiResult apiResult = new ApiResult("로그인 성공", HttpStatus.OK.value());

		// json 키밸류값만 나오게 설정
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResult = objectMapper.writeValueAsString(apiResult);

		// 한글 지원 설정
		response.setContentType("application/json; charset=UTF-8");

		response.getWriter().print(jsonResult);
	}
}
