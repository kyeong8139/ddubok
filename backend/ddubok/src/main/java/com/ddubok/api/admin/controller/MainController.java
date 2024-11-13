package com.ddubok.api.admin.controller;

import com.ddubok.api.admin.service.SeasonService;
import com.ddubok.common.template.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    final private SeasonService seasonService;

    @GetMapping
    public BaseResponse<?> GetActiveSeason() {
        return BaseResponse.ofSuccess(seasonService.getActiveSeason());
    }
}
