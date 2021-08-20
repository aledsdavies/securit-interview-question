package ish.securit.services.interfaces;

import ish.securit.errors.WeakPasswordException;
import org.springframework.stereotype.Service;

@Service
public interface PasswordStrengthService {
    void checkPasswordIsStrong(String password) throws WeakPasswordException;
}
