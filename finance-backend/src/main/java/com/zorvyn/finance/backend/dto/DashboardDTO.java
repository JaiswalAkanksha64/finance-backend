package com.zorvyn.finance.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class DashboardDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> categoryTotals;
}
