package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.entity.*;
import com.kr.realworldspringboot.exception.DuplicateException;
import com.kr.realworldspringboot.repository.*;
import com.kr.realworldspringboot.util.LocalDateUtcParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ProfileService profileService;
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleFavoriteRepository articleFavoriteRepository;
    private final ArticleQueryRepository articleQueryRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final LocalDateUtcParser localDateUtcParser;

    @Override
    public Long createArticle(ArticleCreateDTO articleCreateDTO, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        Article article = createDtoToEntity(articleCreateDTO);

        if(articleRepository.countArticleBySlug(article.getSlug()) > 0){
            throw new DuplicateException("article");
        }

        article.setMember(member);
        articleRepository.save(article);

        List<String> tagList = articleCreateDTO.getTagList();
        if(tagList != null){
            insertTag(article, tagList);
        }
        return article.getId();
    }

    @Override
    public ArticleDTO getArticle(Long id,Long memberId) {
        Article article = articleRepository.findById(id).get();

        List<ArticleTag> tagList = article.getArticleTags();
        List<String> tagNameList = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            Tag tag = tagList.get(i).getTag();
            tagNameList.add(tag.getName());
        }

        ArticleDTO articleDTO = ArticleDTO.builder()
                .slug(article.getSlug())
                .title(article.getTitle())
                .description(article.getDescription())
                .body(article.getBody())
                .tagList(tagNameList)
                .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
                .favorited(memberId == null ? false : isFavorite(article,memberId))
                .favoritesCount(countFavoriteByArticle(article))
                .author(profileService.findProfile(article.getMember().getId(),memberId))
                .build();

        return articleDTO;
    }

    @Override
    public Article getArticleBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug);
        return article;
    }

    @Override
    @Transactional
    public void deleteArticle(Long id, Long memberId) {
        Article article = articleRepository.findById(id).get();
        if(memberId.equals(article.getMember().getId())){
            for (int i = 0; i < article.getArticleTags().size(); i++) {
                articleTagRepository.delete(article.getArticleTags().get(i));
            }
            articleFavoriteRepository.deleteArticleFavoritesByArticle(article);
            articleRepository.delete(article);
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    @Override
    public Long updateArticle(Long id, Long memberId, ArticleUpdateDTO articleUpdateDTO) {

        Article article = articleRepository.findById(id).get();
        String beforeTitle = article.getTitle();
        if(memberId.equals(article.getMember().getId())){
            modelMapper.map(articleUpdateDTO,article);
            article.setSlugify();
            if(!beforeTitle.equals(articleUpdateDTO.getTitle()) && articleRepository.countArticleBySlug(article.getSlug()) > 0){
                throw new DuplicateException("article");
            }
            articleRepository.save(article);

            for (int i = 0; i < article.getArticleTags().size(); i++) {
                articleTagRepository.delete(article.getArticleTags().get(i));
                article.setArticleTags(new ArrayList<ArticleTag>());
            }

            List<String> tagList = articleUpdateDTO.getTagList();
            if(tagList != null){
                insertTag(article, tagList);
            }

            return article.getId();
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    private void insertTag(Article article, List<String> tagList) {
        for (String tagName : tagList) {
            Tag tag;
            if (tagRepository.countByName(tagName) == 0) {
                tag = Tag.builder()
                        .name(tagName)
                        .build();
                tagRepository.save(tag);
            } else {
                tag = tagRepository.findByName(tagName);
            }
            ArticleTag articleTag = ArticleTag.builder()
                    .article(article)
                    .tag(tag)
                    .build();

            article.getArticleTags().add(articleTag);
            articleTagRepository.save(articleTag);
        }
    }

    @Override
    public boolean isFavorite(Article article, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return articleFavoriteRepository.countByArticleAndMember(article, member) != 0;
    }

    @Override
    public Long saveArticleFavorite(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId).get();
        Member member = memberRepository.findById(memberId).get();

        ArticleFavorite articleFavorite = ArticleFavorite.builder()
                .article(article)
                .member(member)
                .build();

        if(articleFavoriteRepository.countByArticleAndMember(articleFavorite.getArticle(),articleFavorite.getMember()) == 0){
            articleFavoriteRepository.save(articleFavorite);
        } else {
            articleFavorite = articleFavoriteRepository.findByArticleAndMember(articleFavorite.getArticle(),articleFavorite.getMember());
        }
        return articleFavorite.getId();
    }

    @Override
    public Long countFavoriteByArticle(Article article) {
        return articleFavoriteRepository.countByArticle(article);
    }

    @Override
    @Transactional
    public void deleteFavoriteByArticleAndMember(Article article, Member member) {
        articleFavoriteRepository.deleteArticleFavoriteByArticleAndMember(article,member);
    }

    @Override
    public JSONObject getArticles(ArticleSearch articleSearch,Long memberId) {
        JSONObject jsonObject = new JSONObject();
        int cnt = 0;
        List<Article> list = null;
        if(articleSearch.getTag() != null){
            list = articleQueryRepository.getArticleByTag(articleSearch);
            cnt = articleQueryRepository.getArticleByTagCount(articleSearch);
        } else if(articleSearch.getFavorited() != null){
            list = articleQueryRepository.getArticleByFavorite(articleSearch);
            cnt = articleQueryRepository.getArticleByFavoriteCount(articleSearch);
        } else {
            list = articleQueryRepository.getArticle(articleSearch);
            cnt = articleQueryRepository.getArticleCount(articleSearch);
        }
        List<ArticleDTO> dtoList = new ArrayList<>();

        for (Article article : list) {
            List<ArticleTag> tagList = article.getArticleTags();
            List<String> tagNameList = new ArrayList<>();
            for (int i = 0; i < tagList.size(); i++) {
                Tag tag = tagList.get(i).getTag();
                tagNameList.add(tag.getName());
            }
            if(articleSearch.getTag() != null){
                int index = 0;
                for (int i = 0; i < tagNameList.size(); i++) {
                    if(tagNameList.get(i).equals(articleSearch.getTag())){
                        index = i;
                    }
                }
                String name = tagNameList.get(index);
                tagNameList.remove(index);
                tagNameList.add(0,name);
            }

            ArticleDTO articleDTO = ArticleDTO.builder()
                    .slug(article.getSlug())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .body(article.getBody())
                    .tagList(tagNameList)
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
                    .favorited(memberId == null ? false : isFavorite(article,memberId))
                    .favoritesCount(countFavoriteByArticle(article))
                    .author(profileService.findProfile(article.getMember().getId(),memberId))
                    .build();

            dtoList.add(articleDTO);
        }
        jsonObject.put("articlesCount",cnt);
        jsonObject.put("articles",dtoList);
        return jsonObject;
    }

    @Override
    public JSONObject getFeeds(ArticleSearch articleSearch, Long memberId) {
        JSONObject jsonObject = new JSONObject();
        int cnt = articleQueryRepository.getFeedsCount(memberId);
        List<Article> list = articleQueryRepository.getFeeds(articleSearch,memberId);
        List<ArticleDTO> dtoList = new ArrayList<>();

        for (Article article : list) {
            List<ArticleTag> tagList = article.getArticleTags();
            List<String> tagNameList = new ArrayList<>();
            for (int i = 0; i < tagList.size(); i++) {
                Tag tag = tagList.get(i).getTag();
                tagNameList.add(tag.getName());
            }

            ArticleDTO articleDTO = ArticleDTO.builder()
                    .slug(article.getSlug())
                    .title(article.getTitle())
                    .description(article.getDescription())
                    .body(article.getBody())
                    .tagList(tagNameList)
                    .createdAt(localDateUtcParser.localDateTimeParseUTC(article.getCreatedAt()))
                    .updatedAt(localDateUtcParser.localDateTimeParseUTC(article.getUpdatedAt()))
                    .favorited(memberId == null ? false : isFavorite(article,memberId))
                    .favoritesCount(countFavoriteByArticle(article))
                    .author(profileService.findProfile(article.getMember().getId(),memberId))
                    .build();

            dtoList.add(articleDTO);
        }
        jsonObject.put("articlesCount",cnt);
        jsonObject.put("articles",dtoList);
        return jsonObject;
    }

}
