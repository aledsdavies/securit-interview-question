package ish.securit.services.interfaces;

import ish.securit.dtos.Safebox;
import ish.securit.dtos.SafeboxContent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SafeboxService {
    boolean exists(String id);

    List<SafeboxContent> getSafeboxContents(String id);

    String createSafebox(Safebox safebox);

    void updateSafeboxContents(List<SafeboxContent> contents);
}
