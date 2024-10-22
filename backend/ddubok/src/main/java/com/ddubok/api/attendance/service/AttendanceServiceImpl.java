package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.entity.Streak;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.api.attendance.repository.StreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    final private AttendanceRepository attendanceRepository;
    final private StreakRepository streakRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public AttendanceHistoryRes getAttendanceHistoryThisMonth(Long memberId) {
        List<LocalDate> attendanceList = getAttendanceList(memberId);
        int maxAttendanceStreak = getMaxAttendanceStreak(memberId, attendanceList);

        return AttendanceHistoryRes.builder()
            .attendanceList(attendanceList)
            .attendanceCount(attendanceList.size())
            .maxAttendanceStreak(maxAttendanceStreak)
            .build();
    }

    /**
     * 특정 유저가 이번달에 출석한 일자의 리스트를 반환함
     *
     * @param memberId 유저의 id
     * @return 유저가 이번달에 출석한 일자의 리스트
     */
    private List<LocalDate> getAttendanceList(Long memberId) {
        int month = LocalDate.now().getMonthValue();
        return attendanceRepository.getDateByMemberIdAndMonth(memberId, month);
    }

    /**
     * 특정 회원의 이번달 최장 연속 출석 기간을 반환함
     *
     * @param memberId       회원의 id값
     * @param attendanceList 이번달 출석 기록
     * @return 이번달 최장 연속 출석 기간
     */
    private int getMaxAttendanceStreak(Long memberId, List<LocalDate> attendanceList) {

        /*
         *  if      출석 기록 < 2
         *  then    출석 기록 == 연속 출석 기간
         *
         *  c1. 0일 출석 -> 연속 출석 0일
         *  c2. 1일 출석 -> 연속 출석 1일
         */
        if (attendanceList.size() < 2) {
            return attendanceList.size();
        }

        Streak streak = streakRepository.findByMemberId(memberId);
        return streak.getMaxStreak();
    }

}
