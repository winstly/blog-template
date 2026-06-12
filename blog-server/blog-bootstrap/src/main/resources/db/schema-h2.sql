-- H2-compatible DDL for dev environment
-- Differences from MySQL: no JSON, no TIMESTAMP ON UPDATE, no FULLTEXT, MEDIUMTEXT→CLOB

-- 1. sys_dict
CREATE TABLE IF NOT EXISTS sys_dict (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  biz_code        VARCHAR(64)  NOT NULL DEFAULT '',
  sub_code        VARCHAR(64)  NOT NULL DEFAULT '',
  item_code       VARCHAR(64)  NOT NULL DEFAULT '',
  item_label      VARCHAR(128) NOT NULL DEFAULT '',
  item_value      VARCHAR(512) NOT NULL DEFAULT '',
  ext_data        CLOB         DEFAULT NULL,
  sort_order      INT          NOT NULL DEFAULT 0,
  display_status  TINYINT      NOT NULL DEFAULT 1,
  remark          VARCHAR(256) DEFAULT NULL,

  PRIMARY KEY (id),
  CONSTRAINT uk_dict_item UNIQUE (biz_code, sub_code, item_code)
);

-- 2. blog_tag
CREATE TABLE IF NOT EXISTS blog_tag (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  tag_code        VARCHAR(64)  NOT NULL DEFAULT '',
  tag_name        VARCHAR(64)  NOT NULL DEFAULT '',
  tree_path       VARCHAR(512) DEFAULT NULL,
  tree_depth      TINYINT      NOT NULL DEFAULT 0,
  sort_order      INT          NOT NULL DEFAULT 0,
  display_status  TINYINT      NOT NULL DEFAULT 1,
  description     VARCHAR(256) DEFAULT NULL,

  PRIMARY KEY (id),
  CONSTRAINT uk_tag_code UNIQUE (tag_code)
);

-- 3. blog_article
CREATE TABLE IF NOT EXISTS blog_article (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  article_code    VARCHAR(128) NOT NULL DEFAULT '',
  title           VARCHAR(256) NOT NULL DEFAULT '',
  summary         VARCHAR(512) DEFAULT NULL,
  cover_url       VARCHAR(512) DEFAULT NULL,
  is_pinned       TINYINT      NOT NULL DEFAULT 0,
  view_count      INT          NOT NULL DEFAULT 0,
  published_at    TIMESTAMP    NULL DEFAULT NULL,

  PRIMARY KEY (id),
  CONSTRAINT uk_article_code UNIQUE (article_code)
);

-- 4. blog_article_tag
CREATE TABLE IF NOT EXISTS blog_article_tag (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  article_code    VARCHAR(128) NOT NULL DEFAULT '',
  tag_code        VARCHAR(64)  NOT NULL DEFAULT '',
  relation_type   VARCHAR(16)  NOT NULL DEFAULT 'tag',

  PRIMARY KEY (id),
  CONSTRAINT uk_article_tag UNIQUE (article_code, tag_code, relation_type)
);

-- 5. blog_interaction
CREATE TABLE IF NOT EXISTS blog_interaction (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  user_name       VARCHAR(64)  NOT NULL DEFAULT '',
  user_avatar_url VARCHAR(512) DEFAULT NULL,
  target_type     VARCHAR(32)  NOT NULL DEFAULT '',
  target_code     VARCHAR(128) NOT NULL DEFAULT '',
  action_type     VARCHAR(16)  NOT NULL DEFAULT '',
  remark          VARCHAR(512) DEFAULT NULL,
  tree_path       VARCHAR(512) DEFAULT NULL,
  tree_depth      TINYINT      NOT NULL DEFAULT 0,
  ext_meta        CLOB         DEFAULT NULL,
  display_status  TINYINT      NOT NULL DEFAULT 1,

  PRIMARY KEY (id)
);

-- 6. blog_content
CREATE TABLE IF NOT EXISTS blog_content (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  target_type     VARCHAR(32)  NOT NULL DEFAULT '',
  target_code     VARCHAR(128) NOT NULL DEFAULT '',
  body            CLOB         NOT NULL,
  content_format  VARCHAR(16)  NOT NULL DEFAULT 'markdown',
  word_count      INT          NOT NULL DEFAULT 0,
  version         INT          NOT NULL DEFAULT 1,
  publish_status  TINYINT      NOT NULL DEFAULT 0,

  PRIMARY KEY (id),
  CONSTRAINT uk_content_target UNIQUE (target_type, target_code)
);

-- 7. blog_content_revision
CREATE TABLE IF NOT EXISTS blog_content_revision (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,

  content_id      BIGINT       NOT NULL,
  body            CLOB         NOT NULL,
  content_format  VARCHAR(16)  NOT NULL DEFAULT 'markdown',
  word_count      INT          NOT NULL DEFAULT 0,
  version         INT          NOT NULL,
  change_summary  VARCHAR(256) DEFAULT NULL,

  PRIMARY KEY (id),
  CONSTRAINT uk_revision_ver UNIQUE (content_id, version)
);

-- 8. blog_diary
CREATE TABLE IF NOT EXISTS blog_diary (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  creator         VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_create      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  modifier        VARCHAR(64)  NOT NULL DEFAULT '',
  gmt_modified    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_deleted      TINYINT      NOT NULL DEFAULT 0,
  diary_code      VARCHAR(128) NOT NULL DEFAULT '',
  content         CLOB         NOT NULL,
  images          CLOB         DEFAULT NULL,
  diary_date      DATE         NOT NULL,
  year            INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_diary_code UNIQUE (diary_code)
);
