package com.ddubok.api.report.service;

import com.ddubok.api.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    final private ReportRepository reportRepository;
}