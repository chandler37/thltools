# phpMyAdmin SQL Dump
# version 2.5.3
# http://www.phpmyadmin.net
#
# Host: localhost
# Generation Time: Oct 05, 2003 at 10:27 PM
# Server version: 3.23.58
# PHP Version: 4.1.2
# 
# Database : `Lex`
# 

#
# Dumping data for table `Authors`
#

INSERT INTO Authors (id, author) VALUES (1, 'Kun-mkhyen KloÂ·n-chen-pa Dri-med-\'od-zer, 1308-1363');

#
# Dumping data for table `Dialects`
#


#
# Dumping data for table `EtymologyTypes`
#

INSERT INTO EtymologyTypes (id, etymologyType) VALUES (2, 'Historical (linguistic), bod yig med'),
(3, 'Creative, bod yig med'),
(1, 'Basic (syllabic), bod yig med');

#
# Dumping data for table `Functions`
#


#
# Dumping data for table `FunctionsGeneral`
#

INSERT INTO FunctionsGeneral (id, functionGeneral) VALUES (2, 'Adjective,bod yig med/'),
(3, 'Adverb,bod yig med/'),
(4, 'Article,bod yig med/'),
(5, 'Verb,bod yig med/'),
(6, 'Noun,bod yig med/'),
(7, 'Conjunction,bod yig med/'),
(8, 'Particle,bod yig med/'),
(9, 'Pronoun,bod yig med/'),
(10, 'Phrase,bod yig med/'),
(1, 'Case Markers,bod yig med/');

#
# Dumping data for table `FunctionsSpecific`
#

INSERT INTO FunctionsSpecific (id, functionSpecific) VALUES (2, 'Absolutive,bod yig med/'),
(3, 'Ergative,bod yig med/'),
(4, 'Genitive,bod yig med/'),
(5, 'Locative,bod yig med/'),
(6, 'Oblique,bod yig med/'),
(7, 'Superlative,bod yig med/'),
(8, 'Comparative,bod yig med/'),
(9, 'Absolute,bod yig med/'),
(10, 'Quantifier,bod yig med/'),
(11, 'Determinant,bod yig med/'),
(12, 'Auxiliary,bod yig med/'),
(13, 'Causative,bod yig med/'),
(14, 'Modal,bod yig med/'),
(15, 'Resultative,bod yig med/'),
(16, 'Volitional,bod yig med/'),
(17, 'Present,bod yig med/'),
(18, 'Past,bod yig med/'),
(19, 'Future,bod yig med/'),
(20, 'Imperative,bod yig med/'),
(21, 'Proper,bod yig med/'),
(22, 'auxiliary,bod yig med/'),
(23, 'Connective,bod yig med/'),
(24, 'Disjunctive,bod yig med/'),
(25, 'Personal,bod yig med/'),
(26, 'Demonstrative,bod yig med/'),
(27, 'Interrogative,bod yig med/'),
(28, 'Verb Phrase,bod yig med/'),
(29, 'Noun Phrase,bod yig med/'),
(30, 'Postpositional,bod yig med/'),
(31, 'Negative,bod yig med/'),
(32, 'Finalizing,bod yig med/'),
(33, 'Interrogative,bod yig med/'),
(34, 'Pluralizers,bod yig med/'),
(35, 'Expressive,bod yig med/'),
(1, 'Ablative,bod yig med/');

#
# Dumping data for table `Languages`
#

INSERT INTO Languages (id, language) VALUES (2, 'Tibetan,bod skad/'),
(3, 'Classical Chinese,gnas rabs kyi rgya skad/'),
(4, 'Modern Chinese,deng dus kyi rgya skad/'),
(5, 'Sanskrit,legs sbyar skad/'),
(6, 'Hindi,deng dus kyi rgya gar skad hin di skad/'),
(7, 'Nepali,bal yul skad/'),
(8, 'Zhangzhung,zhang zhung skad/'),
(9, 'Mongolian,sog skad/'),
(10, 'Korean,khro\'o shan skad dam ko ri ya\'i skad/'),
(11, 'Japanese,nyi hong skad/'),
(12, 'Pali,pa li skad/'),
(1, 'English,dbyin skad/');

#
# Dumping data for table `LiteraryForms`
#

INSERT INTO LiteraryForms (id, literaryForm) VALUES (2, 'prose, bod skad med/'),
(1, 'verse, bod skad med/');

#
# Dumping data for table `LiteraryGenres`
#

INSERT INTO LiteraryGenres (id, literaryGenre) VALUES (2, 'epic, bod skad med/'),
(3, 'philosophy, bod skad med/'),
(4, 'history, bod skad med/'),
(1, 'biography, bod skad med/');

#
# Dumping data for table `LiteraryPeriods`
#

INSERT INTO LiteraryPeriods (id, literaryPeriod) VALUES (2, 'classical, bod skad med/'),
(3, 'modern, bod skad med/'),
(1, 'old, bod skad med/');

#
# Dumping data for table `LiterarySources`
#

INSERT INTO LiterarySources (id, literaryPeriod, literaryGenre, literaryForm, author, sourceNormalizedTitle, tibetanDate, internationalDate, edition, publisher, isbn, yearPublished, volumeNumber, pagination) VALUES (1, 2, 3, 1, 2, 'Mdzod bdun : the famed seven treasuries of VajrayÂ¯ana Buddhist philosophy', 'unknown', '1308-1363', 'Gangtok, Sikkim : Sherab Gyaltsen and Khyentse Labrang, 1983', 'Gangtok, Sikkim', 'unknown', 00000000000000, 0, 'unknown');

#
# Dumping data for table `MajorDialectFamilies`
#

INSERT INTO MajorDialectFamilies (id, majorDialectFamily) VALUES (2, 'Ladakh/Balti,la dwags sbal ti yul skad _nub khongs/'),
(3, 'Central Tibetan,dbus gtsang yul skad _dbus khongs/'),
(4, 'Lhasa,dbus gtsag lha sa yul skad _dbus khongs/'),
(5, 'Kham/Hor,khams hor yul skad _shar khongs/'),
(6, 'Amdo,a mdo yul skad _byang shar khongs/'),
(7, 'Dzongkha/Bhutanese,rdzong kha \'bras ljongs _lho khongs/'),
(1, 'Standard Tibetan,spyi skad _khyab khongs chen po\'i yul skad/');

#
# Dumping data for table `PhoneticsTypes`
#

INSERT INTO PhoneticsTypes (id, phoneticsType) VALUES (2, 'THDL Simplified Phonetics,bod yig med/'),
(3, 'Tournadre Phonetics, bod yig med/'),
(1, 'IPA Phonetics,bod yig med/');

#
# Dumping data for table `Preferences`
#

INSERT INTO Preferences (id, userId, preferencesName, projectSubject, source, language, script, dialect, note, useDefaultProjSub, useDefaultSource, useDefaultLanguage, useDefaultScript, useDefaultDialect, useDefaultNote, projectSubjectSet, sourceSet, languageSet, scriptSet, dialectSet) VALUES (2, 3, 0, 2, 1, 1, 1, 0, '', 'false', 'false', 'false', 'false', 'false', 'false', 'null', 'null', 'null', 'null', 'null'),
(3, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'false', 'false', 'false', 'false', 'false', 'false', NULL, NULL, NULL, NULL, NULL),
(1, 3, 0, 1, 1, 1, 1, 0, 'Default Note', 'false', 'false', 'false', 'false', 'false', 'false', '1', '1', '1', '1', '1'),
(4, 6, 0, 0, 0, 1, 1, 0, '', 'true', 'true', 'true', 'true', 'true', 'false', 'null', 'null', 'null', 'null', 'null'),
(5, 5, 0, 0, 0, 1, 1, 0, '', 'true', 'true', 'true', 'true', 'true', 'true', 'null', 'null', 'null', 'null', 'null'),
(6, 8, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'false', 'false', 'false', 'false', 'false', 'false', NULL, NULL, NULL, NULL, NULL);

#
# Dumping data for table `ProjectSubjects`
#

INSERT INTO ProjectSubjects (id, projectSubject, leader, participantList) VALUES (1, 'Germano Tantra Dictionary,\'jer mA no\'i gsang sngags kyi tha snyad/', 3, '1,2,3'),
(2, 'TLLR Colloquial,bod skad yig kyi slob gnyer gter mdzod las gzhi\'i kha skad tshig mdzod/', 3, ''),
(3, 'Tournadre/Dorje Manual Glossary,bod kyi spyi skad slob deb kyi rgyun mkho\'i tshig mdzod chung ngu/', 3, ''),
(4, 'TLLR Colloquial Standardized Spelling,bod skad yig kyi slob gnyer gter mdzod las gzhi\'i kha skad dag cha khungs ldan gsar/', 3, ''),
(5, 'TLLR Literary Glossary,bod skad yig kyi slob gnyer gter mdzod las gzhi\'i yi ge\'i tshig mdzod/', 3, ''),
(6, 'THDL Architecture Terminology,glog rdul dpe mdzod khang gi bzo skrun rig gnas kyi tha snyad/', 3, ''),
(7, 'THDL Astrology Terminology,glog rdul dpe mdzod khang gi skar rtsis rig pa\'i tha snyad/', 3, ''),
(8, 'THDL/Rubin Art Terminology,glog rdul dpe mdzod khang gi mdzas rtsal/', 3, ''),
(9, 'THDL Colophons,glog rdul dpe mdzod khang gi mjug byang gi tha snyad/', 3, ''),
(10, 'THDL Computer Science Terminology,glog rdul dpe mdzod khang gi glog klad rig pa\'i tha snyad/', 3, ''),
(11, 'THDL Library Science Terminology,glog rdul dpe mdzod khang gi dpe mdzod khang rig pa\'i tha snyad/', 3, ''),
(12, 'THDL Medical Terminology,glog rdul dpe mdzod khang gi gso ba rig pa\'i tha snyad/', 3, ''),
(13, 'THDL Music Terminology,glog rdul dpe mdzod khang gi rol gzhas kyi tha snyad/', 3, ''),
(14, 'THDL Samantabhadra Titles,glog rdul dpe mdzod khang gi rnying ma\'i rgyud \'bum dpe mtshan gyi tha snyad/', 3, ''),
(15, 'Public Domain Dictionary Entry Project,bod skad med/', 3, ''),
(16, 'Rangjung Yeshe Tibetan-English Dictionary, bod yig med/', 4, NULL);

#
# Dumping data for table `Registers`
#

INSERT INTO Registers (id, register) VALUES (2, 'Slang,logs skad/'),
(3, 'Ordinary,skad dkyus ma/'),
(4, 'Humilific,dma\' sa bzung ba\'i zhe sa/'),
(5, 'Honorific,zhe sa/'),
(6, 'High Honorific,zhe sa mtho po/'),
(7, 'Double Honorific,phyogs gnyis kyi zhe sa/'),
(1, 'Unspecified,gtan ma \'khel ba/');

#
# Dumping data for table `RelatedTermTypes`
#

INSERT INTO RelatedTermTypes (id, relatedTermType) VALUES (2, 'Non-honorific Form,bod yig med/'),
(3, 'Conjugated Form Past,bod yig med/'),
(4, 'Conjugated Form Present,bod yig med/'),
(5, 'Conjugated Form Future,bod yig med/'),
(6, 'Conjugated Form Imperativ,bod yig med/'),
(7, 'Dialectical correlates,yul skad mi \'dra ba brjod tshul/'),
(8, 'Literary and colloquial correlates,kha skad dang yig skad bar gyi \'brel ba/'),
(9, 'Compounds,ming \'dus nas ming du grub pa/'),
(10, 'Abbreviation,bsdus ming/'),
(11, 'Phrases,mnyam du \'grogs pa\'i ming tshig/'),
(12, 'Paired term,zung du \'brel ba\'i ming/'),
(13, 'Full Synonym,ming gi rnam grangs don yongs mtshungs/'),
(14, 'Para Synonym,ming gi rnam grangs don nye ba/'),
(15, 'Antonyms,ldog  phyogs kyi ming/'),
(16, 'Poetic expressions,mngon brjod/'),
(1, 'Honorific Form,bod yig med/');

#
# Dumping data for table `Scripts`
#

INSERT INTO Scripts (id, script) VALUES (2, 'Tibetan,bod yig'),
(3, 'Devanagiri,gya gar gyi yi ge'),
(4, 'Chinese,rgya yig'),
(1, 'Roman,dbyin yig');

#
# Dumping data for table `Sources`
#

INSERT INTO Sources (id, sourceTitle, sourceDescription) VALUES (2, 'Chandra Das,cha \'dra dA se/', 'A Public Domain Dictionary.'),
(3, 'H. A. J\ZÃ‰Ã??schke,ye sha ki/', 'A Public Domain Dictionary.'),
(1, 'Self,bdag rang/', 'This source is used when the user is the source for any data.');

#
# Dumping data for table `SpecificDialects`
#

INSERT INTO SpecificDialects (id, specificDialect) VALUES (1, 'Lhasa,dbus gtsang lha sa yul skad (dbus khongs )/');

#
# Dumping data for table `SpellingTypes`
#

INSERT INTO SpellingTypes (id, spellingType) VALUES (1, 'Unspecified,gtan ma \'khel ba/'),
(2, 'THDL Normalized Spelling,bod yig med/'),
(3, 'Alternative literary spelling,sbyor tshul mi \'dra ba/'),
(4, 'Mistaken spelling,sbyor tshul nor ba/'),
(5, 'Mistakenly corrected spelling,bcos nor shor ba/'),
(6, 'Old spelling,brda rnying/'),
(7, 'Vernacular Tibetan spelling,bod yig med/'),
(8, 'Standard Tibetan spelling,spyi skad _khyab khongs chen po\'i yul skad/'),
(9, 'Ladakh/Balti spelling,la dwags sbal ti yul skad _nub khongs/'),
(10, 'Central Tibetan spelling,dbus gtsang yul skad _dbus khongs/'),
(11, 'Lhasa spelling,dbus gtsag lha sa yul skad _dbus khongs/'),
(12, 'Kham/Hor spelling,khams hor yul skad _shar khongs/'),
(13, 'Amdo spelling,a mdo yul skad _byang shar khongs/'),
(14, 'Dzongkha/Bhutanese spelling,rdzong kha \'bras ljongs _lho khongs/'),
(15, 'Local Spelling - Unknown Locale,gnas ma nges pa\'i yul skad/');

#
# Dumping data for table `TransitionalDataLabels`
#

INSERT INTO TransitionalDataLabels (id, transitionalDataLabel) VALUES (2, 'Great Tibetan-Chinese Dictionary'),
(1, 'The Rangjung Yeshe Tibetan-English Dictionary'); 
