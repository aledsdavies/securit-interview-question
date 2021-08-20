package ish.securit.dtos;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Value
public class ErrorMessage {
    String message;
    Exception ex;
}
