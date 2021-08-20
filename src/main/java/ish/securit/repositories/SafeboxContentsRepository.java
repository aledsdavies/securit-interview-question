package ish.securit.repositories;

import ish.securit.dtos.SafeboxContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafeboxContentsRepository extends JpaRepository<SafeboxContent, String> {
    List<SafeboxContent> getContentsBySafeboxId(String id);

    boolean existsBySafeboxIdAndContent(String safeboxId, String content);
}
