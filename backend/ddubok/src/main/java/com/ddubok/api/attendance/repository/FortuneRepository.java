package com.ddubok.api.attendance.repository;

import com.ddubok.api.attendance.entity.Fortune;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {

    @Query(value = "SELECT sentence FROM fortune LIMIT 1 OFFSET :number", nativeQuery = true)
    String getRandomFortuneSentence(@Param("number") int number);

    @Query("SELECT COUNT(*) FROM Fortune f")
    int getRowCount();
}
