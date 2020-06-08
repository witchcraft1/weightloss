package com.rf.springsecurity.dto;

import com.rf.springsecurity.entity.MyUser;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UsersDTO {
    private List<MyUser> users;
}
