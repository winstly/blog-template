package com.winstly.blog.shared.ohs;

import java.util.List;

public interface TagOHS {

    List<TagDTO> listCategoriesByArticle(String articleCode);

    List<TagDTO> listTagsByArticle(String articleCode);

    List<TagDTO> listAllCategories();

    List<TagDTO> listAllTags();

    boolean existsTag(String tagCode);
}
