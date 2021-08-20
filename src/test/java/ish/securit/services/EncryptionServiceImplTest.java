package ish.securit.services;

import ish.securit.services.interfaces.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
class EncryptionServiceImplTest {

    @Autowired
    private EncryptionService encryptionService;


    private final String TEST_STRING = "This is test content";

    @Test
    void encrypt() {
        var result = this.encryptionService.encrypt(TEST_STRING);

        assertNotEquals(TEST_STRING, result);
    }

    @Test
    void decrypt() {
        var encrypted = this.encryptionService.encrypt(TEST_STRING);
        var result = this.encryptionService.decrypt(encrypted);

        assertEquals(TEST_STRING, result);
    }
}