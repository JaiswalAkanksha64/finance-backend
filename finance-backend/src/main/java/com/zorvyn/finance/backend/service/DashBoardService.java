package com.zorvyn.finance.backend.service;

import com.zorvyn.finance.backend.dto.DashboardDTO;
import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import com.zorvyn.finance.backend.model.FinancialRecord;
import com.zorvyn.finance.backend.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final FinancialRecordRepository recordRepository;

    /**
     * Returns high level financial summary:
     * Total Income, Total Expense, Net Balance
     * Net Balance = Total Income - Total Expense
     */
    public DashboardDTO getSummary() {
        BigDecimal totalIncome = recordRepository
                .sumByType(TransactionType.INCOME);
        BigDecimal totalExpense = recordRepository
                .sumByType(TransactionType.EXPENSE);

        // Handle null case when no records exist yet
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;

        // Net balance = income - expense
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setNetBalance(netBalance);

        return dto;
    }

    /**
     * Returns total amount spent per category
     * Only includes categories that have records
     * Example: { "FOOD": 5000, "RENT": 15000 }
     */
    public Map<String, BigDecimal> getCategoryTotals() {
        Map<String, BigDecimal> categoryTotals = new HashMap<>();

        // Loop through all categories and sum amounts
        for (Category category : Category.values()) {
            BigDecimal total = recordRepository.sumByCategory(category);
            // Skip categories with no records
            if (total != null) {
                categoryTotals.put(category.name(), total);
            }
        }

        return categoryTotals;
    }

    /**
     * Returns monthly spending trends
     * Groups records by year-month and sums amounts
     * Example: { "2026-04": 68000, "2026-03": 45000 }
     */
    public Map<String, BigDecimal> getMonthlyTrends() {
        List<FinancialRecord> allRecords = recordRepository.findAll();
        Map<String, BigDecimal> monthlyTotals = new HashMap<>();

        for (FinancialRecord record : allRecords) {
            // Format key as "YYYY-MM"
            String monthKey = record.getDate().getYear() + "-" +
                    String.format("%02d", record.getDate().getMonthValue());

            // Add amount to existing month or create new entry
            monthlyTotals.merge(
                    monthKey,
                    record.getAmount(),
                    BigDecimal::add
            );
        }

        return monthlyTotals;
    }

    /**
     * Returns 5 most recent financial records
     * Ordered by creation date descending (newest first)
     */
    public List<RecordDTO> getRecentActivity() {
        return recordRepository
                .findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(record -> {
                    RecordDTO dto = new RecordDTO();
                    dto.setId(record.getId());
                    dto.setAmount(record.getAmount());
                    dto.setType(record.getType());
                    dto.setCategory(record.getCategory());
                    dto.setDate(record.getDate());
                    dto.setNotes(record.getNotes());
                    dto.setCreatedByName(record.getCreatedBy().getName());
                    dto.setCreatedAt(record.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
