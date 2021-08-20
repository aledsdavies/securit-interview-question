package ish.securit.services;

import ish.securit.errors.WeakPasswordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordStrengthServiceImplTest {
    private final PasswordStrengthServiceImpl passwordStrengthService = new PasswordStrengthServiceImpl();

    @Test
    void validPasswordIsValid() throws WeakPasswordException {
        var validPassword = "extremelyStrongPassword";

        this.passwordStrengthService.checkPasswordIsStrong(validPassword);
    }

    @Test
    void invalidWhenNoUppercase() {
        var validPassword = "extremelystrongpassword";

        assertThrows(
                WeakPasswordException.class,
                () -> {
                    this.passwordStrengthService.checkPasswordIsStrong(validPassword);
                });
    }

    @Test
    void invalidWhenPasswordTooShort() {
        var validPassword = "12345";

        assertThrows(
                WeakPasswordException.class,
                () -> {
                    this.passwordStrengthService.checkPasswordIsStrong(validPassword);
                });

    }
}