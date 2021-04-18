drop table if exists t_hello_api_access_2_entity;
drop table if exists t_hello_api_access_3_entity;
drop table if exists t_hello_api_access_entity;
drop table if exists t_hello_audit_log_entity;
drop table if exists t_hello_channel_entity;
drop table if exists t_hello_tenant_entity;
drop table if exists t_hello_user_entity;


create table if not exists `t_hello_api_access_2_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `allow_url` varchar(255) default '',
  `deny_url` varchar(255) default '',
  primary key (`id`)
);
create table if not exists `t_hello_api_access_3_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `allow_signature` varchar(255) default '',
  `deny_signature` varchar(255) default '',
  primary key (`id`)
);
create table if not exists `t_hello_api_access_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `allow_url_regex` varchar(255) default '',
  `allow_signature_regex` varchar(255) default '',
  `deny_url_regex` varchar(255) default '',
  `deny_signature_regex` varchar(255) default '',
  primary key (`id`)
);
create table if not exists `t_hello_audit_log_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tracing_no` varchar(40) default '',
  `req_datetime` varchar(20) default '',
  `req_ips` varchar(255) default '',
  `req_user_id` varchar(40) default '',
  `req_json_str_raw` text default '',
  `req_json_str` text default '',
  `req_url` varchar(255) default '',
  `req_signature` varchar(255) default '',
  `cached` varchar(1) default '',
  `resp_json_str` text default '',
  `resp_json_str_ctx` text default '',
  `resp_json_str_raw` text default '',
  `resp_ips` varchar(255) default '',
  `resp_datetime` varchar(20) default '',
  primary key (`id`)
);
create table if not exists `t_hello_channel_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `no` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  `token_value` varchar(255) default '',
  `token_force` varchar(255) default '',
  `token_algorithm_type` varchar(255) default '',
  `token_value_type` varchar(255) default '',
  primary key (`id`)
);
create unique index if not exists u_idx_no on t_hello_channel_entity(`no`);
create table if not exists `t_hello_tenant_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `no` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  primary key (`id`)
);
create unique index if not exists u_idx_no on t_hello_tenant_entity(`no`);
create table if not exists `t_hello_user_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `no` varchar(40) default '',
  `name` varchar(30) default '',
  `haha` varchar(255) default '',
  `mobile` varchar(20) default '',
  `email` varchar(40) default '',
  primary key (`id`)
);


insert into t_hello_tenant_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, no, name, description)
select 0,'1612262610215_LoHqeZBGrVYm3MlYmpH','N','','1612262610216_koFVLCNZrhezbgULWqW','tstTenantNo','tstTenantName','tstTenantDesc';
insert into t_hello_user_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, tenant_id, no, name, haha, mobile, email)
select 0,'1612262610216_koFVLCNZrhezbgULWqW','N','','1612262610216_koFVLCNZrhezbgULWqW','1612262610215_LoHqeZBGrVYm3MlYmpH','tstUserNo','tstUserName','[SKC1]494c6f7665596f75','18888888888','email@email.com';
insert into t_hello_channel_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, no, name, description, token_value, token_force, token_algorithm_type, token_value_type)
select 0,'1612263653223_oGFvE5Hyndf0njoFhyK','N','','1612262610216_koFVLCNZrhezbgULWqW','tstChannelNo','tstChannelName','tstChannelDesc','494c6f7665596f75','N','SKC1','SELF';
insert into t_hello_api_access_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, tenant_id, channel_id, allow_url_regex, allow_signature_regex, deny_url_regex, deny_signature_regex)
select 0,'1612263668482_jaHu6tmguyKo2xWgHPj','N','','1612262610216_koFVLCNZrhezbgULWqW','1612262610215_LoHqeZBGrVYm3MlYmpH','1612263653223_oGFvE5Hyndf0njoFhyK','','^[\s\S]*(add|rmvById|mod|mge|lst)\([\s\S]*$','','';


select * from t_hello_api_access_2_entity;
select * from t_hello_api_access_3_entity;
select * from t_hello_api_access_entity;
select * from t_hello_audit_log_entity;
select * from t_hello_channel_entity;
select * from t_hello_tenant_entity;
select * from t_hello_user_entity;


vacuum;
