package com.ddubok.spring.api.attendance.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddubok.api.attendance.entity.Fortune;
import com.ddubok.api.attendance.repository.FortuneRepository;
import com.ddubok.spring.PersistenceLayerTestSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class FortuneRepositoryTest extends PersistenceLayerTestSupport {

    @Autowired
    private FortuneRepository fortuneRepository;

    @DisplayName("유효한 범위의 번호가 주어지면 해당 위치의 문장을 반환한다")
    @Test
    void getRandomFortuneSentenceWithHappyCase(){
      // given
        Fortune fortune1 = Fortune.builder()
            .sentence("운세내용1")
            .build();
        Fortune fortune2 = Fortune.builder()
            .sentence("운세내용2")
            .build();
        Fortune fortune3 = Fortune.builder()
            .sentence("운세내용3")
            .build();
        fortuneRepository.saveAll(List.of(fortune1, fortune2, fortune3));

      // when
        List<String> results = List.of(
            fortuneRepository.getRandomFortuneSentence(0),
            fortuneRepository.getRandomFortuneSentence(1),
            fortuneRepository.getRandomFortuneSentence(2)
        );

        // then
        assertThat(results)
            .hasSize(3)
            .containsExactly("운세내용1", "운세내용2", "운세내용3")
            .allMatch(sentence -> sentence != null && !sentence.isBlank());
    }

    @DisplayName("번호가 범위를 초과하면 null을 반환한다")
    @Test
    void getRandomFortuneSentenceWithNumberGreaterThanTheTotalNumber(){
      // given
        Fortune fortune1 = Fortune.builder()
            .sentence("운세내용1")
            .build();
        fortuneRepository.save(fortune1);

      // when
        String fortuneSentence = fortuneRepository.getRandomFortuneSentence(3);

      // then
        assertThat(fortuneSentence).isNull();
    }

    @DisplayName("데이터가 존재하지 않는다면 null을 반환한다.")
    @Test
    void getRandomFortuneSentenceWithoutData(){
        // given

        // when
        String fortuneSentence = fortuneRepository.getRandomFortuneSentence(0);

        // then
        assertThat(fortuneSentence).isNull();
    }

    @DisplayName("테이블에 저장된 전체 행 개수를 반환한다")
    @Test
    void getRowCountWithData(){
      // given
        Fortune fortune1 = Fortune.builder()
            .sentence("운세내용1")
            .build();
        Fortune fortune2 = Fortune.builder()
            .sentence("운세내용2")
            .build();
        Fortune fortune3 = Fortune.builder()
            .sentence("운세내용3")
            .build();
        fortuneRepository.saveAll(List.of(fortune1,fortune2,fortune3));
      // when
        int rowCnt = fortuneRepository.getRowCount();

      // then
        assertThat(rowCnt).isEqualTo(3);
    }

    @DisplayName("테이블에 저장된게 없다면 0을 반환한다.")
    @Test
    void getRowCountWithoutData(){
      // given

      // when
        int rowCnt = fortuneRepository.getRowCount();

        // then
        assertThat(rowCnt).isEqualTo(0);
    }
}