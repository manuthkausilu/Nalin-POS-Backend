package com.shashi.possysytembackend.repository;

import com.shashi.possysytembackend.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByActionDateBetween(LocalDateTime start, LocalDateTime end);
}