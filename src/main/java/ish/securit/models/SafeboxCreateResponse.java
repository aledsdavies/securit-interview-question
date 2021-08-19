package ish.securit.models;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class SafeboxCreateResponse {
    private String id;
}
