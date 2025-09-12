package com.petvet.repository;
import com.petvet.entity.ProcedureHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureHistoryRepository extends JpaRepository<ProcedureHistory, Long> {}
