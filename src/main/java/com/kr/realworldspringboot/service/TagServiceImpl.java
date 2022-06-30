package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Tag;
import com.kr.realworldspringboot.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<String> getTagNameList() {
        List<Tag> list = tagRepository.getLastTags(PageRequest.of(0, 10));
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            arr.add(list.get(i).getName());
        }
        return arr;
    }
}
