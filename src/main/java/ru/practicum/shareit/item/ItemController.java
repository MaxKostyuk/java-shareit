package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDTO create(@RequestBody @Valid ItemDTO itemDTO, @RequestHeader(name = "X-Sharer-User-Id") int userId) {
        return itemService.create(itemDTO, userId);
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable @Positive int id) {
        return itemService.getById(id);
    }

    @GetMapping
    public List<ItemDTO> getByUser(@RequestHeader(name = "X-Sharer-User-Id") @Positive int userId) {
        return itemService.getByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam @NotBlank String text) {
        if (text.isBlank())
            return new ArrayList<>();
        return itemService.search(text);
    }

    @PatchMapping("/{id}")
    public ItemDTO update(@RequestBody ItemDTO itemDTO, @PathVariable @Positive int id,
                          @RequestHeader(name = "X-Sharer-User-Id") @Positive int userId) {
        itemDTO.setId(id);
        return itemService.update(itemDTO, userId);
    }


}
