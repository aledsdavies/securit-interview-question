package ish.securit.repositories;


import ish.securit.dtos.Safebox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SafeboxRepository extends JpaRepository<Safebox, String> {
    @Query("SELECT count(e)>0 FROM Safebox s WHERE s.name = ?1")
    boolean existsByName(String name);
}