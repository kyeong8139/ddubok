package com.ddubok.api.attendance.repository;

import com.ddubok.api.attendance.entity.Attendance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a.date FROM Attendance a WHERE a.member.id = :memberId AND MONTH(a.date) = :month")
    List<LocalDate> getDateByMemberIdAndMonth(Long memberId, int month);
}
