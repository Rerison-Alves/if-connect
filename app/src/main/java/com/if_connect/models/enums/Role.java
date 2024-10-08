package com.if_connect.models.enums;

import static com.if_connect.models.enums.Permission.ADMIN_CREATE;
import static com.if_connect.models.enums.Permission.ADMIN_DELETE;
import static com.if_connect.models.enums.Permission.ADMIN_READ;
import static com.if_connect.models.enums.Permission.ADMIN_UPDATE;
import static com.if_connect.models.enums.Permission.MANAGER_CREATE;
import static com.if_connect.models.enums.Permission.MANAGER_DELETE;
import static com.if_connect.models.enums.Permission.MANAGER_READ;
import static com.if_connect.models.enums.Permission.MANAGER_UPDATE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum Role {

    USER(Collections.emptySet()),
    ADMIN(
            new HashSet<>(Arrays.asList(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            ))),
    MANAGER(
            new HashSet<>(Arrays.asList(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )))

    ;

    private final Set<Permission> permissions;


    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}