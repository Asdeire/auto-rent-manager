package com.asdeire.persistence.entities.filter;

import java.time.LocalDate;

public record UserFilterDto(String username, String email, double balance) {
}
