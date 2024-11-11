package com.ddubok.spring.api.report.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddubok.api.admin.entity.Season;
import com.ddubok.api.admin.repository.SeasonRepository;
import com.ddubok.api.attendance.exception.IllegalDateException;
import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.report.dto.request.ReportCardReq;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.ReportType;
import com.ddubok.api.report.exception.InvalidTypeException;
import com.ddubok.api.report.repository.ReportRepository;
import com.ddubok.api.report.service.ReportServiceImpl;
import com.ddubok.spring.BusinessLayerTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReportServiceImplTest extends BusinessLayerTestSupport {

    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private CardRepository cardRepository;

    @AfterEach
    void tearDown() {
        reportRepository.deleteAllInBatch();
        cardRepository.deleteAllInBatch();
        seasonRepository.deleteAllInBatch();
    }

    @DisplayName("신고 사유가 비속어일 경우 신고에 비속어 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithVILIFICATION() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("수능 잘봐라 XXX야")
            .path("수능잘보라는이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("욕설이 있습니다.")
            .reportType("비속어")
            .content("해당 카드 안에 욕설이 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.VILIFICATION);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("신고 사유가 광고일 경우 신고에 광고 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithADVERTISMENT() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("광고가 있습니다.")
            .reportType("광고")
            .content("해당 카드 안에 광고가 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.ADVERTISMENT);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("신고 사유가 음란성 내용일 경우 신고에 음란성 내용 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithOBSCENE() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("광고가 있습니다.")
            .reportType("음란성 내용")
            .content("해당 카드 안에 광고가 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.OBSCENE);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("신고 사유가 사생활 침해일 경우 신고에 사생활 침해 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithPERSONALINFORMATION() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("광고가 있습니다.")
            .reportType("사생활 침해")
            .content("해당 카드 안에 광고가 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.PERSONALINFORMATION);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("신고 사유가 도배일 경우 신고에 도배 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithSPAM() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("광고가 있습니다.")
            .reportType("도배")
            .content("해당 카드 안에 광고가 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.SPAM);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("신고 사유가 기타일 경우 신고에 기타 타입이 올바르게 할당되는지 테스트")
    @Test
    void reportCardWithETC() {
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("광고가 있습니다.")
            .reportType("기타")
            .content("해당 카드 안에 광고가 포함되어 있습니다.")
            .build();

        // when
        Report result = reportService.reportCard(testMember.getId(), reportCardReq);

        // then
        assertThat(result)
            .isNotNull()
            .isInstanceOf(Report.class)
            .satisfies(response -> {
                assertThat(response.getReportType())
                    .isEqualTo(ReportType.ETC);
                assertThat(response.getContent())
                    .isEqualTo(reportCardReq.getContent());
                assertThat(response.getTitle())
                    .isEqualTo(reportCardReq.getTitle());
                assertThat(response.getCard().getId())
                    .isEqualTo(reportedCard.getId());
            });
    }

    @DisplayName("적절하지 않은 신고 사유가 들어올 경우 InvalidTypeException가 발생한다")
    @Test
    void reportCardWithInvalidType(){
        // given
        Season season = Season.builder()
            .name("시즌1")
            .description("시즌1입니다.")
            .path(List.of("이미지1", "이미지2", "이미지3"))
            .startedAt(LocalDateTime.of(2024, 10, 31, 9, 0, 0))
            .endedAt(LocalDateTime.of(2024, 11, 1, 9, 0, 0))
            .openedAt(LocalDateTime.of(2024, 11, 14, 9, 0, 0))
            .build();
        seasonRepository.save(season);

        Card reportedCard = Card.builderForSeasonCard()
            .content("싸다 공부 잘하게 되는 약이 1000원")
            .path("약판매광고이미지1")
            .writerName("글쓴이")
            .season(season)
            .build();
        cardRepository.save(reportedCard);

        ReportCardReq reportCardReq = ReportCardReq.builder()
            .cardId(reportedCard.getId())
            .title("신고잘되나요?")
            .reportType("테스트")
            .content("신고기능 테스트를 위한 테스트입니다.")
            .build();

        // when & then
        assertThatThrownBy(
            () -> reportService.reportCard(testMember.getId(), reportCardReq))
            .isInstanceOf(InvalidTypeException.class)
            .hasMessage("잘못된 신고 사유 값입니다: " + reportCardReq.getReportType());
    }
}