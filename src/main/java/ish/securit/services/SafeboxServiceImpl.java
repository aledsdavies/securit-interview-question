package ish.securit.services;


import ish.securit.models.Safebox;
import ish.securit.models.SafeboxContents;
import ish.securit.models.SafeboxContentsUpdateRequest;
import ish.securit.models.SafeboxCreateResponse;
import ish.securit.repositories.SafeboxRepository;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeboxServiceImpl implements SafeboxService {

    private final SafeboxRepository safeboxRepository;

    @Override
    public SafeboxContents getSafeboxContents(String id) {
        var contents = safeboxRepository.getSafeboxContents(id);

        if (contents.isEmpty()) {
            // This is the 404 error case
            throw new RuntimeException("Requested safebox does not exist");
        }

        return SafeboxContents.builder()
                .items(contents.get())
                .build();
    }

    @Override
    @Transactional
    public SafeboxCreateResponse createSafebox(Safebox safebox) {
        var created = safeboxRepository.save(safebox);

        return SafeboxCreateResponse.builder()
                .id(created.getId())
                .build();
    }

    @Override
    @Transactional
    public void updateSafeboxContents(String id, SafeboxContents contents) {
        var updateContents = contents.getItems().stream().map(
                content -> SafeboxContentsUpdateRequest.builder().safeboxId(id).contents(content).build()
        ).collect(Collectors.toList());

    }
}
