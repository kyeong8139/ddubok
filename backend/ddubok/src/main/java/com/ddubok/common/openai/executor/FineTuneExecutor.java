package com.ddubok.common.openai.executor;

import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.repository.ReportRepository;
import com.ddubok.common.openai.service.FineTuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class FineTuneExecutor {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private FineTuneService fineTuneService;

    public void executeFineTuning() throws IOException {
        List<Report> reports = reportRepository.findByStateIn(List.of("ACCEPT", "REJECT"));

        File jsonlFile = Report.createTrainingDataFile(reports);

        fineTuneService.startFineTuning(jsonlFile);
    }
}