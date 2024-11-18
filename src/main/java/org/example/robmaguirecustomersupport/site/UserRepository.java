package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.UserPrincipal;

public interface UserRepository extends GenericRepository<Long, UserPrincipal> {
    UserPrincipal getByUsername(String username);
}
