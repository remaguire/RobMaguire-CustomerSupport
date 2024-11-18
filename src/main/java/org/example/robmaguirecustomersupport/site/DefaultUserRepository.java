package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.UserPrincipal;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultUserRepository
        extends GenericJpaRepository<Long, UserPrincipal>
        implements UserRepository {
    @Override
    public UserPrincipal getByUsername(String username) {
        final var result = this.entityManager.createQuery(
                "SELECT u FROM UserPrincipal u WHERE u.username = :username",
                UserPrincipal.class).setParameter("username", username).getResultList();
        return (result.isEmpty() ? null : result.get(0));
    }
}