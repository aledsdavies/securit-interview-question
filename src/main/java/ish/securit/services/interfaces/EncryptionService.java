package ish.securit.services.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface EncryptionService {
    String encrypt(String contents);
    String decrypt(String contents);
}
