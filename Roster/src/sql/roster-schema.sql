
# -----------------------------------------------------------------------
# Member
# -----------------------------------------------------------------------
drop table if exists Member;

CREATE TABLE Member
(
        id INTEGER NOT NULL,
        created_by INTEGER NOT NULL,
        modified_by INTEGER NOT NULL,
        created_on TIMESTAMP NOT NULL,
        modified_on TIMESTAMP NOT NULL,
        deleted CHAR (5) NOT NULL,
        contact_info_id INTEGER,
        research_interest_id INTEGER,
        publication_id INTEGER,
        member_type VARCHAR (24) NOT NULL,
        person_data_id INTEGER,
        project_data_id INTEGER,
        organization_data_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY (contact_info_id) REFERENCES ContactInfo (id),
    FOREIGN KEY (research_interest_id) REFERENCES ResearchInterest (id),
    FOREIGN KEY (publication_id) REFERENCES Publication (id),
    FOREIGN KEY (person_data_id) REFERENCES PersonData (id),
    FOREIGN KEY (project_data_id) REFERENCES ProjectData (id),
    FOREIGN KEY (organization_data_id) REFERENCES OrganizationData (id)
);

# -----------------------------------------------------------------------
# PersonData
# -----------------------------------------------------------------------
drop table if exists PersonData;

CREATE TABLE PersonData
(
        id INTEGER NOT NULL,
        thdl_user_id INTEGER NOT NULL,
        firstname MEDIUMTEXT,
        middlename MEDIUMTEXT,
        lastname MEDIUMTEXT,
        bio LONGTEXT,
        history LONGTEXT,
        parent_organization MEDIUMTEXT,
        school MEDIUMTEXT,
        department MEDIUMTEXT,
        program MEDIUMTEXT,
        advisor MEDIUMTEXT,
        highest_degree MEDIUMTEXT,
        year_began INTEGER,
        year_finished INTEGER,
        other_backgrounds LONGTEXT,
        organization MEDIUMTEXT,
        division MEDIUMTEXT,
        title MEDIUMTEXT,
        start_date INTEGER,
        job_description LONGTEXT,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# ProjectData
# -----------------------------------------------------------------------
drop table if exists ProjectData;

CREATE TABLE ProjectData
(
        id INTEGER NOT NULL,
        name MEDIUMTEXT,
        parent_organization MEDIUMTEXT,
        divisions MEDIUMTEXT,
        people MEDIUMTEXT,
        mailing_list MEDIUMTEXT,
        description LONGTEXT,
        history LONGTEXT,
        education_programs LONGTEXT,
        resources LONGTEXT,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# OrganizationData
# -----------------------------------------------------------------------
drop table if exists OrganizationData;

CREATE TABLE OrganizationData
(
        id INTEGER NOT NULL,
        name MEDIUMTEXT,
        parent_organization MEDIUMTEXT,
        divisions MEDIUMTEXT,
        people MEDIUMTEXT,
        mailing_list MEDIUMTEXT,
        description LONGTEXT,
        history LONGTEXT,
        education_programs LONGTEXT,
        resources LONGTEXT,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# ContactInfo
# -----------------------------------------------------------------------
drop table if exists ContactInfo;

CREATE TABLE ContactInfo
(
        id INTEGER NOT NULL,
        contact_name MEDIUMTEXT,
        email MEDIUMTEXT,
        website MEDIUMTEXT,
        phone INTEGER,
        fax INTEGER,
        address_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY (address_id) REFERENCES Address (id),
    FOREIGN KEY (phone) REFERENCES Phone (id),
    FOREIGN KEY (fax) REFERENCES Phone (id)
);

# -----------------------------------------------------------------------
# Address
# -----------------------------------------------------------------------
drop table if exists Address;

CREATE TABLE Address
(
        id INTEGER NOT NULL,
        address MEDIUMTEXT,
        city MEDIUMTEXT,
        region MEDIUMTEXT,
        zip MEDIUMTEXT,
        country_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY (country_id) REFERENCES Country (id)
);

# -----------------------------------------------------------------------
# Phone
# -----------------------------------------------------------------------
drop table if exists Phone;

CREATE TABLE Phone
(
        id INTEGER NOT NULL,
        country_code INTEGER,
        area_code INTEGER,
        number INTEGER,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# Publication
# -----------------------------------------------------------------------
drop table if exists Publication;

CREATE TABLE Publication
(
        id INTEGER NOT NULL,
        formal_publications LONGTEXT,
        works_in_progress LONGTEXT,
        projects LONGTEXT,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# ResearchInterest
# -----------------------------------------------------------------------
drop table if exists ResearchInterest;

CREATE TABLE ResearchInterest
(
        id INTEGER NOT NULL,
        interests LONGTEXT,
        activities LONGTEXT,
        collaboration_interests LONGTEXT,
        focus_from INTEGER,
        focus_to INTEGER,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# Document
# -----------------------------------------------------------------------
drop table if exists Document;

CREATE TABLE Document
(
        id INTEGER NOT NULL,
        member_id INTEGER,
        document_type_id INTEGER,
        content_type MEDIUMTEXT,
        path MEDIUMTEXT,
        filename MEDIUMTEXT,
        label MEDIUMTEXT,
    PRIMARY KEY(id),
    FOREIGN KEY (member_id) REFERENCES Member (id),
    FOREIGN KEY (document_type_id) REFERENCES DocumentType (id)
);

# -----------------------------------------------------------------------
# Country
# -----------------------------------------------------------------------
drop table if exists Country;

CREATE TABLE Country
(
        id INTEGER NOT NULL,
        country MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# CulturalArea
# -----------------------------------------------------------------------
drop table if exists CulturalArea;

CREATE TABLE CulturalArea
(
        id INTEGER NOT NULL,
        cultural_area MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# Language
# -----------------------------------------------------------------------
drop table if exists Language;

CREATE TABLE Language
(
        id INTEGER NOT NULL,
        language MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# Discipline
# -----------------------------------------------------------------------
drop table if exists Discipline;

CREATE TABLE Discipline
(
        id INTEGER NOT NULL,
        discipline MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# DocumentType
# -----------------------------------------------------------------------
drop table if exists DocumentType;

CREATE TABLE DocumentType
(
        id INTEGER NOT NULL,
        document_type MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# ProjectType
# -----------------------------------------------------------------------
drop table if exists ProjectType;

CREATE TABLE ProjectType
(
        id INTEGER NOT NULL,
        project_type MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# OrganizationType
# -----------------------------------------------------------------------
drop table if exists OrganizationType;

CREATE TABLE OrganizationType
(
        id INTEGER NOT NULL,
        organization_type MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# PersonType
# -----------------------------------------------------------------------
drop table if exists PersonType;

CREATE TABLE PersonType
(
        id INTEGER NOT NULL,
        person_type MEDIUMTEXT NOT NULL,
    PRIMARY KEY(id)
);

# -----------------------------------------------------------------------
# PersonPersonType
# -----------------------------------------------------------------------
drop table if exists PersonPersonType;

CREATE TABLE PersonPersonType
(
        id INTEGER NOT NULL,
        person_type_id INTEGER NOT NULL,
        person_data_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (person_data_id) REFERENCES PersonData (id),
    FOREIGN KEY (person_type_id) REFERENCES PersonType (id)
);

# -----------------------------------------------------------------------
# OrganizationOrganizationType
# -----------------------------------------------------------------------
drop table if exists OrganizationOrganizationType;

CREATE TABLE OrganizationOrganizationType
(
        id INTEGER NOT NULL,
        organization_type_id INTEGER NOT NULL,
        organization_data_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (organization_data_id) REFERENCES OrganizationData (id),
    FOREIGN KEY (organization_type_id) REFERENCES OrganizationType (id)
);

# -----------------------------------------------------------------------
# ProjectProjectType
# -----------------------------------------------------------------------
drop table if exists ProjectProjectType;

CREATE TABLE ProjectProjectType
(
        id INTEGER NOT NULL,
        project_type_id INTEGER NOT NULL,
        project_data_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (project_data_id) REFERENCES ProjectData (id),
    FOREIGN KEY (project_type_id) REFERENCES ProjectType (id)
);

# -----------------------------------------------------------------------
# ResearchInterestLanguage
# -----------------------------------------------------------------------
drop table if exists ResearchInterestLanguage;

CREATE TABLE ResearchInterestLanguage
(
        id INTEGER NOT NULL,
        language_id INTEGER NOT NULL,
        research_interest_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (research_interest_id) REFERENCES ResearchInterest (id),
    FOREIGN KEY (language_id) REFERENCES Language (id)
);

# -----------------------------------------------------------------------
# ResearchInterestCulturalArea
# -----------------------------------------------------------------------
drop table if exists ResearchInterestCulturalArea;

CREATE TABLE ResearchInterestCulturalArea
(
        id INTEGER NOT NULL,
        cultural_area_id INTEGER NOT NULL,
        research_interest_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (research_interest_id) REFERENCES ResearchInterest (id),
    FOREIGN KEY (cultural_area_id) REFERENCES CulturalArea (id)
);

# -----------------------------------------------------------------------
# ResearchInterestDiscipline
# -----------------------------------------------------------------------
drop table if exists ResearchInterestDiscipline;

CREATE TABLE ResearchInterestDiscipline
(
        id INTEGER NOT NULL,
        discipline_id INTEGER NOT NULL,
        research_interest_id INTEGER NOT NULL,
        relevance INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (research_interest_id) REFERENCES ResearchInterest (id),
    FOREIGN KEY (discipline_id) REFERENCES Discipline (id)
);
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
