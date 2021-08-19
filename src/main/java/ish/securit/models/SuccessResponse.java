package ish.securit.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public class SuccessResponse {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response;
}
