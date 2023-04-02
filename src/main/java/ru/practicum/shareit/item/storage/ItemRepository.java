package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, ItemRepositoryCustom {

    List<Item> findByOwnerId(int id);

    @Query(value = "select i from Item i where " +
            "upper(i.name) like upper (concat('%', ?1, '%')) " +
            "or upper(i.description) like upper (concat('%', ?1, '%')) " +
            "and i.available = true")
    List<Item> search(String string);

    ItemShort getShortById(int id);
}
