package ish.securit.services.interfaces;

import ish.securit.dtos.Safebox;
import ish.securit.dtos.SafeboxContent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SafeboxService {
    Optional<SafeboxContent> getSafeboxContents(String id);

    String createSafebox(Safebox safebox);

    void updateSafeboxContents(List<SafeboxContent> contents);
}
