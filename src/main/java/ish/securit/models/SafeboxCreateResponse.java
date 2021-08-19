package ish.securit.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class SafeboxCreateResponse {
    private String id;
}
