package com.zorvyn.finance.backend.controller;

import com.zorvyn.finance.backend.dto.DashboardDTO;
import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/category-totals")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @GetMapping("/monthly-trends")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<RecordDTO>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }
}
