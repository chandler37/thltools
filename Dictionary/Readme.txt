October 6, 2003
The THDL Tibetan Collaborative Dictionary requires the following:

1. ThdlUser module needs to be compiled into the Dictionary build directory. If Dictionary and ThdlUser are checked out to the same directory, the ThdlUser build file will automatically compile to the default build directory inside Dictionary.

2. A recent version of Tomcat must be installed and the tomcat user/pass combo specified in the build file must be present in the tomcat-users.xml file with the role of manager.

3. catalina-ant.jar needs to be in $ANT_HOME/lib.

4. The MySQL databases Lex and ThdlUsers should be running locally  with select, insert, update and delete privileges for the user specified in the lex-context-config.xml file. 

To get a copy of the database, contact Travis McCauley ( travis.mccauley@alumni.virginia.edu ).

Here are the steps:

[Haley:~/sandbox]% ls -l
total 0
drwxr-xr-x  16 travis  staff  544 Oct  6 13:10 Dictionary
drwxr-xr-x   5 travis  staff  170 Oct  6 12:35 ThdlUser

[Haley:~/sandbox]% ant -f ThdlUser/build.xml 

[Haley:~/sandbox]% ant -f Dictionary/build.xml install






