package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.entity.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getTagList();

    List<String> getTagNameList();
}
