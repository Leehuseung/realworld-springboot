package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    int countByName(String name);

    Tag findByName(String name);
}
