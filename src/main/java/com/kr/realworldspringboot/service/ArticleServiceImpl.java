package com.kr.realworldspringboot.service;

import com.kr.realworldspringboot.dto.ArticleCreateDTO;
import com.kr.realworldspringboot.dto.ArticleDTO;
import com.kr.realworldspringboot.dto.ArticleUpdateDTO;
import com.kr.realworldspringboot.entity.*;
import com.kr.realworldspringboot.repository.*;
import com.kr.realworldspringboot.util.LocalDateUtcParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        List<String> tagList = articleCreateDTO.getTagList();

        article.setMember(member);
        articleRepository.save(article);

        //tag insert
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

            /*
             * article을 save해도 이 이후에 같은 컨트롤러에서 article을 조회해도 ArticleTags는 값이 존재하지않는다.
             * EntityManager가 flush,clear 된다면 문제가 없겠지만, 1차캐시(영속성컨텍스트)에만 관리할 때는
             * 아직 DB에 실제로 적용된 것이 아니기 때문에. 메모리에만 올라가 있는 상태임.
             *
             * ArticleTag에 article을 set하는 함수에서 article.getArticleTags.add(this) 처럼 한번에 관리해주는 것이 편하다.
             */
            article.getArticleTags().add(articleTag);
            articleTagRepository.save(articleTag);
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

        if(memberId.equals(article.getMember().getId())){
            modelMapper.map(articleUpdateDTO,article);
            article.setSlugify();
            articleRepository.save(article);
            return article.getId();
        } else {
            throw new IllegalArgumentException("not authorized");
        }
    }

    @Override
    public boolean isFavorite(Article article, Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return articleFavoriteRepository.countByArticleAndMember(article, member) != 0;
    }

    @Override
    public Long saveArticleFavorite(ArticleFavorite articleFavorite) {
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
    public List<Article> getArticles(ArticleSearch articleSearch) {
        //TODO articleSearch 구현
        List<Article> list = articleQueryRepository.getArticle(articleSearch);
        return list;
    }

    @Override
    public int getArticleCount(ArticleSearch articleSearch) {
        return articleQueryRepository.getArticleCount(articleSearch);
    }

}
