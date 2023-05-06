package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceDbImpl;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    private static final int VALID_ID = 1;
    private static final int VALID_USER_ID = 1;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceDbImpl itemService;

    private ItemDTO itemDTO;
    private Item item;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;
    private List<ItemDTO> returnedItems;
    private ItemDTO returnedItem;
    private LocalDateTime time = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO(VALID_ID, "name", "description", true, 1);
        item = new Item(VALID_ID, "name", "description", true, 1, 1);
    }

    @Test
    @DisplayName("Create all valid")
    void create_shouldReturnItemDTO_allValid() {
        when(userRepository.getUserById(anyInt())).thenReturn(null);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        returnedItem = itemService.create(itemDTO, VALID_USER_ID);
        assertEquals(returnedItem, itemDTO);
        verify(userRepository, times(1)).getUserById(anyInt());
        verifyNoMoreInteractions(userRepository);
        verify(itemRepository, times(1)).save(itemCaptor.capture());
        verifyNoMoreInteractions(itemRepository);
        assertEquals(itemCaptor.getValue(), item);
    }

    @Test
    @DisplayName("Get by id all valid")
    void getById_shouldReturnItemDTO_allValid() {
        when(itemRepository.getItemById(anyInt())).thenReturn(item);
        when(bookingRepository.findTopByItemIdAndStartBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(bookingRepository.findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(anyInt(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(Optional.empty());
        when(commentRepository.findCommentByItem(anyInt())).thenReturn(new ArrayList<>());

        ItemDTO returnedItem = itemService.getById(VALID_ID, VALID_USER_ID);

        itemDTO.setComments(new ArrayList<>());
        assertEquals(returnedItem, itemDTO);
        verify(itemRepository, times(1)).getItemById(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1))
                .findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(anyInt(), any(LocalDateTime.class), any(BookingStatus.class));
        verify(bookingRepository, times(1))
                .findTopByItemIdAndStartBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
        verify(commentRepository, times(1)).findCommentByItem(VALID_ID);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    @DisplayName("Get by user all valid")
    void getByUser_shouldReturnListOfItemDTO_allValid() {
        when(userRepository.getUserById(anyInt())).thenReturn(new User());
        when(itemRepository.findByOwnerId(anyInt(), any(Pageable.class))).thenReturn(List.of(item));
        when(bookingRepository.findTopByItemIdAndStartBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(bookingRepository.findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(anyInt(), any(LocalDateTime.class), any(BookingStatus.class)))
                .thenReturn(Optional.empty());

        List<ItemDTO> returnedItems = itemService.getByUser(VALID_USER_ID, 0, 10);

        assertEquals(returnedItems.size(), 1);
        assertEquals(returnedItems.get(0), itemDTO);
        verify(userRepository, times(1)).getUserById(VALID_USER_ID);
        verifyNoMoreInteractions(userRepository);
        verify(itemRepository, times(1)).findByOwnerId(anyInt(), any(Pageable.class));
        verifyNoMoreInteractions(itemRepository);
        verify(bookingRepository, times(1))
                .findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(anyInt(), any(LocalDateTime.class), any(BookingStatus.class));
        verify(bookingRepository, times(1))
                .findTopByItemIdAndStartBeforeOrderByEndDesc(anyInt(), any(LocalDateTime.class));
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    @DisplayName("Search all valid")
    void search_shouldReturnListOfItemDTO_allValid() {
        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(List.of(item));

        returnedItems = itemService.search("text", 0, 10);
        assertEquals(returnedItems.size(), 1);
        assertEquals(returnedItems.get(0), itemDTO);
    }

    @Test
    @DisplayName("Update all valid")
    void update_shouldReturnItemDTO_allValid() {
        when(itemRepository.getItemById(anyInt())).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        itemDTO.setName("New Name");
        itemDTO.setDescription("New description");
        itemDTO.setAvailable(false);

        ItemDTO returnedItem = itemService.update(itemDTO, VALID_USER_ID);
        assertEquals(returnedItem, itemDTO);
        verify(itemRepository, times(1)).getItemById(itemDTO.getId());
        verify(itemRepository, times(1)).save(itemCaptor.capture());
        verifyNoMoreInteractions(itemRepository);

        item.setName("New Name");
        item.setDescription("New description");
        item.setAvailable(false);
        assertEquals(itemCaptor.getValue(), item);
    }

    @Test
    @DisplayName("Add comment all valid")
    void addComment_shouldReturnCommentDTO_allValid() {
        Comment comment = setComment();
        CommentDTO commentDTO = setCommentDTO();
        when(userRepository.getUserById(anyInt())).thenReturn(new User());
        when(itemRepository.getItemById(anyInt())).thenReturn(new Item());
        when(bookingRepository.checkBookingForComments(anyInt(), anyInt())).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO returnedComment = itemService.addComment(comment);
        assertEquals(returnedComment, commentDTO);
        verify(userRepository, times(1)).getUserById(VALID_USER_ID);
        verifyNoMoreInteractions(userRepository);
        verify(itemRepository, times(1)).getItemById(VALID_ID);
        verifyNoMoreInteractions(itemRepository);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verifyNoMoreInteractions(commentRepository);
        verify(bookingRepository, times(1)).checkBookingForComments(anyInt(), anyInt());
        verifyNoMoreInteractions(bookingRepository);
    }

    private Comment setComment() {
        Comment comment = new Comment();
        comment.setId(VALID_ID);
        comment.setText("text");
        comment.setItem(VALID_ID);
        comment.setAuthor(VALID_USER_ID);
        comment.setCreated(time);
        return comment;
    }

    private CommentDTO setCommentDTO() {
        return new CommentDTO(VALID_ID, "text", null, time);
    }
}
