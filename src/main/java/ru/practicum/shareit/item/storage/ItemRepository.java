package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, ItemRepositoryCustom {

    List<Item> findByOwnerId(int id, Pageable page);

    @Query(value = "select i from Item i where " +
            "upper(i.name) like upper (concat('%', ?1, '%')) " +
            "or upper(i.description) like upper (concat('%', ?1, '%')) " +
            "and i.available = true")
    List<Item> search(String string, Pageable page);

    @Query("select  new ru.practicum.shareit.item.dto.ItemDTO(i.id, i.name, i.description, i.available, i.requestId) " +
            "from Item as i " +
            "where i.requestId = ?1 " +
            "order by i.id")
    List<ItemDTO> getItemsByRequestId(int requestId);
}
