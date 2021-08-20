package ish.securit.repositories;


import ish.securit.dtos.Safebox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafeboxRepository extends JpaRepository<Safebox, String> {
    boolean existsByName(String name);
}