package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ItemRepositoryTest {

    private static final int REQUEST_ID = 123;
    private static final int OWNER_ID = 1;
    private static final String ITEM_NAME = "name";
    private static final String ITEM_DESCRIPTION = "description";
    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private ItemDTO itemDTO;
    private List<ItemDTO> returnedItems;


    @BeforeEach
    void setUp() {
        item = Item.builder()
                .name(ITEM_NAME)
                .description(ITEM_DESCRIPTION)
                .available(true)
                .ownerId(OWNER_ID)
                .requestId(REQUEST_ID)
                .build();
    }

    @Test
    @DisplayName("search all valid")
    void search_shouldReturnListOfItems_allValid() {
        itemRepository.save(item);

        List<Item> returnedItems = itemRepository.search(ITEM_DESCRIPTION.substring(3), PageRequest.of(0, 10, Sort.by("id").ascending()));
        assertEquals(returnedItems.size(), 1);
        item.setId(1);
        assertEquals(returnedItems.get(0), item);
    }

    @Test
    @DisplayName("Get items by request id all valid")
    void getItemsByRequestId_shouldReturnListOfItemDTO_allValid() {
        itemDTO = ItemMapper.toItemDTO(itemRepository.save(item));

        returnedItems = itemRepository.getItemsByRequestId(REQUEST_ID);

        assertEquals(returnedItems.size(), 1);
        assertEquals(returnedItems.get(0), itemDTO);
    }

    @Test
    @DisplayName("Get item by id all valid")
    void getItemById_shouldReturnItem_allValid() {
        item = itemRepository.save(item);

        Item returnedItem = itemRepository.getItemById(item.getId());

        assertEquals(returnedItem, item);
    }

    @Test
    @DisplayName("Get item by id when no item")
    void getItemById_shouldThrowException_noItemInDB() {
        assertThrows(ElementNotFoundException.class, () -> itemRepository.getItemById(999));
    }
}
