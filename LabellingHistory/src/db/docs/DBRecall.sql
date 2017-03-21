--Random Data Population
SELECT     LEVEL                                                       empl_id,
           MOD (ROWNUM, 50000)                                         dept_id,
           TRUNC (DBMS_RANDOM.VALUE (1000, 500000), 2)                 salary,
           DECODE (ROUND (DBMS_RANDOM.VALUE (1, 3)), 1, 'M', 2, 'F', 3, null)   gender,
           TO_DATE (   ROUND (DBMS_RANDOM.VALUE (1, 28))
                    || '-'
                    || ROUND (DBMS_RANDOM.VALUE (1, 12))
                    || '-'
                    || ROUND (DBMS_RANDOM.VALUE (1900, 2010)),
                    'DD-MM-YYYY'
                   )                                                   dob,
           DBMS_RANDOM.STRING ('x', DBMS_RANDOM.VALUE (20, 50))        address
      FROM DUAL
CONNECT BY LEVEL < 10000;

--Primary Key Columns based on Table Name 
SELECT column_name FROM all_cons_columns WHERE constraint_name = (
  SELECT constraint_name FROM user_constraints 
  WHERE UPPER(table_name) = UPPER('tb_ent_col_master') AND CONSTRAINT_TYPE = 'P'
);

--Difference between two tables
select a.column_name--,a.data_type, a.data_length, a.data_scale,a.data_precision
from all_tab_columns a
where table_name in ('PH_INTERFACE_FORMAT_AUDIT')
MINUS
select a.column_name--,a.data_type, a.data_length, a.data_scale,a.data_precision
from all_tab_columns a
where table_name in ('PH_INTERFACE_FORMAT');

--Primary Keys
SELECT cols.table_name, cols.column_name, cols.position, cons.status, cons.owner
FROM all_constraints cons, all_cons_columns cols
WHERE cols.table_name = 'TABLE_NAME'
AND cons.constraint_type = 'P'
AND cons.constraint_name = cols.constraint_name
AND cons.owner = cols.owner
ORDER BY cols.table_name, cols.position;

--Table Name and Column Name based on Constraint Name 
SELECT a.owner, a.table_name, b.column_name
  FROM dba_constraints a, ALL_CONS_COLUMNS b
 WHERE a.constraint_name = b.constraint_name
 and a.constraint_name = 'PK_OW_PRCSSAITEM_IDITEMTYPID';
 
SELECT a.table_name, a.column_name, a.constraint_name, c.owner, 
       -- referenced pk
       c.r_owner, c_pk.table_name r_table_name, c_pk.constraint_name r_pk
  FROM all_cons_columns a
  JOIN all_constraints c ON a.owner = c.owner
                        AND a.constraint_name = c.constraint_name
  JOIN all_constraints c_pk ON c.r_owner = c_pk.owner
                           AND c.r_constraint_name = c_pk.constraint_name
 WHERE c.constraint_type = 'R'
   AND a.table_name in :TableName
   
DELETE FROM TB_ENT_PK_MASTER
WHERE rowid not in
(SELECT MIN(rowid)
FROM TB_ENT_PK_MASTER
GROUP BY table_name, column_name);
commit;


--To Find Nullable Column
SELECT table_name, column_name, nullable
  FROM all_tab_cols
 WHERE NULLABLE = 'N'
--and  owner = 'OW_FORMS_HISTORY'
and table_name in ('OW_PROCESS_HISTORY','OW_USER_PROCESSES_HISTORY','OW_PROCESS_OWNER_HISTORY','OW_EVENT_HISTORY','OW_ATTRIBUTE_HISTORY','OW_PROCESS_ASSO_ITEM_HISTORY','OW_QUEUE_HISTORY','OW_QUEUE_LOADBALANCE_HISTORY','OW_TASK_HISTORY','OW_FORMS_HISTORY','OW_INTERPROCESS_ATTRIBUTE_HIST','OW_TASK_CHECK_HISTORY','OW_TASK_ROUTELIST_HISTORY','OW_JOIN_NODE_HISTORY','OW_SPLIT_NODE_HISTORY','OW_ITEM_ASSO_TASK_HISTORY','OW_CONDITION_HISTORY','OW_RULE_HISTORY','OW_RULE_VALIDITY_HISTORY','OW_USER_RIGHTS_HISTORY','OW_PROCESS_ROUTELIST_HISTORY','OW_PROCESS_SPLIT_NODE_HISTORY');
AND column_name IN ('REQID');

--Insert using Select
INSERT INTO table
(column1, column2, ... column_n )
SELECT expression1, expression2, ... expression_n
FROM source_table
[WHERE conditions]; 

select table_name, count(*) 
from user_tab_columns
where table_name in ('PH_RULE_NODE', 'PH_RULE_NODE_AUDIT') 
GROUP BY table_name;
 
D:\HUBSWorkspace\Jboss-5.1.0.-jdk\server\default\deploy\HUBS.ear\HUBSWeb.war\js\form

select table_name, column_name from tb_ent_pk_master where table_name in (select table_name from tb_ent_pk_master group by table_name)
MINUS
select table_name, column_name from ALL_CONS_COLUMNS where constraint_name in 
(select constraint_name from USER_CONSTRAINTS where table_name in (select table_name from tb_ent_pk_master group by table_name) and CONSTRAINT_TYPE='P')
and owner='C##MITHULB';

Select * from dev2.TB_LINKED_LABEL_INFO;

alter table TB_LINKED_LABEL_INFO drop constraint TB_LINKED_LABEL_INFO_PK drop index;
alter table TB_LINKED_LABEL_INFO add constraint TB_LINKED_LABEL_INFO_PK  PRIMARY KEY (APPLICATION_ID, LABEL_NAME, LINKED_APPLICATION_ID, LINKED_LABEL_NAME); 

Select 'ALTER TABLE ' || usrcon.table_name || ' ADD CONSTRAINT ' || usrcon.constraint_name || ' PRIMARY KEY ("'|| listagg(usrcol.column_name, '","') within group (order by usrcol.column_name) || '")'
from user_constraints usrcon, user_cons_columns usrcol
where usrcon.constraint_name = usrcol.constraint_name
and usrcon.constraint_type = 'P'
--and usrcon.constraint_name = 'PK_GRID111_W'
group by usrcon.constraint_name,usrcon.table_name;

----------------------------
create user C##LOCAL_COPY identified by localcopy DEFAULT TABLESPACE USERS TEMPORARY TABLESPACE TEMP;
GRANT DBA to C##LOCAL_COPY;
DROP USER C##LOCAL_COPY CASCADE;

UPDATE TB_PROPERTY_VALUE SET VALUE=REPLACE(VALUE,'ejblocal:','java:comp/env/') WHERE LOWER(VALUE) LIKE'%ejblocal%'

Alter TABLESPACE USERS resize 100M;


select df.tablespace_name "Tablespace",
totalusedspace "Used MB",
(df.totalspace - tu.totalusedspace) "Free MB",
df.totalspace "Total MB",
round(100 * ( (df.totalspace - tu.totalusedspace)/ df.totalspace))
"Pct. Free"
from
(select tablespace_name,
round(sum(bytes) / 1048576) TotalSpace
from dba_data_files 
group by tablespace_name) df,
(select round(sum(bytes)/(1024*1024)) totalusedspace, tablespace_name
from dba_segments 
group by tablespace_name) tu
where df.tablespace_name = tu.tablespace_name ;


Select 'ALTER TABLE ' || usrcon.table_name || ' ADD CONSTRAINT ' || usrcon.constraint_name || ' PRIMARY KEY ('|| listagg(usrcol.column_name, ',') within group (order by usrcol.column_name) || ')'
from user_constraints usrcon, user_cons_columns usrcol
where usrcon.constraint_name = usrcol.constraint_name
and usrcon.constraint_type = 'P'
and usrcon.table_name = 'TESTDEC13_HISTORY'
group by usrcon.constraint_name,usrcon.table_name;

Select usrcon.table_name , usrcon.constraint_name, usrcol.column_name
from user_constraints usrcon, user_cons_columns usrcol
where usrcon.constraint_name = usrcol.constraint_name
and usrcon.constraint_type = 'P';

Select 'ALTER TABLE ' || usrcon.table_name || ' ADD CONSTRAINT ' || usrcon.constraint_name || ' PRIMARY KEY ('|| listagg(usrcol.column_name, ',') within group (order by usrcol.column_name) || ')'
from user_constraints usrcon, user_cons_columns usrcol
where usrcon.constraint_name = usrcol.constraint_name
and usrcon.constraint_type = 'C'
--and usrcon.constraint_name = 'PK_GRID111_W'
group by usrcon.constraint_name,usrcon.table_name;

Select TABLE_NAME, CONSTRAINT_NAME, R_CONSTRAINT_NAME from user_constraints where constraint_name = 'FK_SCRFM_LFM_FLDID';

Select * from user_cons_columns where constraint_name = 'FK_SCRFM_LFM_FLDID';

select dbms_metadata.get_ddl('CONSTRAINT', 'FK_SCRFM_LFM_FLDID') from dual;

SELECT dbms_metadata.get_dependent_ddl('CONSTRAINT', table_name)
  FROM user_tables t
--WHERE table_name IN ('EMP', 'DEPT')
  WHERE EXISTS (SELECT 1
                FROM user_constraints
              WHERE table_name = t.table_name
                AND constraint_type = 'C');
from 
   user_constraints c 
where 
   c.constraint_type = 'P'