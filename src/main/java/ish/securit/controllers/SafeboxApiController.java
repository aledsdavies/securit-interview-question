package ish.securit.controllers;

import ish.securit.models.Safebox;
import ish.securit.models.SafeboxContents;
import ish.securit.models.SafeboxCreateResponse;
import ish.securit.services.interfaces.SafeboxService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SafeboxApiController {

    private final SafeboxService safeboxService;

    @GetMapping("/{id}/items")
    public SafeboxContents getSafeboxContents(@PathVariable String id) {
        return safeboxService.getSafeboxContents(id);
    }

    @PostMapping
    public SafeboxCreateResponse createSafebox(@RequestBody @Valid Safebox safebox) {
        return safeboxService.createSafebox(safebox);
    }

    @PutMapping("/{id}/items")
    public void updateSafeboxContents(@PathVariable String id, @RequestBody @Valid SafeboxContents contents) {
        safeboxService.updateSafeboxContents(id, contents);
    }
}
