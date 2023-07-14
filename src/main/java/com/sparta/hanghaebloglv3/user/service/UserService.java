package com.sparta.hanghaebloglv3.user.service;

import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.user.dto.PasswordRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileResponseDto;
import com.sparta.hanghaebloglv3.user.dto.SignupRequestDto;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.entity.UserRoleEnum;
import com.sparta.hanghaebloglv3.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

/**
 * UserService.
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MessageSource messageSource;

	// ADMIN_TOKEN 관리자 암호
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	/**
	 * Signup.
	 */
	@Transactional
	public ResponseEntity<ApiResult> signup(SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());

		// 회원 중복 확인
		Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"in.used.username",
							null,
							"In Used Username",
							Locale.getDefault()
					)
			);
		}

		// 관리자 권한 확인
		UserRoleEnum role = UserRoleEnum.USER;
		if (signupRequestDto.isAdmin()) {
			if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
				throw new IllegalArgumentException(
						messageSource.getMessage(
								"wrong.admin.password",
								null,
								"Wrong Admin Password",
								Locale.getDefault()
						)
				);
			}
			role = UserRoleEnum.ADMIN;
		}

		// 회원 등록처리
		UserEntity userEntity = new UserEntity(username, password, role);
		userRepository.save(userEntity);

		return ResponseEntity.ok().body(new ApiResult(ProjConst.SIGN_UP_SUCCESS,HttpStatus.OK.value()));
	}

	// 프로필 조회
	@Transactional(readOnly = true)
	public ProfileResponseDto getProfile(UserEntity user) {

		return new ProfileResponseDto(user.getUsername(), user.getIntroduction()); // 해당 유저 정보 반환
	}

	// 프로필 수정 전 패스워드 재확인
	@Transactional
	public ApiResult checkPassword(PasswordRequestDto requestDto, UserEntity user) {

		//비밀번호 확인
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"wrong.password",
							null,
							"Wrong Password",
							Locale.getDefault()
					)
			);
		}

		return new ApiResult(ProjConst.PASSWORD_CHECK_OK, HttpStatus.OK.value()); // 수정 페이지로 넘어가기 전 비밀번호 확인
	}

	// 프로필 수정
	@Transactional
	public ApiResult updateProfile(ProfileRequestDto requestDto, UserEntity user) {

		user.update(requestDto); // 유저 정보 수정

		return new ApiResult(ProjConst.UPDATE_PROFILE_SUCCESS, HttpStatus.OK.value());
	}
}