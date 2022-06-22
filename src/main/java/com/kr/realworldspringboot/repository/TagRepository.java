package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Member;
import com.kr.realworldspringboot.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    int countByName(String name);

    Tag findByName(String name);

    @Query("select t from Tag t order by t.id desc")
    List<Tag> getLastTags(Pageable pageable);
}
