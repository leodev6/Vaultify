package it.codeaveclionel.vaultify.repository;

import it.codeaveclionel.vaultify.entity.Credential;
import it.codeaveclionel.vaultify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    List<Credential> findByUser(User user);
    Optional<Credential> findByIdAndUser(Long id, User user);
    List<Credential> findByUserAndCategory(User user, String category);
    List<Credential> findByUserAndSiteNameContainingIgnoreCase(User user, String siteName);
}