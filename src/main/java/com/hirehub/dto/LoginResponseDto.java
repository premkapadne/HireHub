package com.hirehub.dto;

public record LoginResponseDto(String message, UserDto user, String jwtToken) {
}
