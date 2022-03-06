package com.dbrz.trading.notification;

import javax.validation.constraints.NotBlank;

public record Notification(@NotBlank String title, @NotBlank String message) {
}
