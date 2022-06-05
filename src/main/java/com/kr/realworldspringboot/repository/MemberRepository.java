package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.username in :name")
    Member findMemberByUsername(String name);

}
