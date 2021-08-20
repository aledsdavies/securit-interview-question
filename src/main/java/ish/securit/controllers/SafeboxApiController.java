package ish.securit.controllers;

import ish.securit.openapi.api.SafeboxApi;
import ish.securit.openapi.model.SafeboxContentsRequest;
import ish.securit.openapi.model.SafeboxContentsResponse;
import ish.securit.openapi.model.SafeboxCreateRequest;
import ish.securit.openapi.model.SafeboxCreateResponse;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SafeboxApiController implements ish.securit.openapi.api.SafeboxApi {
    private final SafeboxService safeboxService;

    @Override
    public ResponseEntity<SafeboxContentsResponse> safeboxIdItemsGet(String id) {
        var contents = this.safeboxService.getSafeboxContents(id);

        if (contents.isEmpty()) {
            // This is the 404 error case
            throw new RuntimeException("Requested safebox does not exist");
        }

        return SafeboxApi.super.safeboxIdItemsGet(id);
    }

    @Override
    public ResponseEntity<Void> safeboxIdItemsPut(String id, SafeboxContentsRequest safeboxContentsRequest) {
        return SafeboxApi.super.safeboxIdItemsPut(id, safeboxContentsRequest);
    }

    @Override
    public ResponseEntity<SafeboxCreateResponse> safeboxPost(SafeboxCreateRequest safeboxCreateRequest) {
        return SafeboxApi.super.safeboxPost(safeboxCreateRequest);
    }
}
