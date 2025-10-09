package ru.checkdev.site.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.site.dto.FilterDTO;
import ru.checkdev.site.service.FilterService;

import javax.servlet.http.HttpServletRequest;

import static ru.checkdev.site.util.RequestResponseTools.getToken;

@Tag(name = "FilterRestController", description = "Filter REST API")
@RestController
@RequestMapping("/filter")
@AllArgsConstructor
public class FilterRestController {

    private final FilterService filterService;

    @GetMapping("/{userId}")
    public ResponseEntity<FilterDTO> getByUserId(
            @PathVariable int userId,
            HttpServletRequest req) throws JsonProcessingException {
        return new ResponseEntity<>(
                filterService.getByUserId(getToken(req), userId), HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public void save(@RequestBody FilterDTO filter,
                     HttpServletRequest req) throws JsonProcessingException {
        filterService.save(getToken(req), filter);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteByUserId(@PathVariable int userId,
                               HttpServletRequest req) throws JsonProcessingException {
        filterService.deleteByUserId(getToken(req), userId);
    }
}
