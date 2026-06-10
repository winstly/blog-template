-- ============================================================
-- Blog 数据库建表脚本
-- MySQL 8.0+ | utf8mb4 + utf8mb4_unicode_ci | InnoDB
-- ngram_token_size = 2
-- ============================================================

-- 1. 系统字典
CREATE TABLE sys_dict (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  biz_code        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '一级业务码',
  sub_code        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '二级业务码',
  item_code       VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '三级业务码',
  item_label      VARCHAR(128) NOT NULL DEFAULT '' COMMENT '条目显示名',
  item_value      VARCHAR(512) NOT NULL DEFAULT '' COMMENT '条目值',
  ext_data        JSON         DEFAULT NULL COMMENT '扩展数据',
  sort_order      INT          NOT NULL DEFAULT 0 COMMENT '排序序号',
  display_status  TINYINT      NOT NULL DEFAULT 1 COMMENT '展示状态: 0=隐藏 1=显示',
  remark          VARCHAR(256) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY (id),
  UNIQUE KEY uk_dict_item (biz_code, sub_code, item_code),
  INDEX idx_dict_biz (biz_code, sub_code, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统字典表';

-- 2. 标签（树形结构）
CREATE TABLE blog_tag (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  tag_code        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '标签编码',
  tag_name        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '标签名称',
  tree_path       VARCHAR(512) DEFAULT NULL COMMENT '物化路径',
  tree_depth      TINYINT      NOT NULL DEFAULT 0 COMMENT '层级深度: 0=根',
  sort_order      INT          NOT NULL DEFAULT 0 COMMENT '排序序号',
  display_status  TINYINT      NOT NULL DEFAULT 1 COMMENT '展示状态: 0=隐藏 1=显示',
  description     VARCHAR(256) DEFAULT NULL COMMENT '标签描述',

  PRIMARY KEY (id),
  UNIQUE KEY uk_tag_code (tag_code),
  INDEX idx_tag_path (tree_path(191)),
  INDEX idx_tag_depth (tree_depth, tree_path(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 3. 内容条目
CREATE TABLE blog_article (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  article_code    VARCHAR(128) NOT NULL DEFAULT '' COMMENT '编码',
  title           VARCHAR(256) NOT NULL DEFAULT '' COMMENT '标题',
  summary         VARCHAR(512) DEFAULT NULL COMMENT '摘要',
  cover_url       VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
  is_pinned       TINYINT      NOT NULL DEFAULT 0 COMMENT '是否置顶: 0=否 1=是',
  view_count      INT          NOT NULL DEFAULT 0 COMMENT '浏览量',
  published_at    TIMESTAMP    NULL DEFAULT NULL COMMENT '发布时间',

  PRIMARY KEY (id),
  UNIQUE KEY uk_article_code (article_code),
  INDEX idx_article_published (published_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容条目表';

-- 4. 条目-标签关联
CREATE TABLE blog_article_tag (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  article_code    VARCHAR(128) NOT NULL DEFAULT '' COMMENT '文章编码',
  tag_code        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '标签编码',
  relation_type   VARCHAR(16)  NOT NULL DEFAULT 'tag' COMMENT '关联类型: category=分类 tag=标签',

  PRIMARY KEY (id),
  UNIQUE KEY uk_article_tag (article_code, tag_code, relation_type),
  INDEX idx_tag_articles (tag_code),
  INDEX idx_relation_type (relation_type, tag_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='条目-标签关联表';

-- 5. 访客交互
CREATE TABLE blog_interaction (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  user_name       VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '用户昵称',
  user_avatar_url VARCHAR(512) DEFAULT NULL COMMENT '用户头像URL',
  target_type     VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '目标类型',
  target_code     VARCHAR(128) NOT NULL DEFAULT '' COMMENT '目标编码',
  action_type     VARCHAR(16)  NOT NULL DEFAULT '' COMMENT '动作类型',
  remark          VARCHAR(512) DEFAULT NULL COMMENT '短文本(点赞理由等，评论正文见blog_content)',
  tree_path       VARCHAR(512) DEFAULT NULL COMMENT '物化路径',
  tree_depth      TINYINT      NOT NULL DEFAULT 0 COMMENT '层级深度: 0=根',
  ext_meta        JSON         DEFAULT NULL COMMENT '扩展元数据',

  PRIMARY KEY (id),
  INDEX idx_interaction_target (target_type, target_code, gmt_create),
  INDEX idx_interaction_path (tree_path(191)),
  INDEX idx_interaction_action (action_type, target_type, target_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='访客交互表';

-- 6. 正文内容
CREATE TABLE blog_content (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  target_type     VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '宿主类型',
  target_code     VARCHAR(128) NOT NULL DEFAULT '' COMMENT '宿主编码',
  body            MEDIUMTEXT   NOT NULL COMMENT '正文内容',
  content_format  VARCHAR(16)  NOT NULL DEFAULT 'markdown' COMMENT '内容格式',
  word_count      INT          NOT NULL DEFAULT 0 COMMENT '字数',
  version         INT          NOT NULL DEFAULT 1 COMMENT '当前版本号',
  publish_status  TINYINT      NOT NULL DEFAULT 0 COMMENT '发布状态: 0=草稿 1=已发布',

  PRIMARY KEY (id),
  UNIQUE KEY uk_content_target (target_type, target_code),
  FULLTEXT INDEX ft_content_search (body) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='正文内容表';

-- 7. 正文修订快照
CREATE TABLE blog_content_revision (
  id              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  creator         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '创建人',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modifier        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '修改人',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  is_deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=正常 1=已删除',

  content_id      BIGINT       NOT NULL COMMENT '内容ID',
  body            MEDIUMTEXT   NOT NULL COMMENT '快照正文',
  content_format  VARCHAR(16)  NOT NULL DEFAULT 'markdown' COMMENT '内容格式',
  word_count      INT          NOT NULL DEFAULT 0 COMMENT '字数',
  version         INT          NOT NULL COMMENT '版本号',
  change_summary  VARCHAR(256) DEFAULT NULL COMMENT '变更说明',

  PRIMARY KEY (id),
  UNIQUE KEY uk_revision_ver (content_id, version),
  INDEX idx_revision_content (content_id, version DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='正文修订快照表';
