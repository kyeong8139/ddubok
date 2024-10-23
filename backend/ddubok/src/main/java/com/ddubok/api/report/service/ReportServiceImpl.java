package com.ddubok.api.report.service;

import com.ddubok.api.admin.exception.SeasonNotFoundException;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.exception.CardNotFoundException;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.exception.MemberNotFoundException;
import com.ddubok.api.member.repository.MemberRepository;
import com.ddubok.api.report.dto.request.ReportMemberReq;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.repository.ReportRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @inheritDoc
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    final private ReportRepository reportRepository;
    final private MemberRepository memberRepository;
    final private CardRepository cardRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Long reportMember(ReportMemberReq reportMemberReq) {
        Member member = memberRepository.findById(reportMemberReq.getReportMemberId())
            .orElseThrow(() -> new MemberNotFoundException("유저 번호가 정확하지 않습니다."));
        Card card = cardRepository.findById(reportMemberReq.getCardId())
            .orElseThrow(() -> new CardNotFoundException("카드를 찾을 수 없습니다."));
        Report report = reportRepository.save(Report.builder()
            .content(reportMemberReq.getContent())
            .member(member)
            .card(card)
            .build());
        return report.getId();
    }
}