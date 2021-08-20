package ish.securit.repositories;

import ish.securit.dtos.SafeboxContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafeboxContentsRepository extends JpaRepository<SafeboxContent, String> {

    @Query("SELECT c FROM safebox_contents c WHERE c.safebox_id = ?1")
    List<SafeboxContent> getContentsBySafeboxId(String id);
}
