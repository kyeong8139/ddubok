package com.ddubok.api.admin.controller;

import com.ddubok.api.admin.service.MemberStatusService;
import com.ddubok.api.admin.service.SeasonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    final private SeasonService seasonService;
    final private MemberStatusService memberStatusService;

}
