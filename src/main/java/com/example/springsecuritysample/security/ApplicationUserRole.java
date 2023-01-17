package com.example.springsecuritysample.security;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.example.springsecuritysample.security.ApplicationUserPermission.*;

@RequiredArgsConstructor
public enum ApplicationUserRole {

    STUDENT(Sets.newHashSet()),
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ)),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
