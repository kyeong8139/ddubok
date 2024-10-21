package com.ddubok.api.admin.service;

import com.ddubok.api.admin.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    final private SeasonRepository seasonRepository;
}
