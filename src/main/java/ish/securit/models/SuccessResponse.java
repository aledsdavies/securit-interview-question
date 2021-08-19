package ish.securit.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
public class SuccessResponse {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object response;
}
