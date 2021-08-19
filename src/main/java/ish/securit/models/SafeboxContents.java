package ish.securit.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Builder
@Jacksonized
@Getter
public class SafeboxContents {


    @NotNull
    private ArrayList<String> items;
}
