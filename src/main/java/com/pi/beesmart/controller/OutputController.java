package com.pi.beesmart.controller;

import com.pi.beesmart.model.OutputDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class OutputController {

    @GetMapping("/output/")
    public List<OutputDTO> getOutput() {
        return new OutputConverter().toDTO(new OutputDAO().getAll());
    }
    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @PutMapping("/output/{gpio}")
    public OutputDTO putOutput(@RequestBody OutputDTO dto, @PathVariable int gpio) {
        final OutputConverter converter = new OutputConverter();
        return converter.toDTO(new OutputDAO().update(converter.toEntity(dto), gpio));
    }
}
