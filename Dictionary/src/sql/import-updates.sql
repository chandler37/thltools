use lex;

--add TransitionalDataLabels
insert into TransitionalDataLabels values ( null, "Text That You Want To Appear In The Public Interface To Describe This Dictionary" );

--add some new project subjects
insert int ProjectSubjects( id, projectSubject, leader, participantList ) 
	values ( null, "The Name Of The New Project Subject,bod yig med", 0, null );

select "+++++Display Newly inserted ids";
select id, transitionalDataLabel from TransitionalDataLabels order by id desc;
select id, projectSubject from ProjectSubjects order by id desc;


	
