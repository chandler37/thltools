-- MySQL dump 8.23
--
-- Host: localhost    Database: Lex
---------------------------------------------------------
-- Server version	3.23.58-max-log

--
-- Table structure for table `AnalyticalNotes`
--

CREATE TABLE AnalyticalNotes (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  analyticalNote text,
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Authors`
--

CREATE TABLE Authors (
  id int(11) NOT NULL auto_increment,
  author mediumtext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Definitions`
--

CREATE TABLE Definitions (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) NOT NULL default '0',
  definition text,
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Dialects`
--

CREATE TABLE Dialects (
  id int(11) NOT NULL auto_increment,
  majorDialectFamily smallint(6) NOT NULL default '0',
  specificDialect smallint(6) NOT NULL default '0',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `EncyclopediaArticles`
--

CREATE TABLE EncyclopediaArticles (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  article text NOT NULL,
  articleTitle text NOT NULL,
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Etymologies`
--

CREATE TABLE Etymologies (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  loanLanguage smallint(6) default NULL,
  etymologyType smallint(6) NOT NULL default '0',
  derivation varchar(255) NOT NULL default '',
  etymologyDescription text NOT NULL,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `EtymologyTypes`
--

CREATE TABLE EtymologyTypes (
  id int(11) NOT NULL auto_increment,
  etymologyType varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Functions`
--

CREATE TABLE Functions (
  id int(11) NOT NULL auto_increment,
  functionsGeneral smallint(6) NOT NULL default '0',
  functionsSpecific smallint(6) NOT NULL default '0',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `FunctionsGeneral`
--

CREATE TABLE FunctionsGeneral (
  id int(11) NOT NULL auto_increment,
  functionGeneral varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `FunctionsSpecific`
--

CREATE TABLE FunctionsSpecific (
  id int(11) NOT NULL auto_increment,
  functionSpecific varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Glosses`
--

CREATE TABLE Glosses (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  gloss text,
  translation text,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `GrammaticalFunctions`
--

CREATE TABLE GrammaticalFunctions (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  function smallint(6) NOT NULL default '0',
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `Keywords`
--

CREATE TABLE Keywords (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  keyword text,
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Languages`
--

CREATE TABLE Languages (
  id int(11) NOT NULL auto_increment,
  language varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `LiteraryForms`
--

CREATE TABLE LiteraryForms (
  id int(11) NOT NULL auto_increment,
  literaryForm mediumtext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `LiteraryGenres`
--

CREATE TABLE LiteraryGenres (
  id int(11) NOT NULL auto_increment,
  literaryGenre mediumtext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `LiteraryPeriods`
--

CREATE TABLE LiteraryPeriods (
  id int(11) NOT NULL auto_increment,
  literaryPeriod mediumtext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `LiteraryQuotations`
--

CREATE TABLE LiteraryQuotations (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  literarySource text,
  spelling text,
  pagination text,
  passage text,
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `LiterarySources`
--

CREATE TABLE LiterarySources (
  id int(11) NOT NULL auto_increment,
  literaryPeriod int(11) default NULL,
  literaryGenre int(11) default NULL,
  literaryForm int(11) default NULL,
  author int(11) default NULL,
  sourceNormalizedTitle mediumtext,
  tibetanDate mediumtext,
  internationalDate mediumtext,
  edition mediumtext,
  publisher mediumtext,
  isbn mediumtext,
  yearPublished timestamp(14) NOT NULL,
  volumeNumber int(11) NOT NULL default '0',
  pagination mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `MajorDialectFamilies`
--

CREATE TABLE MajorDialectFamilies (
  id int(11) NOT NULL auto_increment,
  majorDialectFamily mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Meta`
--

CREATE TABLE Meta (
  metaId int(11) NOT NULL auto_increment,
  translationOf int(11) default NULL,
  deleted tinyint(1) NOT NULL default '0',
  createdBy int(11) NOT NULL default '0',
  modifiedBy int(11) NOT NULL default '0',
  createdByProjSub int(11) NOT NULL default '0',
  modifiedByProjSub int(11) NOT NULL default '0',
  createdOn datetime default NULL,
  modifiedOn datetime default NULL,
  source int(11) NOT NULL default '0',
  language smallint(6) NOT NULL default '0',
  script smallint(6) NOT NULL default '0',
  dialect smallint(6) NOT NULL default '0',
  note text,
  PRIMARY KEY  (metaId),
  KEY translationOf (translationOf)
) TYPE=MyISAM;

--
-- Table structure for table `ModelSentences`
--

CREATE TABLE ModelSentences (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  subdefinitionId int(11) NOT NULL default '0',
  modelSentence text,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `PhoneticsTypes`
--

CREATE TABLE PhoneticsTypes (
  id int(11) NOT NULL auto_increment,
  phoneticsType varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Preferences`
--

CREATE TABLE Preferences (
  id int(11) NOT NULL auto_increment,
  userId int(11) NOT NULL default '0',
  preferencesName int(11) default NULL,
  projectSubject int(11) default NULL,
  source int(11) default NULL,
  language int(11) default NULL,
  script int(11) default NULL,
  dialect int(11) default NULL,
  note mediumtext,
  useDefaultProjSub varchar(5) NOT NULL default 'false',
  useDefaultSource varchar(5) NOT NULL default 'false',
  useDefaultLanguage varchar(5) NOT NULL default 'false',
  useDefaultScript varchar(5) NOT NULL default 'false',
  useDefaultDialect varchar(5) NOT NULL default 'false',
  useDefaultNote varchar(5) NOT NULL default 'false',
  projectSubjectSet varchar(255) default NULL,
  sourceSet varchar(255) default NULL,
  languageSet varchar(255) default NULL,
  scriptSet varchar(255) default NULL,
  dialectSet varchar(255) default NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `ProjectSubjects`
--

CREATE TABLE ProjectSubjects (
  id int(11) NOT NULL auto_increment,
  projectSubject varchar(255) default NULL,
  leader int(11) NOT NULL default '0',
  participantList mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Pronunciations`
--

CREATE TABLE Pronunciations (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  phonetics text NOT NULL,
  phoneticsType smallint(6) NOT NULL default '0',
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `Registers`
--

CREATE TABLE Registers (
  id int(11) NOT NULL auto_increment,
  register mediumtext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `RelatedTermTypes`
--

CREATE TABLE RelatedTermTypes (
  id int(11) NOT NULL auto_increment,
  relatedTermType varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `RelatedTerms`
--

CREATE TABLE RelatedTerms (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  relatedTerm text,
  relatedTermType smallint(6) NOT NULL default '0',
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Scripts`
--

CREATE TABLE Scripts (
  id int(11) NOT NULL auto_increment,
  script varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Sources`
--

CREATE TABLE Sources (
  id int(11) NOT NULL auto_increment,
  sourceTitle mediumtext NOT NULL,
  sourceDescription mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `SpecificDialects`
--

CREATE TABLE SpecificDialects (
  id int(11) NOT NULL auto_increment,
  specificDialect mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `SpeechRegisters`
--

CREATE TABLE SpeechRegisters (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  register smallint(6) NOT NULL default '0',
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `SpellingTypes`
--

CREATE TABLE SpellingTypes (
  id int(11) NOT NULL auto_increment,
  spellingType varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `Spellings`
--

CREATE TABLE Spellings (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  spelling varchar(255) NOT NULL default '',
  spellingType smallint(6) NOT NULL default '0',
  PRIMARY KEY  (metaId),
  KEY parentId (parentId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `Subdefinitions`
--

CREATE TABLE Subdefinitions (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  subdefinition text,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `Terms`
--

CREATE TABLE Terms (
  metaId int(11) NOT NULL default '0',
  term varchar(255) NOT NULL default '',
  precedence smallint(6) default NULL,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId)
) TYPE=MyISAM;

--
-- Table structure for table `TransitionalData`
--

CREATE TABLE TransitionalData (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  transitionalDataLabel smallint(6) default NULL,
  forPublicConsumption varchar(5) NOT NULL default '',
  transitionalDataText text,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `TransitionalDataLabels`
--

CREATE TABLE TransitionalDataLabels (
  id int(11) NOT NULL auto_increment,
  transitionalDataLabel mediumtext,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `TranslationEquivalents`
--

CREATE TABLE TranslationEquivalents (
  metaId int(11) NOT NULL default '0',
  parentId int(11) default NULL,
  precedence smallint(6) default NULL,
  translationEquivalent text,
  PRIMARY KEY  (metaId),
  KEY metaId (metaId),
  KEY parentId (parentId)
) TYPE=MyISAM;

--
-- Table structure for table `Users`
--

CREATE TABLE Users (
  id int(11) NOT NULL auto_increment,
  metaId int(11) NOT NULL default '0',
  userRoleList varchar(255) default NULL,
  username varchar(255) NOT NULL default '',
  password varchar(255) NOT NULL default '',
  firstname varchar(255) NOT NULL default '',
  lastname varchar(255) NOT NULL default '',
  middlename varchar(255) NOT NULL default '',
  title varchar(255) NOT NULL default '',
  email varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

