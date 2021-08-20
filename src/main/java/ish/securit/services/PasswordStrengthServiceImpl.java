package ish.securit.services;

import ish.securit.errors.WeakPasswordException;
import ish.securit.services.interfaces.PasswordStrengthService;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.springframework.stereotype.Service;

@Service
public class PasswordStrengthServiceImpl implements PasswordStrengthService {

    @Override
    public void checkPasswordIsStrong(String password) throws WeakPasswordException {
        var passwordValidator = new PasswordValidator(
                new LengthRule(7, Integer.MAX_VALUE), new CharacterRule(EnglishCharacterData.UpperCase, 1)
        );

        var result = passwordValidator.validate(new PasswordData(password));

        if (!result.isValid()) {
            throw new WeakPasswordException("The Password you are trying to use is not strong enough");
        }
    }
}
