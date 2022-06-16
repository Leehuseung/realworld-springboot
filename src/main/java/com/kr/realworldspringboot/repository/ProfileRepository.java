package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByMemberId(Long memberId);

    @Query("select f from Follow f where f.memberId = :memberId and f.followMemberId = :followMemberId")
    Optional<Follow> findByMemberIdAndFollowUsername(Long memberId,Long followMemberId);




}
