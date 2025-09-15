package com.example.bankcards.repository;

import com.example.bankcards.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History,Long> {


}
