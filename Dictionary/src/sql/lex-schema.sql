drop table if exists FunctionsGeneral;
drop table if exists LiteraryPeriods;
drop table if exists Registers;
drop table if exists Pronunciations;
drop table if exists Authors;
drop table if exists Keywords;
drop table if exists Glosses;
drop table if exists RelatedTerms;
drop table if exists PhoneticsTypes;
drop table if exists Etymologies;
drop table if exists Meta;
drop table if exists SpellingTypes;
drop table if exists Dialects;
drop table if exists TransitionalData;
drop table if exists LiterarySources;
drop table if exists Spellings;
drop table if exists AnalyticalNotes;
drop table if exists EtymologyTypes;
drop table if exists SpeechRegisters;
drop table if exists Definitions;
drop table if exists FunctionsSpecific;
drop table if exists RelatedTermTypes;
drop table if exists ProjectSubjects;
drop table if exists ModelSentences;
drop table if exists Subdefinitions;
drop table if exists LiteraryGenres;
drop table if exists GrammaticalFunctions;
drop table if exists Functions;
drop table if exists SpecificDialects;
drop table if exists Sources;
drop table if exists TransitionalDataLabels;
drop table if exists LiteraryForms;
drop table if exists EncyclopediaArticles;
drop table if exists LiteraryQuotations;
drop table if exists MajorDialectFamilies;
drop table if exists Terms;
drop table if exists Scripts;
drop table if exists Languages;
drop table if exists TranslationEquivalents;
create table FunctionsGeneral (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   functionGeneral VARCHAR(255) not null,
   primary key (id)
);
create table LiteraryPeriods (
   id INTEGER NOT NULL AUTO_INCREMENT,
   literaryPeriod TEXT not null,
   primary key (id)
);
create table Registers (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   register TEXT not null,
   primary key (id)
);
create table Pronunciations (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   phonetics TEXT not null,
   phoneticsType SMALLINT not null,
   primary key (metaId)
);
create table Authors (
   id INTEGER NOT NULL AUTO_INCREMENT,
   author TEXT not null,
   primary key (id)
);
create table Keywords (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   keyword TEXT,
   primary key (metaId)
);
create table Glosses (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   gloss TEXT,
   translation TEXT,
   primary key (metaId)
);
create table RelatedTerms (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   relatedTerm TEXT,
   relatedTermType SMALLINT not null,
   primary key (metaId)
);
create table PhoneticsTypes (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   phoneticsType VARCHAR(255) not null,
   primary key (id)
);
create table Etymologies (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   loanLanguage SMALLINT,
   etymologyType SMALLINT not null,
   derivation VARCHAR(255) not null,
   etymologyDescription TEXT not null,
   primary key (metaId)
);
create table Meta (
   metaId INTEGER NOT NULL AUTO_INCREMENT,
   translationOf INTEGER,
   deleted BIT not null,
   createdBy INTEGER not null,
   modifiedBy INTEGER not null,
   createdByProjSub INTEGER not null,
   modifiedByProjSub INTEGER not null,
   createdOn DATETIME,
   modifiedOn DATETIME,
   source INTEGER not null,
   language SMALLINT not null,
   script SMALLINT not null,
   dialect SMALLINT not null,
   note TEXT,
   primary key (metaId)
);
create table SpellingTypes (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   spellingType VARCHAR(255) not null,
   primary key (id)
);
create table Dialects (
   id INTEGER NOT NULL AUTO_INCREMENT,
   majorDialectFamily SMALLINT not null,
   specificDialect SMALLINT not null,
   primary key (id)
);
create table TransitionalData (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   transitionalDataLabel SMALLINT,
   forPublicConsumption VARCHAR(5) not null,
   transitionalDataText TEXT,
   primary key (metaId)
);
create table LiterarySources (
   id INTEGER NOT NULL AUTO_INCREMENT,
   literaryPeriod INTEGER,
   literaryGenre INTEGER,
   literaryForm INTEGER,
   author INTEGER,
   sourceNormalizedTitle TEXT,
   tibetanDate TEXT,
   internationalDate TEXT,
   edition TEXT,
   publisher TEXT,
   isbn TEXT,
   yearPublished DATETIME,
   volumeNumber INTEGER not null,
   pagination TEXT,
   primary key (id)
);
create table Spellings (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   spelling VARCHAR(255) not null,
   spellingType SMALLINT not null,
   primary key (metaId)
);
create table AnalyticalNotes (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   analyticalNote TEXT,
   primary key (metaId)
);
create table EtymologyTypes (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   etymologyType VARCHAR(255) not null,
   primary key (id)
);
create table SpeechRegisters (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   register SMALLINT not null,
   primary key (metaId)
);
create table Definitions (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT not null,
   definition TEXT,
   primary key (metaId)
);
create table FunctionsSpecific (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   functionSpecific VARCHAR(255) not null,
   primary key (id)
);
create table RelatedTermTypes (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   relatedTermType VARCHAR(255) not null,
   primary key (id)
);
create table ProjectSubjects (
   id INTEGER NOT NULL AUTO_INCREMENT,
   projectSubject VARCHAR(255),
   leader INTEGER not null,
   participantList TEXT,
   primary key (id)
);
create table ModelSentences (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   subdefinitionId INTEGER not null,
   modelSentence TEXT,
   primary key (metaId)
);
create table Subdefinitions (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   subdefinition TEXT,
   primary key (metaId)
);
create table LiteraryGenres (
   id INTEGER NOT NULL AUTO_INCREMENT,
   literaryGenre TEXT not null,
   primary key (id)
);
create table GrammaticalFunctions (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   function SMALLINT not null,
   primary key (metaId)
);
create table Functions (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   functionsGeneral SMALLINT not null,
   functionsSpecific SMALLINT not null,
   primary key (id)
);
create table SpecificDialects (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   specificDialect TEXT,
   primary key (id)
);
create table Sources (
   id INTEGER NOT NULL AUTO_INCREMENT,
   sourceTitle TEXT not null,
   sourceDescription TEXT,
   primary key (id)
);
create table TransitionalDataLabels (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   transitionalDataLabel TEXT,
   primary key (id)
);
create table LiteraryForms (
   id INTEGER NOT NULL AUTO_INCREMENT,
   literaryForm TEXT not null,
   primary key (id)
);
create table EncyclopediaArticles (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   article TEXT not null,
   articleTitle TEXT not null,
   primary key (metaId)
);
create table LiteraryQuotations (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   literarySource TEXT,
   spelling TEXT,
   pagination TEXT,
   passage TEXT,
   primary key (metaId)
);
create table MajorDialectFamilies (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   majorDialectFamily TEXT,
   primary key (id)
);
create table Terms (
   metaId INTEGER not null,
   term VARCHAR(255) not null,
   precedence SMALLINT,
   primary key (metaId)
);
create table Scripts (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   script VARCHAR(255) not null,
   primary key (id)
);
create table Languages (
   id SMALLINT NOT NULL AUTO_INCREMENT,
   language VARCHAR(255) not null,
   primary key (id)
);
create table TranslationEquivalents (
   metaId INTEGER not null,
   parentId INTEGER,
   precedence SMALLINT,
   translationEquivalent TEXT,
   primary key (metaId)
);
alter table Pronunciations add index (metaId), add constraint FK1889BDEBFC5B200 foreign key (metaId) references Meta (metaId);
alter table Pronunciations add index (parentId), add constraint FK1889BDE460B8F65 foreign key (parentId) references Terms (metaId);
alter table Keywords add index (parentId), add constraint FK230903CA460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table Keywords add index (metaId), add constraint FK230903CABFC5B200 foreign key (metaId) references Meta (metaId);
alter table Glosses add index (metaId), add constraint FK6A780618BFC5B200 foreign key (metaId) references Meta (metaId);
alter table Glosses add index (parentId), add constraint FK6A780618460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table RelatedTerms add index (parentId), add constraint FK125CF43C460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table RelatedTerms add index (metaId), add constraint FK125CF43CBFC5B200 foreign key (metaId) references Meta (metaId);
alter table Etymologies add index (metaId), add constraint FKD08FB0BFBFC5B200 foreign key (metaId) references Meta (metaId);
alter table Etymologies add index (parentId), add constraint FKD08FB0BF460B8F65 foreign key (parentId) references Terms (metaId);
alter table Meta add index (translationOf), add constraint FK248A2527981468 foreign key (translationOf) references Meta (metaId);
alter table TransitionalData add index (metaId), add constraint FK9204002ABFC5B200 foreign key (metaId) references Meta (metaId);
alter table TransitionalData add index (parentId), add constraint FK9204002A460B8F65 foreign key (parentId) references Terms (metaId);
alter table Spellings add index (parentId), add constraint FK21F7B1D9460B8F65 foreign key (parentId) references Terms (metaId);
alter table Spellings add index (metaId), add constraint FK21F7B1D9BFC5B200 foreign key (metaId) references Meta (metaId);
alter table AnalyticalNotes add index (parentId), add constraint FK5226AE09460B8F65 foreign key (parentId) references Meta (metaId);
alter table AnalyticalNotes add index (metaId), add constraint FK5226AE09BFC5B200 foreign key (metaId) references Meta (metaId);
alter table SpeechRegisters add index (metaId), add constraint FK6456534EBFC5B200 foreign key (metaId) references Meta (metaId);
alter table SpeechRegisters add index (parentId), add constraint FK6456534E460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table Definitions add index (parentId), add constraint FK11071D60460B8F65 foreign key (parentId) references Terms (metaId);
alter table Definitions add index (metaId), add constraint FK11071D60BFC5B200 foreign key (metaId) references Meta (metaId);
alter table ModelSentences add index (metaId), add constraint FKBDC96567BFC5B200 foreign key (metaId) references Meta (metaId);
alter table ModelSentences add index (parentId), add constraint FKBDC96567460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table Subdefinitions add index (metaId), add constraint FKDC691B60BFC5B200 foreign key (metaId) references Meta (metaId);
alter table Subdefinitions add index (parentId), add constraint FKDC691B60460B8F65 foreign key (parentId) references Definitions (metaId);
alter table GrammaticalFunctions add index (metaId), add constraint FKCD3F816DBFC5B200 foreign key (metaId) references Meta (metaId);
alter table GrammaticalFunctions add index (parentId), add constraint FKCD3F816D460B8F65 foreign key (parentId) references Terms (metaId);
alter table EncyclopediaArticles add index (parentId), add constraint FKAC14CB5D460B8F65 foreign key (parentId) references Terms (metaId);
alter table EncyclopediaArticles add index (metaId), add constraint FKAC14CB5DBFC5B200 foreign key (metaId) references Meta (metaId);
alter table LiteraryQuotations add index (parentId), add constraint FK99D4FE0B460B8F65 foreign key (parentId) references Subdefinitions (metaId);
alter table LiteraryQuotations add index (metaId), add constraint FK99D4FE0BBFC5B200 foreign key (metaId) references Meta (metaId);
alter table Terms add index (metaId), add constraint FK4CF5967BFC5B200 foreign key (metaId) references Meta (metaId);
alter table TranslationEquivalents add index (metaId), add constraint FK27FEF3F8BFC5B200 foreign key (metaId) references Meta (metaId);
alter table TranslationEquivalents add index (parentId), add constraint FK27FEF3F8460B8F65 foreign key (parentId) references Subdefinitions (metaId);
