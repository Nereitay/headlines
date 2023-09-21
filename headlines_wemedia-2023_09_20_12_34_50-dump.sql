-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: headlines_wemedia
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `wm_channel`
--

DROP TABLE IF EXISTS `wm_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_channel` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '频道描述',
  `is_default` tinyint unsigned DEFAULT NULL COMMENT '是否默认频道',
  `status` tinyint unsigned DEFAULT NULL,
  `ord` tinyint unsigned DEFAULT NULL COMMENT '默认排序',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='频道信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_channel`
--

LOCK TABLES `wm_channel` WRITE;
/*!40000 ALTER TABLE `wm_channel` DISABLE KEYS */;
INSERT INTO `wm_channel` VALUES (1,'java','后端框架',1,1,1,'2021-04-18 12:25:30'),(2,'Mysql','轻量级数据库',1,1,4,'2021-04-18 10:55:41'),(3,'Vue','阿里前端框架',1,1,5,'2021-04-18 10:55:41'),(4,'Python','未来的语言',1,1,6,'2021-04-18 10:55:41'),(5,'Weex','向未来致敬',1,1,7,'2021-04-18 10:55:41'),(6,'大数据','不会，则不要说自己是搞互联网的',1,1,10,'2021-04-18 10:55:41'),(7,'其他','其他',1,1,12,'2021-04-18 10:55:41');
/*!40000 ALTER TABLE `wm_channel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_fans_statistics`
--

DROP TABLE IF EXISTS `wm_fans_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_fans_statistics` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned DEFAULT NULL COMMENT '主账号ID',
  `article` int unsigned DEFAULT NULL COMMENT '子账号ID',
  `read_count` int unsigned DEFAULT NULL,
  `comment` int unsigned DEFAULT NULL,
  `follow` int unsigned DEFAULT NULL,
  `collection` int unsigned DEFAULT NULL,
  `forward` int unsigned DEFAULT NULL,
  `likes` int unsigned DEFAULT NULL,
  `unlikes` int unsigned DEFAULT NULL,
  `unfollow` int unsigned DEFAULT NULL,
  `burst` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_user_id_time` (`user_id`,`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体粉丝数据统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_fans_statistics`
--

LOCK TABLES `wm_fans_statistics` WRITE;
/*!40000 ALTER TABLE `wm_fans_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `wm_fans_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_material`
--

DROP TABLE IF EXISTS `wm_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_material` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned DEFAULT NULL COMMENT '自媒体用户ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片地址',
  `type` tinyint unsigned DEFAULT NULL COMMENT '素材类型\r\n            0 图片\r\n            1 视频',
  `is_collection` tinyint(1) DEFAULT NULL COMMENT '是否收藏',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体图文素材信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_material`
--

LOCK TABLES `wm_material` WRITE;
/*!40000 ALTER TABLE `wm_material` DISABLE KEYS */;
INSERT INTO `wm_material` VALUES (67,1102,'http://192.168.1.150:9000/headlines/2021/04/26/a73f5b60c0d84c32bfe175055aaaac40.jpg',0,0,'2021-04-26 00:14:01'),(68,1102,'http://192.168.1.150:9000/headlines/2021/04/26/d4f6ef4c0c0546e69f70bd3178a8c140.jpg',0,0,'2021-04-26 00:18:20'),(69,1102,'http://192.168.1.150:9000/headlines/2021/04/26/5ddbdb5c68094ce393b08a47860da275.jpg',0,0,'2021-04-26 00:18:27'),(70,1102,'http://192.168.1.150:9000/headlines/2021/04/26/9f8a93931ab646c0a754475e0c4b0a98.jpg',0,0,'2021-04-26 00:18:34'),(71,1102,'http://192.168.1.150:9000/headlines/2021/04/26/ef3cbe458db249f7bd6fb4339e593e55.jpg',0,0,'2021-04-26 00:18:39');
/*!40000 ALTER TABLE `wm_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_news`
--

DROP TABLE IF EXISTS `wm_news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_news` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned DEFAULT NULL COMMENT '自媒体用户ID',
  `title` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '图文内容',
  `type` tinyint unsigned DEFAULT NULL COMMENT '文章布局\r\n            0 无图文章\r\n            1 单图文章\r\n            3 多图文章',
  `channel_id` int unsigned DEFAULT NULL COMMENT '图文频道ID',
  `labels` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `submited_time` datetime DEFAULT NULL COMMENT '提交时间',
  `status` tinyint unsigned DEFAULT NULL COMMENT '当前状态\r\n            0 草稿\r\n            1 提交（待审核）\r\n            2 审核失败\r\n            3 人工审核\r\n            4 人工审核通过\r\n            8 审核通过（待发布）\r\n            9 已发布',
  `publish_time` datetime DEFAULT NULL COMMENT '定时发布时间，不定时则为空',
  `reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拒绝理由',
  `article_id` bigint unsigned DEFAULT NULL COMMENT '发布库文章ID',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '//图片用逗号分隔',
  `enable` tinyint unsigned DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6232 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体图文内容信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_news`
--

LOCK TABLES `wm_news` WRITE;
/*!40000 ALTER TABLE `wm_news` DISABLE KEYS */;
INSERT INTO `wm_news` VALUES (6225,1102,'“真”项目课程对找工作有什么帮助？','[{\"type\":\"text\",\"value\":\"找工作，企业重点问的是项目经验，更是HR筛选的“第一门槛”，直接决定了你是否有机会进入面试环节。\\n\\n　　项目经验更是评定“个人能力/技能”真实性的“证据”，反映了求职者某个方面的实际动手能力、对某个领域或某种技能的掌握程度。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/7d0911a41a3745efa8509a87f234813c.jpg\"},{\"type\":\"text\",\"value\":\"很多经过培训期望快速上岗的程序员，靠着培训机构“辅导”顺利经过面试官对于“项目经验”的考核上岗后，在面对“有限时间”“复杂业务”“新项目需求”等多项标签加持的工作任务，却往往不知从何下手或开发进度极其缓慢。最终结果就是：熬不过试用期。\\n\\n　　从而也引发了企业对于“培训出身程序员”的“有色眼光”。你甚至也一度怀疑“IT培训班出来的人真的不行吗?”\"}]',1,1,'项目课程','2021-04-19 00:08:10','2021-04-19 00:08:10',9,'2021-04-19 00:08:05','审核成功',1383828014629179393,'http://192.168.1.150:9000/headlines/2021/4/20210418/7d0911a41a3745efa8509a87f234813c.jpg',1),(6226,1102,'学IT，为什么要学项目课程？','[{\"type\":\"text\",\"value\":\"在选择IT培训机构时，你应该有注意到，很多机构都将“项目课程”作为培训中的重点。那么，为什么要学习项目课程?为什么项目课程才是IT培训课程的核心?\\n\\n　　1\\n\\n　　在这个靠“技术经验说话”的IT行业里，假如你是一个计算机或IT相关专业毕业生，在没有实际项目开发经验的情况下，“找到第一份全职工作”可能是你职业生涯中遇到的最大挑战。\\n\\n　　为什么说找第一份工作很难?\\n\\n　　主要在于：实际企业中用到的软件开发知识和在学校所学的知识是完全不同的。假设你已经在学校和同学做过周期长达2-3个月的项目，但真正工作中的团队协作与你在学校中经历的协作也有很多不同。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/e8113ad756a64ea6808f91130a6cd934.jpg\"},{\"type\":\"text\",\"value\":\"在实际团队中，每一位成员彼此团结一致，为项目的交付而努力，这也意味着你必须要理解好在项目中负责的那部分任务，在规定时间交付还需确保你负责的功能，在所有环境中都能很好地发挥作用，而不仅仅是你的本地机器。\\n\\n　　这需要你对项目中的每一行代码严谨要求。学校练习的项目中，对bug的容忍度很大，而在实际工作中是绝对不能容忍的。项目中的任何一个环节都涉及公司利益，任何一个bug都可能影响公司的收入及形象。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/c7c3d36d25504cf6aecdcd5710261773.jpg\"}]',3,1,'项目课程','2021-04-19 00:13:58','2021-04-19 00:13:58',9,'2021-04-19 00:10:48','审核成功',1383827995813531650,'http://192.168.1.150:9000/headlines/2021/4/20210418/7d0911a41a3745efa8509a87f234813c.jpg,http://192.168.1.150:9000/headlines/2021/4/20210418/c7c3d36d25504cf6aecdcd5710261773.jpg,http://192.168.1.150:9000/headlines/2021/4/20210418/e8113ad756a64ea6808f91130a6cd934.jpg',1),(6227,1102,'小白如何辨别其真与伪&好与坏？','[{\"type\":\"text\",\"value\":\"通过上篇《IT培训就业艰难，行业乱象频发，如何破解就业难题?》一文，相信大家已初步了解“项目课程”对程序员能否就业且高薪就业的重要性。\\n\\n　　因此，小白在选择IT培训机构时，关注的重点就在于所学“项目课程”能否真正帮你增加就业筹码。当然，前提必须是学到“真”项目。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/1818283261e3401892e1383c1bd00596.jpg\"}]',1,1,'小白','2021-04-19 00:15:05','2021-04-19 00:15:05',9,'2021-04-19 00:14:58','审核成功',1383827976310018049,'http://192.168.1.150:9000/headlines/2021/4/20210418/1818283261e3401892e1383c1bd00596.jpg',1),(6228,1102,'工作线程数是不是设置的越大越好','[{\"type\":\"text\",\"value\":\"根据经验来看，jdk api 一般推荐的线程数为CPU核数的2倍。但是有些书籍要求可以设置为CPU核数的8倍，也有的业务设置为CPU核数的32倍。\\n“工作线程数”的设置依据是什么，到底设置为多少能够最大化CPU性能，是本文要讨论的问题。\\n\\n工作线程数是不是设置的越大越好\\n显然不是的。使用java.lang.Thread类或者java.lang.Runnable接口编写代码来定义、实例化和启动新线程。\\n一个Thread类实例只是一个对象，像Java中的任何其他对象一样，具有变量和方法，生死于堆上。\\nJava中，每个线程都有一个调用栈，即使不在程序中创建任何新的线程，线程也在后台运行着。\\n一个Java应用总是从main()方法开始运行，main()方法运行在一个线程内，它被称为主线程。\\n一旦创建一个新的线程，就产生一个新的调用栈。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/a3f0bc438c244f788f2df474ed8ecdc1.jpg\"}]',1,1,'11','2021-04-19 00:16:57','2021-04-19 00:16:57',9,'2021-04-19 00:16:52','审核成功',1383827952326987778,'http://192.168.1.150:9000/headlines/2021/4/20210418/a3f0bc438c244f788f2df474ed8ecdc1.jpg',1),(6229,1102,'Base64编解码原理','[{\"type\":\"text\",\"value\":\"我在面试过程中，问过很多高级java工程师，是否了解Base64？部分人回答了解，部分人直接回答不了解。而说了解的那部分人却回答不上来它的原理。\\n\\nBase64 的由来\\nBase64是网络上最常见的用于传输8Bit字节代码的编码方式之一，大家可以查看RFC2045～RFC2049，上面有MIME的详细规范。它是一种基于用64个可打印字符来表示二进制数据的表示方法。它通常用作存储、传输一些二进制数据编码方法！也是MIME（多用途互联网邮件扩展，主要用作电子邮件标准）中一种可打印字符表示二进制数据的常见编码方法！它其实只是定义用可打印字符传输内容一种方法，并不会产生新的字符集！\\n\\n传统的邮件只支持可见字符的传送，像ASCII码的控制字符就 不能通过邮件传送。这样用途就受到了很大的限制，比如图片二进制流的每个字节不可能全部是可见字符，所以就传送不了。最好的方法就是在不改变传统协议的情 况下，做一种扩展方案来支持二进制文件的传送。把不可打印的字符也能用可打印字符来表示，问题就解决了。Base64编码应运而生，Base64就是一种 基于64个可打印字符来表示二进制数据的表示方法。\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/b44c65376f12498e873223d9d6fdf523.jpg\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]',1,1,'11','2021-04-19 00:17:44','2021-04-19 00:17:44',9,'2021-04-19 00:17:42','审核成功',1383827911810011137,'http://192.168.1.150:9000/headlines/2021/4/20210418/b44c65376f12498e873223d9d6fdf523.jpg',1),(6230,1102,'为什么项目经理不喜欢重构？','[{\"type\":\"text\",\"value\":\"经常听到开发人员抱怨 ，“这么烂的代码，我来重构一下！”，“这代码怎么能这么写呢？谁来重构一下？”，“这儿有个坏味道，重构吧！”\\n\\n作为一名项目经理，每次听到“重构”两个字，既想给追求卓越代码的开发人员点个赞，同时又会感觉非常紧张，为什么又要重构？马上就要上线了，怎么还要改？是不是应该阻止开发人员做重构？\\n\\n重构几乎是开发人员最喜欢的一项实践了，可项目经理们却充满了顾虑，那么为什么项目经理不喜欢重构呢？\\n\\n老功能被破坏\\n不止一次遇到这样的场景，某一天一个老功能突然被破坏了，项目经理们感到奇怪，产品这块儿的功能已经很稳定了，也没有在这部分开发什么新功能，为什么突然出问题了呢？\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/e8113ad756a64ea6808f91130a6cd934.jpg\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/4a498d9cf3614570ac0cb2da3e51c164.jpg\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]',1,1,'11','2021-04-19 00:19:23','2021-04-19 00:19:23',9,'2021-04-19 00:19:09','审核成功',1383827888816836609,'http://192.168.1.150:9000/headlines/2021/4/20210418/4a498d9cf3614570ac0cb2da3e51c164.jpg',1),(6231,1102,'Kafka文件的存储机制','[{\"type\":\"text\",\"value\":\"Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制Kafka文件的存储机制\"},{\"type\":\"image\",\"value\":\"http://192.168.1.150:9000/headlines/2021/4/20210418/4a498d9cf3614570ac0cb2da3e51c164.jpg\"},{\"type\":\"text\",\"value\":\"请在这里输入正文\"}]',1,1,'11','2021-04-19 00:58:47','2021-04-19 00:58:47',9,'2021-04-19 00:20:17','审核成功',1383827787629252610,'http://192.168.1.150:9000/headlines/2021/4/20210418/4a498d9cf3614570ac0cb2da3e51c164.jpg',1);
/*!40000 ALTER TABLE `wm_news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_news_material`
--

DROP TABLE IF EXISTS `wm_news_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_news_material` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `material_id` int unsigned DEFAULT NULL COMMENT '素材ID',
  `news_id` int unsigned DEFAULT NULL COMMENT '图文ID',
  `type` tinyint unsigned DEFAULT NULL COMMENT '引用类型\r\n            0 内容引用\r\n            1 主图引用',
  `ord` tinyint unsigned DEFAULT NULL COMMENT '引用排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体图文引用素材信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_news_material`
--

LOCK TABLES `wm_news_material` WRITE;
/*!40000 ALTER TABLE `wm_news_material` DISABLE KEYS */;
INSERT INTO `wm_news_material` VALUES (255,61,6232,0,0),(256,61,6232,1,0),(263,61,6231,0,0),(264,61,6231,1,0),(265,57,6230,0,0),(266,61,6230,0,1),(267,61,6230,1,0),(268,58,6229,0,0),(269,58,6229,1,0),(270,62,6228,0,0),(271,62,6228,1,0),(272,66,6227,0,0),(273,66,6227,1,0),(274,57,6226,0,0),(275,64,6226,0,1),(276,65,6226,1,0),(277,64,6226,1,1),(278,57,6226,1,2),(279,65,6225,0,0),(280,65,6225,1,0);
/*!40000 ALTER TABLE `wm_news_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_news_statistics`
--

DROP TABLE IF EXISTS `wm_news_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_news_statistics` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int unsigned DEFAULT NULL COMMENT '主账号ID',
  `article` int unsigned DEFAULT NULL COMMENT '子账号ID',
  `read_count` int unsigned DEFAULT NULL COMMENT '阅读量',
  `comment` int unsigned DEFAULT NULL COMMENT '评论量',
  `follow` int unsigned DEFAULT NULL COMMENT '关注量',
  `collection` int unsigned DEFAULT NULL COMMENT '收藏量',
  `forward` int unsigned DEFAULT NULL COMMENT '转发量',
  `likes` int unsigned DEFAULT NULL COMMENT '点赞量',
  `unlikes` int unsigned DEFAULT NULL COMMENT '不喜欢',
  `unfollow` int unsigned DEFAULT NULL COMMENT '取消关注量',
  `burst` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_user_id_time` (`user_id`,`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体图文数据统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_news_statistics`
--

LOCK TABLES `wm_news_statistics` WRITE;
/*!40000 ALTER TABLE `wm_news_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `wm_news_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_sensitive`
--

DROP TABLE IF EXISTS `wm_sensitive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_sensitive` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sensitives` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '敏感词',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='敏感词信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_sensitive`
--

LOCK TABLES `wm_sensitive` WRITE;
/*!40000 ALTER TABLE `wm_sensitive` DISABLE KEYS */;
INSERT INTO `wm_sensitive` VALUES (3104,'冰毒','2021-05-23 15:38:51'),(3105,'法轮功','2021-05-23 15:38:51'),(3106,'私人侦探','2021-05-23 11:09:22'),(3107,'针孔摄象','2021-05-23 11:09:52'),(3108,'信用卡提现','2021-05-23 11:10:11'),(3109,'无抵押贷款','2021-05-23 11:10:41'),(3110,'广告代理','2021-05-23 11:10:59'),(3111,'代开发票','2021-05-23 11:11:18'),(3112,'蚁力神','2021-05-23 11:11:39'),(3113,'售肾','2021-05-23 11:12:08'),(3114,'刻章办','2021-05-23 11:12:24'),(3116,'套牌车','2021-05-23 11:12:37'),(3117,'足球投注','2021-05-23 11:12:51'),(3118,'地下钱庄','2021-05-23 11:13:07'),(3119,'出售答案','2021-05-23 11:13:24'),(3200,'小额贷款','2021-05-23 11:13:40');
/*!40000 ALTER TABLE `wm_sensitive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wm_user`
--

DROP TABLE IF EXISTS `wm_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wm_user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ap_user_id` int DEFAULT NULL,
  `ap_author_id` int DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录密码',
  `salt` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '盐',
  `nickname` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `location` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '归属地',
  `phone` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint unsigned DEFAULT NULL COMMENT '状态\r\n            0 暂时不可用\r\n            1 永久不可用\r\n            9 正常可用',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `type` tinyint unsigned DEFAULT NULL COMMENT '账号类型\r\n            0 个人 \r\n            1 企业\r\n            2 子账号',
  `score` tinyint unsigned DEFAULT NULL COMMENT '运营评分',
  `login_time` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='自媒体用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wm_user`
--

LOCK TABLES `wm_user` WRITE;
/*!40000 ALTER TABLE `wm_user` DISABLE KEYS */;
INSERT INTO `wm_user` VALUES (1100,NULL,NULL,'zhangsan','ab8c7c1e66a164ab6891b927550ea39a','abc','小张',NULL,NULL,'13588996789',1,NULL,NULL,NULL,'2020-02-17 23:51:15','2020-02-17 23:51:18'),(1101,NULL,NULL,'lisi','a6ecab0c246bbc87926e0fba442cc014','def','小李',NULL,NULL,'13234567656',1,NULL,NULL,NULL,NULL,NULL),(1102,NULL,NULL,'admin','a66abb5684c45962d887564f08346e8d','123456','管理',NULL,NULL,'13234567657',1,NULL,NULL,NULL,NULL,'2020-03-14 09:35:13'),(1118,NULL,NULL,'lisi1','123','123',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1119,NULL,NULL,'shaseng','1234',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `wm_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-09-20 12:34:50
