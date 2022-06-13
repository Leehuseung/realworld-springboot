package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Follow, String> {

    Optional<Follow> findByMemberId(Long memberId);

    @Query("select f from Follow f where f.memberId = :memberId and f.username = :username")
    Optional<Follow> findByMemberIdAndFollowUsername(Long memberId,String username);

    @Query("update Follow f set f.username = :change_username where f.username = :username")
    @Modifying
    int updateFollowUsername(String username, String change_username);




}
