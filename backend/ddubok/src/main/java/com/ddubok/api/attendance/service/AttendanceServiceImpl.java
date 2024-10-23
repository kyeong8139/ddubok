package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CoinRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.dto.response.FortuneRes;
import com.ddubok.api.attendance.entity.Attendance;
import com.ddubok.api.attendance.entity.Streak;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.api.attendance.repository.FortuneRepository;
import com.ddubok.api.attendance.repository.StreakRepository;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.entity.UserState;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    final private AttendanceRepository attendanceRepository;
    final private StreakRepository streakRepository;
    final private FortuneRepository fortuneRepository;

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
        updateStreak(member);

        return getCreateAttendanceRes(member);
    }

    private void updateStreak(Member member) {
        Optional<Streak> streakOptional = streakRepository.findByMember(member);
        if (streakOptional.isEmpty()) {
            createStreak(member);
            return;
        }

        Streak streak = streakOptional.get();
        StreakAction action = evaluateStreakState(streak);
        switch (action) {
            case RESET:
                streak.resetCurrentStreak();
                streak.resetMaxStreak();
                break;
            case RESET_CURRENT:
                streak.resetCurrentStreak();
                break;
            case INCREMENT:
                streak.addCurrentStreak();
                if (streak.getCurrentStreak() > streak.getMaxStreak()) {
                    streak.syncMaxStreakWithCurrent();
                }
                break;
        }

        streakRepository.save(streak);
    }

    private void createStreak(Member member) {
        Streak streak = Streak.builder()
            .currentStreak(1)
            .maxStreak(1)
            .member(member)
            .build();

        streakRepository.save(streak);
    }

    private StreakAction evaluateStreakState(Streak streak) {
        int currentMonth = LocalDate.now().getMonthValue();
        int updatedMonth = streak.getUpdatedAt().getMonthValue();
        boolean isUpdatedThisMonth = (currentMonth == updatedMonth);

        if (!isUpdatedThisMonth) {
            return StreakAction.RESET;
        }

        int updatedDay = streak.getUpdatedAt().getDayOfMonth();
        int today = LocalDate.now().getDayOfMonth();
        boolean isAttendedYesterday = (today == updatedDay + 1);

        if (!isAttendedYesterday) {
            return StreakAction.RESET_CURRENT;
        }

        return StreakAction.INCREMENT;
    }

    private CreateAttendanceRes getCreateAttendanceRes(Member member) {
        return CreateAttendanceRes.builder()
            .coin(getCoinRes(member))
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

    private CoinRes getCoinRes(Member member) {
        /*
         * todo : 코인 획득량 알고리즘 추가
         */
        int changedCoinAmount = 0;

        /*
         * todo : 현재 코인 조회 로직 추가
         */
        int currentCoin = 10;

        return CoinRes.builder()
            .changedCoinAmount(changedCoinAmount)
            .currentCoin(currentCoin)
            .build();
    }
}
