package com.ddubok.api.attendance.repository;

import com.ddubok.api.attendance.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a.date FROM Attendance a WHERE a.member.id = :memberId AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    List<LocalDate> getDateByMemberIdAndMonth(Long memberId, int year, int month);
}
