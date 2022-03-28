package com.bol.mancala.repository;

import com.bol.mancala.dao.MancalaBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MancalaRepository extends JpaRepository<MancalaBoard, String> {}
