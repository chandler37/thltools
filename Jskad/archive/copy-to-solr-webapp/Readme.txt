The files in this directory must be copied to your solr webapp
before you can use lucene-thdl-build.xml to post, commit, or 
delete documents from your solr server.

schema.xml & solrconfig.xml: 
    Copy these files to your solr/conf directory,
    replacing the existing files with these names

 
lucene-thdl.jar :
    Create an up to date copy of this file by
    running the task lucene-thdl-jar, then copy
    to your solr/lib directory. If solr/lib does
    not exist, then create it.

