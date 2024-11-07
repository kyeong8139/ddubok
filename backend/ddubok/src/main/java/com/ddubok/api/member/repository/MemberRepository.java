package com.ddubok.api.member.repository;

import com.ddubok.api.member.entity.Member;
import com.ddubok.api.member.entity.UserState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);

    Optional<Member> findBySocialId(String socialId);

    Page<Member> findByNicknameContaining(String keyword, Pageable pageable);

    Page<Member> findByState(UserState state, Pageable pageable);

    @Query("SELECT m FROM member m WHERE m.nickname LIKE %:keyword% AND m.state = :state")
    Page<Member> findByStateAndNickname(UserState state, String keyword, Pageable pageable);

    @Override
    Page<Member> findAll(Pageable pageable);
}
