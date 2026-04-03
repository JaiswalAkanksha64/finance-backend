package com.zorvyn.finance.backend.service;

import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.dto.RecordRequest;
import com.zorvyn.finance.backend.enums.Category;
import com.zorvyn.finance.backend.enums.TransactionType;
import com.zorvyn.finance.backend.exception.ResourceNotFoundException;
import com.zorvyn.finance.backend.model.FinancialRecord;
import com.zorvyn.finance.backend.model.User;
import com.zorvyn.finance.backend.repository.FinancialRecordRepository;
import com.zorvyn.finance.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordDTO createRecord(RecordRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        record.setCreatedBy(user);

        recordRepository.save(record);
        return convertToDTO(record);

    }

    public Map<String, Object> getAllRecords(
            TransactionType type,
            Category category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page, size, Sort.by("date").descending());
        List<FinancialRecord> records;

        if (type != null) {
            records = recordRepository.findByType(type);
        } else if (category != null) {
            records = recordRepository.findByCategory(category);
        } else if (startDate != null && endDate != null) {
            records = recordRepository.findByDateBetween(startDate, endDate);
        } else {
            records = recordRepository.findAll(pageable).getContent();
            Page<FinancialRecord> pageResult =
                    recordRepository.findAll(pageable);
            List<RecordDTO> dtos = pageResult.getContent()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("records", dtos);
            response.put("currentPage", pageResult.getNumber());
            response.put("totalPages", pageResult.getTotalPages());
            response.put("totalRecords", pageResult.getTotalElements());
            response.put("hasNext", pageResult.hasNext());
            return response;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("records", records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        response.put("currentPage", page);
        response.put("totalPages", 1);
        response.put("totalRecords", records.size());
        response.put("hasNext", false);
        return response;
    }

    public RecordDTO getRecordById(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Record not found with id: " + id));
        return convertToDTO(record);
    }

    public RecordDTO updateRecord(Long id, RecordRequest request) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Record not found with id: " + id));

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        recordRepository.save(record);
        return convertToDTO(record);
    }

    public void deleteRecord(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Record not found with id: " + id));
        recordRepository.delete(record);
    }

    private RecordDTO convertToDTO(FinancialRecord record) {
        RecordDTO dto = new RecordDTO();
        dto.setId(record.getId());
        dto.setAmount(record.getAmount());
        dto.setType(record.getType());
        dto.setCategory(record.getCategory());
        dto.setDate(record.getDate());
        dto.setNotes(record.getNotes());
        dto.setCreatedByName(record.getCreatedBy().getName());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        return dto;
    }
}