create table tbl_sample1(col1 varchar2(500));
create table tbl_sample2(col2 varchar2(50));

select * from tbl_sample1;
select * from tbl_sample2;

delete tbl_sample1;

alter table tbl_board add (replycnt number default 0);

update tbl_board set replycnt = (select count(rno) from tbl_reply where tbl_reply.bno = tbl_board.bno);

