package com.zorvyn.finance.backend.dto;

import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordDTO {

    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;
    private String notes;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
