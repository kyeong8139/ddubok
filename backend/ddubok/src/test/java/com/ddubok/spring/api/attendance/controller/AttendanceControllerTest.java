package com.ddubok.spring.api.attendance.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.ddubok.api.attendance.dto.response.AttendanceHistoryRes;
import com.ddubok.api.attendance.dto.response.CreateAttendanceRes;
import com.ddubok.api.attendance.dto.response.FortuneRes;
import com.ddubok.spring.ControllerTestSupport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class AttendanceControllerTest extends ControllerTestSupport {

    @WithMockUser
    @DisplayName("출석 기록 조회 성공 테스트")
    @Test
    void getAttendanceHistorySuccess() throws Exception {
        // given
        Long memberId = 1L;
        LocalDate currentDate = LocalDate.of(2024, 10, 31);
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        LocalDate date1 = currentDate.minusDays(1);
        LocalDate date2 = currentDate.minusDays(2);
        LocalDate date3 = currentDate.minusDays(3);
        AttendanceHistoryRes historyRes = AttendanceHistoryRes.builder()
            .attendanceList(List.of(date1, date2, date3, currentDate))
            .attendanceCount(4)
            .build();
        given(authUtil.getMemberId()).willReturn(memberId);
        given(attendanceService.getAttendanceHistoryThisMonth(anyLong(), anyInt(), anyInt()))
            .willReturn(historyRes);

        // when & then
        mockMvc.perform(get("/api/v1/attendances"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.attendanceCount").value(4))
            .andExpect(jsonPath("$.data.attendanceList", hasSize(4)))
            .andExpect(jsonPath("$.data.attendanceList[0]").value(date1.toString()))
            .andExpect(jsonPath("$.data.attendanceList[1]").value(date2.toString()))
            .andExpect(jsonPath("$.data.attendanceList[2]").value(date3.toString()))
            .andExpect(jsonPath("$.data.attendanceList[3]").value(currentDate.toString()));
    }

    @WithMockUser
    @DisplayName("출석 생성 성공 테스트")
    @Test
    void createAttendanceSuccess() throws Exception {
        // given
        Long memberId = 1L;
        LocalDate currentDate = LocalDate.of(2024, 10, 31);
        LocalDate date1 = currentDate.minusDays(1);
        LocalDate date2 = currentDate.minusDays(2);
        LocalDate date3 = currentDate.minusDays(3);
        CreateAttendanceRes createRes = CreateAttendanceRes.builder()
            .attendanceHistory(
                AttendanceHistoryRes.builder()
                    .attendanceList(List.of(date1, date2, date3, currentDate))
                    .attendanceCount(4)
                    .build()
            )
            .fortune(FortuneRes.builder()
                .score(80)
                .sentence("운세내용1")
                .build())
            .build();
        given(authUtil.getMemberId()).willReturn(memberId);
        given(attendanceService.createAttendance(anyLong(), any())).willReturn(createRes);

        // when & then
        mockMvc.perform(post("/api/v1/attendances").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.fortune.sentence").value("운세내용1"))
            .andExpect(jsonPath("$.data.fortune.score").value(80))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceCount").value(4))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceList", hasSize(4)))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceList[0]").value(date1.toString()))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceList[1]").value(date2.toString()))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceList[2]").value(date3.toString()))
            .andExpect(jsonPath("$.data.attendanceHistory.attendanceList[3]").value(currentDate.toString()));

    }
}