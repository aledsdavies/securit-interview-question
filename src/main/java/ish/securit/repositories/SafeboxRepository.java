package ish.securit.repositories;


import ish.securit.models.Safebox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface SafeboxRepository extends JpaRepository<Safebox, String> {

    @Query("SELECT c.contents FROM Contents c WHERE c.contents = ?1")
    Optional<ArrayList<String>> getSafeboxContents(String id);
}