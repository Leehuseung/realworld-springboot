package com.kr.realworldspringboot.repository;

import com.kr.realworldspringboot.dto.ProfileDTO;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.kr.realworldspringboot.entity.QFollow.follow;
import static com.kr.realworldspringboot.entity.QMember.member;

@AllArgsConstructor
@Repository
public class ProfileQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ProfileDTO getProfile(Long memberId, Long loginMemberId){
        ProfileDTO profileDTO = jpaQueryFactory
                                    .select(Projections.bean(ProfileDTO.class,
                                            member.username,
                                            member.bio,
                                            member.image,
                                            ExpressionUtils.as(JPAExpressions
                                                    .select(follow)
                                                    .from(follow)
                                                    .where(memberIdEq(loginMemberId),
                                                            follow.followMemberId.eq(memberId))
                                                    .fetchAll().exists()
                                                    ,"following")
                                    ))
                                    .from(member)
                                    .where(member.id.eq(memberId))
                                    .fetchOne();

        return profileDTO;
    }

    private Predicate memberIdEq(Long loginMemberId) {
        return loginMemberId == null ? null : follow.memberId.eq(loginMemberId);
    }
}
