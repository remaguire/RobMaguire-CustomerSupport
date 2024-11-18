package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.UserPrincipal;

public interface AuthenticationService {
    UserPrincipal register(String username, String password);

    UserPrincipal authenticate(String username, String password);

    void saveUser(UserPrincipal principal, String newPassword);
}
