October 6, 2003
The THDL Tibetan Collaborative Dictionary requires the following:

1. Check out the ThdlUser module to the same directory as the Dictionary. Or if you want to put it elsewhere, modify build.properties accordingly.

2. A recent version of Tomcat must be installed and the tomcat user/pass combo specified in the build file must be present in the tomcat-users.xml file with the role of manager.

3. Ant must be installed and the ant bin directory should be in your PATH variable. Also, catalina-ant.jar needs to be in $ANT_HOME/lib.

4. The MySQL databases Lex and ThdlUsers should be running locally  with select, insert, update and delete privileges for the user specified in the lex-context-config.xml file. 

To get a copy of the database, contact Travis McCauley ( travis.mccauley@alumni.virginia.edu ).



