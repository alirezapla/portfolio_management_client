package co.pla.portfoliomanagement.identity.domain.repository;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByUid(UUID uid);
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(UUID uid);
    Page<User> findAll(Pageable pageable);


}
