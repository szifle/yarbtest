alter table if exists BOARD drop constraint if exists FK409fie40k8sjkwmm0kkbype9l;
alter table if exists BOARD_COLUMN drop constraint if exists FK1bgsy280wyrfieica37vpat42;
alter table if exists BOARD_NOTE drop constraint if exists FKfvyseqvvo8qg90hqveiwi8xaq;
drop table if exists "USER" cascade;
drop table if exists BOARD cascade;
drop table if exists BOARD_COLUMN cascade;
drop table if exists BOARD_NOTE cascade;
drop sequence if exists BOARD_COLUMN_SEQUENCE;
drop sequence if exists BOARD_NOTE_SEQUENCE;
drop sequence if exists BOARD_SEQUENCE;
drop sequence if exists USER_SEQUENCE;
