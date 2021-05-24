package com.rf.springsecurity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    private String login;
    private String password;
    private String confirmedPassword;
}
