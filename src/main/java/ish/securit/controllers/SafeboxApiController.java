package ish.securit.controllers;

import ish.securit.dtos.Safebox;
import ish.securit.dtos.SafeboxContent;
import ish.securit.errors.MalformedIdException;
import ish.securit.errors.NotFoundException;
import ish.securit.errors.SafeboxExistsException;
import ish.securit.openapi.api.SafeboxApi;
import ish.securit.openapi.model.InlineObject;
import ish.securit.openapi.model.InlineObject1;
import ish.securit.openapi.model.InlineResponse200;
import ish.securit.openapi.model.InlineResponse2001;
import ish.securit.services.interfaces.PasswordStrengthService;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SafeboxApiController implements SafeboxApi {
    private final SafeboxService safeboxService;
    private final PasswordStrengthService passwordStrengthService;

    @Override
    public ResponseEntity<InlineResponse2001> safeboxIdItemsGet(String id) throws Exception {
        checkIdIsValid(id);
        checkSafeboxExists(id);

        var contents = this.safeboxService.getSafeboxContents(id);

        return ResponseEntity.ok(this.mapSafeboxContentToInlineResponse2001(contents));
    }

    @Override
    @Valid
    public ResponseEntity<InlineResponse200> safeboxPost(InlineObject inlineObject) throws Exception {
        if (safeboxService.checkIfNameExists(inlineObject.getName())) {
            throw new SafeboxExistsException("Safebox already exists");
        }

        /*
            Check the password strength to ensure it is strong enough
         */
        passwordStrengthService.checkPasswordIsStrong(inlineObject.getPassword());

        var id = this.safeboxService.createSafebox(Safebox.builder()
                .name(inlineObject.getName())
                .password(inlineObject.getPassword())
                .build());

        return ResponseEntity.ok(new InlineResponse200().id(id));
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, InlineObject1 inlineObject1) throws Exception {
        checkIdIsValid(id);
        checkSafeboxExists(id);

        this.safeboxService.updateSafeboxContents(mapInlineObject1ToListOfSafeboxContent(id, inlineObject1));

        return ResponseEntity.ok().build();
    }


    private InlineResponse2001 mapSafeboxContentToInlineResponse2001(List<SafeboxContent> content) {
        return new InlineResponse2001().items(
                content.stream().map(SafeboxContent::getContent).collect(Collectors.toList())
        );
    }

    private List<SafeboxContent> mapInlineObject1ToListOfSafeboxContent(String safeboxId, InlineObject1 inlineObject1) {
        return inlineObject1.getItems()
                .stream()
                .map(c -> SafeboxContent.builder()
                        .safeboxId(safeboxId)
                        .content(c)
                        .build())
                .collect(Collectors.toList());
    }

    private void checkIdIsValid(String id) throws MalformedIdException {
        var err = new MalformedIdException("ID is expected to be in the UUID format");

        if (id.isBlank()) {
            throw err;
        }

        try {
            // This will error on any invalid UUID format allowing us a convenient way to test whether the ID is formatted correctly
            UUID.fromString(id);
        } catch (Exception ex) {
            throw err;
        }
    }

    private void checkSafeboxExists(String id) throws NotFoundException {
        if (!safeboxService.exists(id)) {
            // This is the 404 error case
            throw new NotFoundException("Requested safebox does not exist");
        }
    }

}