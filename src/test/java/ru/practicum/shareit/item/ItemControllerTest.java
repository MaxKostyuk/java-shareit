package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private static final int VALID_USER_ID = 123;
    private static final int INVALID_USER_ID = -1;
    public static final String VALID_FROM = "0";
    public static final String INVALID_FROM = "-1";
    public static final String VALID_SIZE = "10";
    public static final String INVALID_SIZE = "0";

    @MockBean
    private ItemService itemServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDTO itemDTO;
    private Comment comment;


    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO(1, "item", "description", true, 0);

        comment = new Comment();
        comment.setText("comment");
    }

    @Test
    @DisplayName("Add item with valid arguments")
    void create_shouldReturnItem_validItem_validUserId() throws Exception {
        when(itemServiceMock.create(any(ItemDTO.class), anyInt())).thenReturn(itemDTO);

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDTO.getId()))
                .andExpect(jsonPath("$.name").value(itemDTO.getName()))
                .andExpect(jsonPath("$.description").value(itemDTO.getDescription()));
    }

    @Test
    @DisplayName("Add item with invalid user id")
    void create_shouldReturnBadRequest_validItem_invalidUserId() throws Exception {
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add item with invalid item")
    void create_shouldReturnBadRequest_invalidItem_validUserId() throws Exception {
        itemDTO.setName(null);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get item with valid arguments")
    void getById_shouldReturnItem_validItemId_validUserId() throws Exception {
        when(itemServiceMock.getById(anyInt(), anyInt())).thenReturn(itemDTO);

        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDTO.getId()))
                .andExpect(jsonPath("$.name").value(itemDTO.getName()))
                .andExpect(jsonPath("$.description").value(itemDTO.getDescription()));
    }

    @Test
    @DisplayName("Get item with invalid user id")
    void getById_shouldReturnNotFound_validItemId_invalidUserId() throws Exception {
        mockMvc.perform(get("/item/1")
                        .header(USER_ID_HEADER, INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get item with invalid item id")
    void getById_shouldReturnNotFound_invalidItemId_validUserId() throws Exception {
        mockMvc.perform(get("/item/-1")
                        .header(USER_ID_HEADER, VALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get by user with valid arguments")
    void getByUser_shouldReturnItem_validFrom_validSize_validUserId() throws Exception {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        when(itemServiceMock.getByUser(anyInt(), anyInt(), anyInt())).thenReturn(itemDTOList);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDTO.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDTO.getDescription()));
    }

    @Test
    @DisplayName("Get by user with invalid user id")
    void getByUser_shouldReturnBadRequest_validFrom_validSize_invalidUserId() throws Exception {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        when(itemServiceMock.getByUser(anyInt(), anyInt(), anyInt())).thenReturn(itemDTOList);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, INVALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get by user with invalid from")
    void getByUser_shouldReturnBadRequest_invalidFrom_validSize_validUserId() throws Exception {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        when(itemServiceMock.getByUser(anyInt(), anyInt(), anyInt())).thenReturn(itemDTOList);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .param("from", INVALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get by user with invalid size")
    void getByUser_shouldReturnBadRequest_validFrom_invalidSize_validUserId() throws Exception {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        when(itemServiceMock.getByUser(anyInt(), anyInt(), anyInt())).thenReturn(itemDTOList);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .param("from", VALID_FROM)
                        .param("size", INVALID_SIZE))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Search with valid arguments")
    void search_shouldReturnItem_validText_validFrom_validSize() throws Exception {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        itemDTOList.add(itemDTO);

        when(itemServiceMock.search(anyString(), anyInt(), anyInt())).thenReturn(itemDTOList);

        mockMvc.perform(get("/items/search")
                        .param("text", "test_text")
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDTO.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDTO.getDescription()));
    }

    @Test
    @DisplayName("Search with empty text")
    void search_shouldReturnEmptyArray_emptyText_validFrom_validSize() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "   ")
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Search with invalid text")
    void search_shouldReturnBadRequest_invalidText_validFrom_validSize() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("from", VALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Search with invalid from")
    void search_shouldReturnBadRequest_validText_invalidFrom_validSize() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "test_text")
                        .param("from", INVALID_FROM)
                        .param("size", VALID_SIZE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Search with invalid size")
    void search_shouldReturnBadRequest_validText_validFrom_invalidSize() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "test_text")
                        .param("from", VALID_FROM)
                        .param("size", INVALID_SIZE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update with valid arguments")
    void update_shouldReturnItem_validItem_validItemId_validUserId() throws Exception {
        when(itemServiceMock.update(any(ItemDTO.class), anyInt())).thenReturn(itemDTO);

        mockMvc.perform(patch(("/items/1"))
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDTO.getId()))
                .andExpect(jsonPath("$.name").value(itemDTO.getName()))
                .andExpect(jsonPath("$.description").value(itemDTO.getDescription()));
    }

    @Test
    @DisplayName("Update with no item")
    void update_shouldReturnBadRequest_noItem_validItemId_validUserId() throws Exception {
        mockMvc.perform(patch(("/items/1"))
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update with invalid item id")
    void update_shouldReturnBadRequest_validItem_invalidItemId_validUserId() throws Exception {
        mockMvc.perform(patch(("/items/-1"))
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update with invalid user id")
    void update_shouldReturnBadRequest_validItem_validItemId_invalidUserId() throws Exception {
        mockMvc.perform(patch(("/items/1"))
                        .header(USER_ID_HEADER, INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add comment with valid arguments")
    void addComment_shouldReturnCommentDTO_validComment_validItemId_validUserId() throws Exception {
        String commentText = "comment_text";
        String commentAuthor = "author";
        LocalDateTime time = LocalDateTime.now();
        CommentDTO commentDTO = CommentDTO.builder()
                .id(1)
                .text(commentText)
                .authorName(commentAuthor)
                .created(time)
                .build();

        when(itemServiceMock.addComment(any(Comment.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value(commentText))
                .andExpect(jsonPath("$.authorName").value(commentAuthor));
    }

    @Test
    @DisplayName("Add comment with invalid comment")
    void addComment_shouldReturnBadRequest_invalidComment_validItemId_validUserId() throws Exception {
        comment.setText(null);

        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add comment with invalid item id")
    void addComment_shouldReturnBadRequest_validComment_invalidItemId_validUserId() throws Exception {
        mockMvc.perform(post("/items/-1/comment")
                        .header(USER_ID_HEADER, VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add comment with invalid user id")
    void addComment_shouldReturnBadRequest_validComment_validItemId_invalidUserId() throws Exception {
        mockMvc.perform(post("/items/1/comment")
                        .header(USER_ID_HEADER, INVALID_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest());
    }


}
