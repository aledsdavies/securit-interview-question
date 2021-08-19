package ish.securit.models;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Builder
@Jacksonized
public class SafeboxContents {

    @NotNull
    private ArrayList<String> items;
}
