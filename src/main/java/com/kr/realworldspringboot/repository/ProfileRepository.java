package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Follow, String> {

    @Query("select f from Follow f where f.email = :followEmail and f.username = :username")
    Optional<Follow> findByEmailAndFollowUsername(String followEmail,String username);


}
