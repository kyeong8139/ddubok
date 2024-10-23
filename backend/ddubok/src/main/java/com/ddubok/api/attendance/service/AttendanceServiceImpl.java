package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.dto.response.FortuneRes;
import com.ddubok.api.attendance.entity.Attendance;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.api.attendance.repository.FortuneRepository;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.entity.UserState;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    final private AttendanceRepository attendanceRepository;
    final private FortuneRepository fortuneRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public AttendanceHistoryRes getAttendanceHistoryThisMonth(Long memberId) {
        List<LocalDate> attendanceList = getAttendanceList(memberId);

        return AttendanceHistoryRes.builder()
            .attendanceList(attendanceList)
            .attendanceCount(attendanceList.size())
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
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CreateAttendanceRes createAttendance(Long memberId) {

        /*
         * todo : Redis를 통하여 오늘의 출석 체크 여부 확인 로직 추가
         */

        /*
         * todo : 실제 Member 객체로 변경
         */
        Member member = Member.builder().id(1L).role(Role.ROLE_USER).socialProvider("kakao")
            .socialId("kakao123").nickname("lucky_user").state(UserState.ACTIVATED)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).deletedAt(null).build();

        Attendance attendance = Attendance.builder().member(member).build();
        attendanceRepository.save(attendance);

        return getCreateAttendanceRes(member);
    }

    private CreateAttendanceRes getCreateAttendanceRes(Member member) {
        return CreateAttendanceRes.builder()
            .fortune(getFortuneRes())
            .attendanceHistory(getAttendanceHistoryThisMonth(member.getId()))
            .build();
    }

    private FortuneRes getFortuneRes() {
        int rowCount = fortuneRepository.getRowCount();
        int rowNumber = (int) (Math.random() * (rowCount - 1));
        String sentence = fortuneRepository.getRandomFortuneSentence(rowNumber);
        int score = (int) (Math.random() * 40) + 60;

        return FortuneRes.builder()
            .sentence(sentence)
            .score(score)
            .build();
    }
}
