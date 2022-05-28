package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
