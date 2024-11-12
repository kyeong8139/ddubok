package com.ddubok.api.attendance.service;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.dto.response.FortuneRes;
import com.ddubok.api.attendance.entity.Attendance;
import com.ddubok.api.attendance.exception.IllegalDateException;
import com.ddubok.api.attendance.repository.AttendanceRepository;
import com.ddubok.api.attendance.repository.FortuneRepository;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.notification.dto.request.NotificationMessageDto;
import com.ddubok.api.notification.repository.NotificationTokenRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    final private NotificationTokenRepository notificationTokenRepository;
    final private AttendanceRepository attendanceRepository;
    final private FortuneRepository fortuneRepository;
    final private MemberRepository memberRepository;
    final private RedisTemplate<String, Object> redisTemplate;

    /**
     * {@inheritDoc}
     */
    @Override
    public AttendanceHistoryRes getAttendanceHistoryThisMonth(Long memberId, int year, int month) {

        if(month<1||month>12){
            throw new IllegalDateException("월은 1에서 12 사이의 값이어야 합니다.");
        }

        List<LocalDate> attendanceList = attendanceRepository.getDateByMemberIdAndMonth(memberId,
            year, month);

        return AttendanceHistoryRes.builder()
            .attendanceList(attendanceList)
            .attendanceCount(attendanceList.size())
            .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CreateAttendanceRes createAttendance(Long memberId, LocalDate currentDate) {

        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();

        String key = "Attendance:" + memberId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return (CreateAttendanceRes) redisTemplate.opsForValue().get(key);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(
            MemberNotFoundException::new);
        Attendance attendance = Attendance.builder().member(member).date(currentDate).build();
        attendanceRepository.save(attendance);

        CreateAttendanceRes createAttendanceRes = getCreateAttendanceRes(member, year, month);
        saveCreateAttendanceResToRedis(key, createAttendanceRes);

        return createAttendanceRes;
    }

    @Override
    public void sendAttendanceNotification() {
        NotificationMessageDto message = NotificationMessageDto.builder()
            .id(null)
            .title("오늘의 운세가 배달됐어요!")
            .body("오늘의 운세를 확인해보세요!")
            .data(Map.of())
            .timestamp(LocalDateTime.now())
            .build();

        redisTemplate.convertAndSend("attendance-check", message);
    }

    private void saveCreateAttendanceResToRedis(String key,
        CreateAttendanceRes value) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().atTime(LocalTime.MIDNIGHT).plusDays(1);
        Duration ttl = Duration.between(now, midnight);

        redisTemplate.opsForValue().set(key, value, ttl);
    }

    private CreateAttendanceRes getCreateAttendanceRes(Member member, int year, int month) {
        return CreateAttendanceRes.builder()
            .fortune(getFortuneRes())
            .attendanceHistory(getAttendanceHistoryThisMonth(member.getId(), year, month))
            .build();
    }

    private FortuneRes getFortuneRes() {
        int rowCount = fortuneRepository.getRowCount();
        int rowNumber = (int) (Math.random() * (rowCount - 1));
        String sentence = fortuneRepository.getRandomFortuneSentence(rowNumber);

        int score;
        double randomValue = Math.random();

        if (randomValue < 0.1) {
            score = (int) (Math.random() * 11) + 60;
        } else if (randomValue < 0.4) {
            score = (int) (Math.random() * 11) + 90;
        } else {  // 71 ~ 89 (70% 확률)
            score = (int) (Math.random() * 19) + 71;
        }

        return FortuneRes.builder()
            .sentence(sentence)
            .score(score)
            .build();
    }
}
