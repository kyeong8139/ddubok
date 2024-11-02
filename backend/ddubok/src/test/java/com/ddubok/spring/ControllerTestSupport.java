package com.ddubok.spring;

import com.ddubok.api.attendance.controller.AttendanceController;
import com.ddubok.api.attendance.service.AttendanceServiceImpl;
import com.ddubok.common.auth.util.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    AttendanceController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AttendanceServiceImpl attendanceService;

    @MockBean
    protected AuthUtil authUtil;

}