package com.epam.esm.gcs.web.controller;

import com.epam.esm.gcs.business.dto.TagDto;
import com.epam.esm.gcs.business.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    private List<TagDto> findAll() {
        return tagService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    private TagDto create(@RequestBody TagDto tag) {
        return tagService.create(tag);
    }

    @GetMapping("/{id}")
    private TagDto findById(@PathVariable Long id) {
        return tagService.remove(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    private TagDto update(@PathVariable Long id, @RequestBody TagDto tag) {
        return tagService.update(new TagDto(id, tag.getName()));
    }

    @DeleteMapping("/{id}")
    private TagDto delete(@PathVariable Long id) {
        return tagService.remove(id);
    }
}