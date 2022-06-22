package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Tag;
import com.kr.realworldspringboot.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTagList() {
        List<Tag> list = tagRepository.getLastTags(PageRequest.of(0, 10));
        return list;
    }
}
