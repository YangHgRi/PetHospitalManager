package com.phm.util;

import com.phm.security.model.MyUser;
import org.springframework.security.core.context.SecurityContextHolder;

public interface UserUtil {
    static Object getUser() {
        var people = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (people instanceof MyUser myUser) {
            return myUser.role();
        } else return people;
    }
}
