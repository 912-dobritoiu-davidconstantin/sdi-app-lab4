package com.sdi.app.exception;

import com.sdi.app.model.ERole;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(ERole role) {
        super("Could not find role " + role);
    }

}
