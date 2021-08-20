package ish.securit.repositories;


import ish.securit.dtos.Safebox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SafeboxRepository extends JpaRepository<Safebox, String> {
    boolean existsByName(String name);
    Optional<Safebox> getByName(String name);
}