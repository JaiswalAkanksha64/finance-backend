package com.zorvyn.finance.backend.controller;

import com.zorvyn.finance.backend.dto.DashboardDTO;
import com.zorvyn.finance.backend.dto.RecordDTO;
import com.zorvyn.finance.backend.service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard",
        description = "Financial summary and analytics endpoints")
public class DashBoardController {

    private final DashBoardService dashboardService;

    @Operation(summary = "Get financial summary",
            description = "Returns total income, expense and net balance")
    @GetMapping("/summary")
    public ResponseEntity<DashboardDTO> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @Operation(summary = "Get category wise totals",
            description = "Returns total amount spent per category")
    @GetMapping("/category-totals")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @Operation(summary = "Get monthly trends",
            description = "Returns total amounts grouped by month")
    @GetMapping("/monthly-trends")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyTrends() {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends());
    }

    @Operation(summary = "Get recent activity",
            description = "Returns 5 most recent financial records")
    @GetMapping("/recent-activity")
    public ResponseEntity<List<RecordDTO>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivity());
    }
}