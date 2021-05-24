package com.rf.springsecurity.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.rf.springsecurity.security.UserPermission.*;

public enum UserRole{
    USER(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions){this.permissions = permissions;}

    public Set<UserPermission> getPermissions(){
        return this.permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedPermissions(){
        Set<SimpleGrantedAuthority> permissions = this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public String getAuthority(){
        return this.name();
    }

}
