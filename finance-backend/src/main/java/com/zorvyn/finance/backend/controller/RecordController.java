package com.zorvyn.finance.backend.controller;

import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.dto.RecordRequest;
import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import com.zorvyn.finance.backend.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<RecordDTO> createRecord(
            @Valid @RequestBody RecordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(recordService.createRecord(
                        request, userDetails.getUsername()));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody RecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
