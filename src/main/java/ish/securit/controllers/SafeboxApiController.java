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

    @Override
    public ResponseEntity<InlineResponse2001> safeboxIdItemsGet(String id) throws Exception {
        checkIdIsValid(id);

        if (!safeboxService.exists(id)) {
            // This is the 404 error case
            throw new NotFoundException("Requested safebox does not exist");
        }

        var contents = this.safeboxService.getSafeboxContents(id);

        return ResponseEntity.ok(this.mapSafeboxContentToInlineResponse2001(contents));
    }

    @Override
    @Valid
    public ResponseEntity<InlineResponse200> safeboxPost(InlineObject inlineObject) throws Exception {
        if (safeboxService.checkIfNameExists(inlineObject.getName())) {
            throw new SafeboxExistsException("Safebox already exists");
        }

        var id = this.safeboxService.createSafebox(Safebox.builder()
                .name(inlineObject.getName())
                .password(inlineObject.getPassword())
                .build());

        return ResponseEntity.ok(new InlineResponse200().id(id));
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, InlineObject1 inlineObject1) throws Exception {
        checkIdIsValid(id);
        return SafeboxApi.super.safeboxIdItemsPut(id, inlineObject1);
    }

    private InlineResponse2001 mapSafeboxContentToInlineResponse2001(List<SafeboxContent> content) {
        return new InlineResponse2001().items(
                content.stream().map(SafeboxContent::getContents).collect(Collectors.toList())
        );
    }

    private void checkIdIsValid(String id) throws MalformedIdException {
        var err = new MalformedIdException("ID is expected to be in the UUID format");

        if (id.isBlank()) {
            throw err;
        }

        try {
            UUID.fromString(id);
        } catch (Exception ex) {
            throw err;
        }
    }
}