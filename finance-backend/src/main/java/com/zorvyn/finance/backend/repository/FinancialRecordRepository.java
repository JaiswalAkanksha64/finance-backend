package com.zorvyn.finance.backend.repository;

import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import com.zorvyn.finance.backend.model.FinancialRecord;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    List<FinancialRecord> findByType(TransactionType type);

    List<FinancialRecord> findByCategory(Category category);

    List<FinancialRecord> findByDateBetween(
            LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f " +
            "WHERE f.type = :type")
    BigDecimal sumByType(TransactionType type);

    @Query("SELECT SUM(f.amount) FROM FinancialRecord f " +
            "WHERE f.category = :category")
    BigDecimal sumByCategory(Category category);

    List<FinancialRecord> findTop5ByOrderByCreatedAtDesc();


}
