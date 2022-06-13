package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Member findMemberByUsername(String username);

    Optional<Member> findByEmail(String email);


}
