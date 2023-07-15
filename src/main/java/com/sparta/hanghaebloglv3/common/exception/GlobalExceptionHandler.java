package com.sparta.hanghaebloglv3.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// IllegalArgumentException : 적합하지 않거나(illegal) 적절하지 못한(inappropriate) 인자(Argument)를 메소드에 넘겨주었을 때 발생합니다
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

	// 게시글, 댓글, 좋아요의 id값을 찾지 못했을 때 발생시킬 예외
	@ExceptionHandler({IdNotFoundException.class})
	public ResponseEntity<RestApiException> IdNotFoundExceptionHandler(IdNotFoundException ex) {
		RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(
				restApiException,
				HttpStatus.NOT_FOUND
		);
	}
}
