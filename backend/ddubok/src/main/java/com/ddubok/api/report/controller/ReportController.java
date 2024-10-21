package com.ddubok.api.report.controller;

import com.ddubok.api.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    final private ReportService reportService;
}
