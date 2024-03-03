package com.watchman.metaserver.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(@NotBlank String newPassword) {}
