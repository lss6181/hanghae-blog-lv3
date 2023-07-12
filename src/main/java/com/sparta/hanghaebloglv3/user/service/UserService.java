package com.sparta.hanghaebloglv3.user.service;

import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.user.dto.PasswordRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileResponseDto;
import com.sparta.hanghaebloglv3.user.dto.SignupRequestDto;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.entity.UserRoleEnum;
import com.sparta.hanghaebloglv3.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserService.
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// ADMIN_TOKEN 관리자 암호
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	/**
	 * Signup.
	 */
	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {
		String username = signupRequestDto.getUsername();
		String password = passwordEncoder.encode(signupRequestDto.getPassword());

		// 회원 중복 확인
		Optional<UserEntity> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.IN_USED_ID, null);
		}

		// 관리자 권한 확인
		UserRoleEnum role = UserRoleEnum.USER;
		if (signupRequestDto.isAdmin()) {
			if (!ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
				throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
			}
			role = UserRoleEnum.ADMIN;
		}

		// 회원 등록처리
		UserEntity userEntity = new UserEntity(username, password, role);
		userRepository.save(userEntity);
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
		if (!user.getPassword().equals(requestDto.getPassword())) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_PASSWORD, null);
		}
		return new ApiResult("프로필 수정으로 넘어가기", HttpStatus.OK.value()); // 수정 페이지로 넘어가기 전 비밀번호 확인
	}

    // 프로필 수정
  @Transactional
  public ApiResult updateProfile(ProfileRequestDto requestDto, UserEntity user) {

    user.update(requestDto); // 유저 정보 수정

    return new ApiResult("정보 수정 완료", HttpStatus.OK.value());
  }
}