package com.zorvyn.finance.backend.controller;

import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.dto.RecordRequest;
import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import com.zorvyn.finance.backend.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records",
        description = "CRUD operations for financial records")
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "Create a new financial record",
            description = "ANALYST and ADMIN only")
    @PostMapping
    public ResponseEntity<RecordDTO> createRecord(
            @Valid @RequestBody RecordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recordService.createRecord(
                        request, userDetails.getUsername()));
    }

    @Operation(summary = "Get all records",
            description = "Supports filtering by type, category, date range and pagination")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRecords(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                recordService.getAllRecords(
                        type, category, startDate, endDate, page, size));
    }

    @Operation(summary = "Get record by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RecordDTO> getRecordById(
            @PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @Operation(summary = "Update a record",
            description = "ANALYST and ADMIN only")
    @PutMapping("/{id}")
    public ResponseEntity<RecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody RecordRequest request) {
        return ResponseEntity.ok(
                recordService.updateRecord(id, request));
    }

    @Operation(summary = "Delete a record",
            description = "ADMIN only")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}