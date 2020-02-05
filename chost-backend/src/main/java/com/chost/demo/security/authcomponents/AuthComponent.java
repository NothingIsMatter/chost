package com.chost.demo.security.authcomponents;

import com.chost.demo.model.entity.File;
import com.chost.demo.model.wrappers.UserWrapper;

public interface AuthComponent {
    boolean isOwnerOf(String file, UserWrapper wrapper);
}
