package co.pla.portfoliomanagement.identity.infrastructure.repository.jpa;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByUid(UUID uid);
    Page<User> findAll(Pageable pageable);
}
