package com.zorvyn.finance.backend.dto;

import com.zorvyn.finance.backend.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;


}
