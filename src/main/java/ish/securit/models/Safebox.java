package ish.securit.models;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Builder
@Jacksonized
@Table(name = "safebox")
public class Safebox {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String password;
}

