package com.albaraka.digital.dto.admin;

import com.albaraka.digital.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminUserSummary {

    private Long id;
    private String email;
    private String fullName;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;
}