The THDL Tibetan Collaborative Dictionary requires the following:

1. ThdlUser module needs to be compiled into the Dictionary build directory. If Dictionary and ThdlUser are checked out to the same directory, the ThdlUser build file will automatically compile to the default build directory inside Dictionary.

2. A recent version of Tomcat must be installed and the tomcat user/pass combo specified in the build file must be present in the tomcat-users.xml file with the role of manager.

3. The MySQL databases Lex and ThdlUsers should be running locally  with select, insert, update and delete privileges for the user specified in the lex-context-config.xml file. 

To get a copy of the database, contact Travis McCauley ( travis.mccauley@alumni.virginia.edu ).

