package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemServiceDbImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemIntegrationTest {

    @Autowired
    private ItemServiceDbImpl itemService;

    @Autowired
    private UserRepository userRepository;

    private int userId;
    private ItemDTO item;
    private ItemDTO returnedItem;
    private List<ItemDTO> returnedItems;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("name");
        user.setEmail("valid@email.com");
        userId = userRepository.save(user).getId();

        item = new ItemDTO();
        item.setName("item name");
        item.setDescription("item description");
        item.setAvailable(true);
    }

    @Test
    void create_shouldReturnItemDTO_allValid() {
        returnedItem = itemService.create(item, userId);

        item.setId(returnedItem.getId());
        assertEquals(item, returnedItem);
    }

    @Test
    void getById_shouldReturnItemDTO_allValid() {
        int createdItemId = itemService.create(item, userId).getId();
        returnedItem = itemService.getById(createdItemId, 0);

        assertEquals(item.getName(), returnedItem.getName());
        assertEquals(item.getDescription(), returnedItem.getDescription());
        assertEquals(item.getAvailable(), returnedItem.getAvailable());
    }

    @Test
    void getByUser_shouldReturnListOfItemDTO_allValid() {
        itemService.create(item, userId);

        returnedItems = itemService.getByUser(userId, 0, 10);

        assertEquals(1, returnedItems.size());
        assertEquals(item.getName(), returnedItems.get(0).getName());
        assertEquals(item.getDescription(), returnedItems.get(0).getDescription());
        assertEquals(item.getAvailable(), returnedItems.get(0).getAvailable());
    }

    @Test
    void search_shouldReturnListOfItemDTO_allValid() {
        itemService.create(item, userId);

        returnedItems = itemService.search("item", 0, 10);

        assertEquals(1, returnedItems.size());
        assertEquals(item.getName(), returnedItems.get(0).getName());
        assertEquals(item.getDescription(), returnedItems.get(0).getDescription());
        assertEquals(item.getAvailable(), returnedItems.get(0).getAvailable());
    }

    @Test
    void update_shouldReturnItemDTO_allValid() {
        item = itemService.create(item, userId);
        item.setName("updated_name");
        item.setDescription("updated_description");

        itemService.update(item, userId);
        returnedItem = itemService.getById(item.getId(), userId);

        assertEquals(item.getName(), returnedItem.getName());
        assertEquals(item.getDescription(), returnedItem.getDescription());
        assertEquals(item.getAvailable(), returnedItem.getAvailable());
    }
}
