package org.example.robmaguirecustomersupport.site;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.robmaguirecustomersupport.entities.UserPrincipal;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.springframework.security.crypto.bcrypt.BCrypt.*;

@Service
public class DefaultAuthenticationService implements AuthenticationService {
    private static final SecureRandom RNG;
    private static final int HASHING_ROUNDS = 10;

    static {
        try {
            RNG = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    public UserPrincipal register(String username, String password) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                userRepository.getByUsername(username) != null)
            return null;

        final var principal = new UserPrincipal();
        final var salt = gensalt(HASHING_ROUNDS, RNG);
        principal.setUsername(username);
        principal.setHashedPassword(hashpw(password, salt).getBytes());
        userRepository.add(principal);

        return authenticate(username, password);
    }

    @Override
    @Transactional
    public UserPrincipal authenticate(String username, String password) {
        final var principal = userRepository.getByUsername(username);
        if (principal == null)
            return null;

        if (!checkpw(password, new String(principal.getHashedPassword(), StandardCharsets.UTF_8)))
            return null;

        return principal;
    }

    @Override
    @Transactional
    public void saveUser(UserPrincipal principal, String newPassword) {
        if (newPassword != null && !newPassword.isBlank()) {
            final var salt = gensalt(HASHING_ROUNDS, RNG);
            principal.setHashedPassword(hashpw(newPassword, salt).getBytes());
        }

        if (principal.getUserId() < 0) userRepository.add(principal);
        else userRepository.update(principal);
    }
}