package ish.securit.services;


import ish.securit.dtos.Safebox;
import ish.securit.dtos.SafeboxContent;
import ish.securit.repositories.SafeboxContentsRepository;
import ish.securit.repositories.SafeboxRepository;
import ish.securit.services.interfaces.EncryptionService;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SafeboxServiceImpl implements SafeboxService {
    private final SafeboxRepository safeboxRepository;
    private final SafeboxContentsRepository safeboxContentsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    @Override
    public boolean exists(String id) {
        return safeboxRepository.existsById(id);
    }

    @Override
    public boolean checkIfNameExists(String name) {
        return safeboxRepository.existsByName(name);
    }

    @Override
    public List<SafeboxContent> getSafeboxContents(String id) {
        return safeboxContentsRepository.getContentsBySafeboxId(id).stream()
                .map(c -> c.toBuilder().content(encryptionService.decrypt(c.getContent())).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createSafebox(Safebox safebox) {
        // We Encode the password before storing it in the database for security.
        safebox.setPassword(passwordEncoder.encode(safebox.getPassword()));

        return safeboxRepository.save(safebox).getId();
    }

    @Override
    @Transactional
    public void updateSafeboxContents(List<SafeboxContent> contents) {
        contents.stream()
                .map(c -> c.toBuilder().content(encryptionService.encrypt(c.getContent())).build())
                .filter(c -> !this.safeboxContentsRepository.existsBySafeboxIdAndContent(c.getSafeboxId(), c.getContent()))
                .forEach(this.safeboxContentsRepository::save);
    }
}
