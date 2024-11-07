package com.ddubok.spring;

import static org.mockito.Mockito.when;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.Role;
import com.ddubok.api.member.entity.UserState;
import com.ddubok.api.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public abstract class BusinessLayerTestSupport {

    @Autowired
    protected MemberRepository memberRepository;

    protected Member testMember;

    @MockBean
    protected RedisTemplate<String, Object> redisTemplate;

    @Mock
    protected ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
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

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }
}
