package com.watchman.metaserver.user.model;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(@NotBlank String newPassword) {}
