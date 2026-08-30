package com.hirehub.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.hirehub.entity.Contact}
 */
public record ContactRequestDto(String email, String message, String name, String subject,
                                String userType) implements Serializable {
}