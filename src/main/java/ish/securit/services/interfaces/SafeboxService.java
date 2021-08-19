package ish.securit.services.interfaces;

import ish.securit.models.Safebox;
import ish.securit.models.SafeboxContents;
import ish.securit.models.SafeboxCreateResponse;
import org.springframework.stereotype.Service;

@Service
public interface SafeboxService {
    SafeboxContents getSafeboxContents(String id);

    SafeboxCreateResponse createSafebox(Safebox safebox);

    void updateSafeboxContents(String id, SafeboxContents contents);
}
