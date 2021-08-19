package ish.securit.controllers;

import ish.securit.models.SafeboxContents;
import ish.securit.models.Safebox;
import ish.securit.models.SafeboxCreateResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/safebox")
public class SafeboxApiController {

    @GetMapping("/{id}/items")
    public SafeboxContents getSafeboxContents(@PathVariable String id) {
        return null;
    }

    @PostMapping
    public SafeboxCreateResponse createSafebox(@RequestBody @Valid Safebox request) {
        return null;
    }

    @PutMapping("/{id}/items")
    public void updateSafeboxContents(@PathVariable String id, @RequestBody @Valid SafeboxContents contents) {

    }
}
