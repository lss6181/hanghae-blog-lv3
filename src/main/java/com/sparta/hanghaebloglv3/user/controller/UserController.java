package com.sparta.hanghaebloglv3.user.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.user.dto.PasswordRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileResponseDto;
import com.sparta.hanghaebloglv3.user.dto.SignupRequestDto;
import com.sparta.hanghaebloglv3.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * UserController.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class
UserController {

	private final UserService userService;

	// 회원가입
	@PostMapping("/user/signup")
	public ResponseEntity<ApiResult> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
		// 회원가입 시 username과 password의 구성이 알맞지 않으면 에러메시지와 statusCode: 400을 Client에 반환하기
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + " : " + fieldError.getDefaultMessage());
				return ResponseEntity.badRequest().body(
						new ApiResult(fieldError.getField() + " : " + fieldError.getDefaultMessage(), HttpStatus.BAD_REQUEST.value())
				);
			}
		}

		return userService.signup(signupRequestDto);
	}

	// 프로필 조회
	@GetMapping("/profile")
	public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return userService.getProfile(userDetails.getUser());
	}

	// 프로필 수정 전 패스워드 재확인
	@PostMapping("/profile/valid-password")
	public ApiResult checkPassword(@RequestBody PasswordRequestDto passwordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return userService.checkPassword(passwordRequestDto, userDetails.getUser());
	}

	// 프로필 수정
	@PutMapping("/profile")
	public ApiResult updateProfile(@RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return userService.updateProfile(requestDto, userDetails.getUser());
	}
}