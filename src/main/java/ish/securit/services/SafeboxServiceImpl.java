package ish.securit.services;


import ish.securit.dtos.Safebox;
import ish.securit.dtos.SafeboxContent;
import ish.securit.repositories.SafeboxContentsRepository;
import ish.securit.repositories.SafeboxRepository;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeboxServiceImpl implements SafeboxService {
    private final SafeboxRepository safeboxRepository;
    private final SafeboxContentsRepository safeboxContentsRepository;

    @Override
    public Optional<SafeboxContent> getSafeboxContents(String id) {
        return safeboxContentsRepository.getContentsBySafeboxId(id);
    }

    @Override
    @Transactional
    public String createSafebox(Safebox safebox) {
        return safeboxRepository.save(safebox).getId();
    }

    @Override
    @Transactional
    public void updateSafeboxContents(List<SafeboxContent> contents) {
        this.safeboxContentsRepository.saveAll(contents);
    }
}
