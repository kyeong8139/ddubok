package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    final private AttendanceRepository attendanceRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalDate> getAttendanceList(Long memberId) {
        int month = LocalDate.now().getMonthValue();
        return attendanceRepository.getDateByMemberIdAndMonth(memberId, month);
    }
}
