October 6, 2003
The THDL Tibetan Collaborative Dictionary requires the following:

1. Check out the ThdlUser module to the same directory as the Dictionary. Or if you want to put it elsewhere, modify build.properties accordingly.

2. A recent version of Tomcat must be installed and the tomcat user/pass combo specified in the build file must be present in the tomcat-users.xml file with the role of manager.

3. Ant must be installed and the ant bin directory should be in your PATH variable. Also, catalina-ant.jar needs to be in $ANT_HOME/lib.

4. The MySQL databases Lex and ThdlUsers should be running locally  with select, insert, update and delete privileges for the user specified in the following files: lex-context-config.xml, tomcat\conf\Catalina\localhost\lex.xml. Make sure to also update the docbase, workdir, and address for mysql database.

5. If you are using the DictionaryImporter class, copy dictionary-importer.properties.sample to dictionary.properties and update also the user and password.

6. The package includes a pre-configured tomcat server. If you would rather use your own installation of tomcat:
- Copy from tomcat/common/lib to your tomcat installation the following files: commons-dbcp-1.2.1.jar, commons-pool-1.2.jar, commons-collections-3.1.jar.
- Modify build.xml so that tomcat/common/lib folder points to your tomcat installation.
- Copy lex.xml from tomcat/conf/Catalina/localhost to your tomcat installation. Then modify update the workdir property.

To get a copy of the database, contact Steve Weinberger ( snw8f@virginia.edu ).

This document was written by Travis McCauley.
Updated by Andres Montano, 2/22/2005