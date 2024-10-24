package com.ddubok.api.member.repository;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.UserState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    Optional<Member> findBySocialId(String socialId);

    List<Member> findByNicknameContaining(String keyword);

    List<Member> findByState(UserState state);

    @Query("SELECT m FROM member m WHERE m.nickname LIKE %:keyword% AND m.state = :state")
    List<Member> findByStateAndNickname(UserState state, String keyword);
}
