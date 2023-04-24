package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer>, ItemRequestRepositoryCustom {

    @Query("select new ru.practicum.shareit.request.dto.ItemRequestDTO(ir.id, ir.description, ir.created) " +
            "from ItemRequest as ir " +
            "where ir.userId = ?1 " +
            "order by ir.created desc")
    List<ItemRequestDTO> getUserRequests(int userId);
}
