package com.example.bankcards.repository;


import com.example.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardsRepository extends JpaRepository<Card, Long> {

    List<Card> findByOwnerId(Long id);

    Optional<Card> findByCardId(long id);

   Optional<Card> findByCardIdAndOwnerId(long id,long ownerId);

}
