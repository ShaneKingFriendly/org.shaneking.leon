drop table if exists t_hello_api_access_regex_entity;
drop table if exists t_hello_api_access_signature_entity;
drop table if exists t_hello_api_access_url_entity;
drop table if exists t_hello_audit_log_entity;
drop table if exists t_hello_channel_entity;
drop table if exists t_hello_tenant_entity;
drop table if exists t_hello_user_entity;
drop table if exists t_hello_user_entity_d;


-- HelloApiAccessRegexEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_api_access_regex_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `allow_url_regex` varchar(255) default '',
  `allow_signature_regex` varchar(255) default '',
  `deny_url_regex` varchar(255) default '',
  `deny_signature_regex` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_tenant_id on t_hello_api_access_regex_entity(`channel_id`,`tenant_id`);

-- HelloApiAccessSignatureEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_api_access_signature_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `op` varchar(1) default '',
  `signature` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_tenant_id_signature on t_hello_api_access_signature_entity(`channel_id`,`tenant_id`,`signature`);

-- HelloApiAccessUrlEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_api_access_url_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `op` varchar(1) default '',
  `url` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_tenant_id_url on t_hello_api_access_url_entity(`channel_id`,`tenant_id`,`url`);

-- HelloAuditLogEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_audit_log_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `proxy_channel_id` varchar(40) default '',
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

-- HelloChannelEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_channel_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  `token_value` varchar(255) default '',
  `token_force` varchar(255) default '',
  `token_algorithm_type` varchar(255) default '',
  `token_value_type` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no on t_hello_channel_entity(`no`);

-- HelloTenantEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_tenant_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no on t_hello_tenant_entity(`no`);

-- HelloUserEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_hello_user_entity` (
  `version` int not null default 0,
  `id` char(40) not null,
  `dd` varchar(40) default 'N',
  `no` varchar(40) default '',
  `invalid` varchar(1) default 'N',
  `last_modify_date_time` varchar(20) default '',
  `last_modify_user_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `name` varchar(30) default '',
  `haha` varchar(255) default '',
  `mobile` varchar(20) default '',
  `email` varchar(40) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no_tenant_id on t_hello_user_entity(`no`,`tenant_id`);


insert into t_hello_tenant_entity (version, id, no, invalid, last_modify_date_time, last_modify_user_id, name, description)
select 0,'1612262610215_LoHqeZBGrVYm3MlYmpH','tstTenantNo','N','','1612262610216_koFVLCNZrhezbgULWqW','tstTenantName','tstTenantDesc';
insert into t_hello_user_entity (version, id, no, invalid, last_modify_date_time, last_modify_user_id, tenant_id, name, haha, mobile, email)
select 0,'1612262610216_koFVLCNZrhezbgULWqW','tstUserNo','N','','1612262610216_koFVLCNZrhezbgULWqW','1612262610215_LoHqeZBGrVYm3MlYmpH','tstUserName','[SKC1]494c6f7665596f75','18888888888','email@email.com';
insert into t_hello_channel_entity (version, id, no, invalid, last_modify_date_time, last_modify_user_id, name, description, token_value, token_force, token_algorithm_type, token_value_type)
select 0,'1612263653223_oGFvE5Hyndf0njoFhyK','tstChannelNo','N','','1612262610216_koFVLCNZrhezbgULWqW','tstChannelName','tstChannelDesc','494c6f7665596f75','N','SKC1','SELF';
insert into t_hello_api_access_regex_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, channel_id, tenant_id, allow_url_regex, allow_signature_regex, deny_url_regex, deny_signature_regex)
select 0,'1612263668482_jaHu6tmguyKo2xWgHPj','N','','1612262610216_koFVLCNZrhezbgULWqW','1612263653223_oGFvE5Hyndf0njoFhyK','1612262610215_LoHqeZBGrVYm3MlYmpH','','^[\s\S]*(add|rmv|mod|mge|lst)\([\s\S]*$','','';

create table if not exists t_hello_user_entity_d as select * from t_hello_user_entity where 1=0;
select * from t_hello_api_access_regex_entity;
select * from t_hello_api_access_signature_entity;
select * from t_hello_api_access_url_entity;
select * from t_hello_audit_log_entity;
select * from t_hello_channel_entity;
select * from t_hello_tenant_entity;
select * from t_hello_user_entity;
select * from t_hello_user_entity_d;


vacuum;
