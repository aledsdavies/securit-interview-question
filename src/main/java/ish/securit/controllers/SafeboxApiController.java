package ish.securit.controllers;

import ish.securit.dtos.SafeboxContent;
import ish.securit.errors.MalformedIdException;
import ish.securit.errors.NotFoundException;
import ish.securit.openapi.api.SafeboxApi;
import ish.securit.openapi.model.SafeboxContentsRequest;
import ish.securit.openapi.model.SafeboxContentsResponse;
import ish.securit.openapi.model.SafeboxCreateRequest;
import ish.securit.openapi.model.SafeboxCreateResponse;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SafeboxApiController implements SafeboxApi {
    private final SafeboxService safeboxService;

    @Override
    public ResponseEntity<SafeboxContentsResponse> safeboxIdItemsGet(String id) throws Exception {
        checkIdIsValid(id);

        if (!safeboxService.exists(id)) {
            // This is the 404 error case
            throw new NotFoundException("Requested safebox does not exist");
        }

        var contents = this.safeboxService.getSafeboxContents(id);

        return ResponseEntity.ok(this.convertToSafeboxContentsResponse(contents));
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, SafeboxContentsRequest safeboxContentsRequest) throws Exception {
        checkIdIsValid(id);

        return SafeboxApi.super.safeboxIdItemsPut(id, safeboxContentsRequest);
    }

    @Override
    public ResponseEntity<SafeboxCreateResponse> safeboxPost(SafeboxCreateRequest safeboxCreateRequest) throws Exception {
        return SafeboxApi.super.safeboxPost(safeboxCreateRequest);
    }

    private SafeboxContentsResponse convertToSafeboxContentsResponse(List<SafeboxContent> content) {
        return new SafeboxContentsResponse().items(
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
