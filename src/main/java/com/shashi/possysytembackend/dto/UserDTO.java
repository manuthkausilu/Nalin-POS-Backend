package com.shashi.possysytembackend.dto;

import com.shashi.possysytembackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Integer userId;
    private String userName;
    private String password;
    private String email;
    private User.Role role;
    private Boolean isActive;

}

