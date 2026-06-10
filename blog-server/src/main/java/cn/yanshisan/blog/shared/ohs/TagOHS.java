package cn.yanshisan.blog.shared.ohs;

import java.util.List;

public interface TagOHS {

    List<TagVO> listCategoriesByArticle(String articleCode);

    List<TagVO> listTagsByArticle(String articleCode);

    List<TagVO> listAllCategories();

    List<TagVO> listAllTags();

    boolean existsTag(String tagCode);
}
