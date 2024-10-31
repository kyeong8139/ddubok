package com.ddubok.spring;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@SpringBootTest
public abstract class PersistenceLayerTestSupport {

    @Autowired
    protected MemberRepository memberRepository;

    protected Member testMember;

    @BeforeEach
    void setUpMockMember() {
        memberRepository.deleteAllInBatch();

        // 공통 테스트용 Member 객체 생성
        testMember = Member.builder()
            .role(Role.ROLE_USER)
            .socialProvider("KAKAO")
            .socialId("TEST_SOCIAL_ID")
            .nickname("TestUser")
            .state(UserState.ACTIVATED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // memberRepository의 동작을 모킹하여, 테스트 내에서 일관된 데이터 반환 설정
        memberRepository.save(testMember);
    }
}
