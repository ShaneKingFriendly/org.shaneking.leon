drop table if exists t_simple_channel_access_regex_api_entity;
drop table if exists t_simple_channel_access_signature_api_entity;
drop table if exists t_simple_channel_access_tenant_regex_api_entity;
drop table if exists t_simple_channel_access_tenant_signature_api_entity;
drop table if exists t_simple_channel_access_tenant_url_api_entity;
drop table if exists t_simple_channel_access_url_api_entity;
drop table if exists t_simple_channel_entity;
drop table if exists t_simple_rr_async_log_entity;
drop table if exists t_simple_rr_audit_log_entity;
drop table if exists t_simple_tenant_entity;
drop table if exists t_simple_user_entity;


-- SimpleChannelAccessRegexApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_regex_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `allow_url_regex` varchar(255) default '',
  `allow_signature_regex` varchar(255) default '',
  `deny_url_regex` varchar(255) default '',
  `deny_signature_regex` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_no on t_simple_channel_access_regex_api_entity(`channel_id`,`no`);
create unique index if not exists u_idx_channel_id on t_simple_channel_access_regex_api_entity(`channel_id`);


-- SimpleChannelAccessSignatureApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_signature_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `op` varchar(1) default '',
  `signature` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_signature on t_simple_channel_access_signature_api_entity(`channel_id`,`signature`);
create unique index if not exists u_idx_channel_id_no on t_simple_channel_access_signature_api_entity(`channel_id`,`no`);


-- SimpleChannelAccessTenantRegexApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_tenant_regex_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `allow_url_regex` varchar(255) default '',
  `allow_signature_regex` varchar(255) default '',
  `deny_url_regex` varchar(255) default '',
  `deny_signature_regex` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_tenant_id_channel_id on t_simple_channel_access_tenant_regex_api_entity(`tenant_id`,`channel_id`);
create unique index if not exists u_idx_tenant_id_no on t_simple_channel_access_tenant_regex_api_entity(`tenant_id`,`no`);


-- SimpleChannelAccessTenantSignatureApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_tenant_signature_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `op` varchar(1) default '',
  `signature` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_tenant_id_signature on t_simple_channel_access_tenant_signature_api_entity(`channel_id`,`tenant_id`,`signature`);
create unique index if not exists u_idx_tenant_id_no on t_simple_channel_access_tenant_signature_api_entity(`tenant_id`,`no`);


-- SimpleChannelAccessTenantUrlApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_tenant_url_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `op` varchar(1) default '',
  `url` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_tenant_id_url on t_simple_channel_access_tenant_url_api_entity(`channel_id`,`tenant_id`,`url`);
create unique index if not exists u_idx_tenant_id_no on t_simple_channel_access_tenant_url_api_entity(`tenant_id`,`no`);


-- SimpleChannelAccessUrlApiEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_access_url_api_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `op` varchar(1) default '',
  `url` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_channel_id_no on t_simple_channel_access_url_api_entity(`channel_id`,`no`);
create unique index if not exists u_idx_channel_id_url on t_simple_channel_access_url_api_entity(`channel_id`,`url`);



-- SimpleChannelEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_channel_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  `enc_tv` varchar(255) default '',
  `enc_tf` varchar(1) default '',
  `enc_tat` varchar(255) default '',
  `enc_tvt` varchar(7) default '',
  `dsz_seconds` int default 0,
  `mvc_tv` varchar(255) default '',
  `mvc_tf` varchar(1) default '',
  `mvc_tat` varchar(255) default '',
  `mvc_tvt` varchar(7) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no on t_simple_channel_entity(`no`);
create unique index if not exists u_idx_name on t_simple_channel_entity(`name`);


-- SimpleRrAsyncLogEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_rr_async_log_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(30) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `req_json_str_raw` text default '',
  `ctx_json_str` text default '',
  `start_datetime` varchar(20) default '',
  `req_json_str` text default '',
  `rtn_json_str` text default '',
  `rtn_code` varchar(255) default '',
  `rtn_msg` text default '',
  `done_datetime` varchar(20) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no on t_simple_rr_async_log_entity(`no`);


-- SimpleRrAuditLogEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_rr_audit_log_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `channel_id` varchar(40) default '',
  `tenant_id` varchar(40) default '',
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

create index if not exists no on t_simple_rr_audit_log_entity(no);


-- SimpleTenantEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_tenant_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `name` varchar(255) default '',
  `description` varchar(255) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_no on t_simple_tenant_entity(`no`);
create unique index if not exists u_idx_name on t_simple_tenant_entity(`name`);


-- SimpleUserEntityTest_createTableAndIndexIfNotExistSql_null_o.txt
create table if not exists `t_simple_user_entity` (
  `id` char(40) not null,
  `ver` int not null default 0,
  `dd` varchar(40) default 'N',
  `ivd` varchar(1) default 'N',
  `lm_dsz` varchar(20) default '',
  `lm_uid` varchar(40) default '',
  `no` varchar(40) default '',
  `tenant_id` varchar(40) default '',
  `name` varchar(30) default '',
  `haha` varchar(255) default '',
  `mobile` varchar(20) default '',
  `email` varchar(40) default '',
  primary key (`id`)
);

create unique index if not exists u_idx_tenant_id_no on t_simple_user_entity(`tenant_id`,`no`);



insert into t_simple_tenant_entity (id, no, lm_dsz, lm_uid, name, description)
select '1612262610215_LoHqeZBGrVYm3MlYmpH','tstTenantNo','','1612262610216_koFVLCNZrhezbgULWqW','tstTenantName','tstTenantDesc';
insert into t_simple_user_entity (id, no, lm_dsz, lm_uid, tenant_id, name, haha, mobile, email)
select '1612262610216_koFVLCNZrhezbgULWqW','tstUserNo','','1612262610216_koFVLCNZrhezbgULWqW','1612262610215_LoHqeZBGrVYm3MlYmpH','tstUserName','[SKC1]494c6f7665596f75','18888888888','email@email.com';
insert into t_simple_channel_entity (id, no, lm_dsz, lm_uid, name, description, enc_tv, enc_tf, enc_tat, enc_tvt)
select '1612263653223_oGFvE5Hyndf0njoFhyK','tstChannelNo','','1612262610216_koFVLCNZrhezbgULWqW','tstChannelName','tstChannelDesc','494c6f7665596f75','N','SKC1','SELF';
insert into t_simple_channel_access_regex_api_entity (id, no, lm_dsz, lm_uid, channel_id, allow_url_regex, allow_signature_regex, deny_url_regex, deny_signature_regex)
select '1612263668482_jaHu6tmguyKo2xWgHPj','','','1612262610216_koFVLCNZrhezbgULWqW','1612263653223_oGFvE5Hyndf0njoFhyK','','^[\s\S]*(add|rmv|mod|mge|lst)\([\s\S]*$','','';


select * from t_simple_channel_access_regex_api_entity;
select * from t_simple_channel_access_signature_api_entity;
select * from t_simple_channel_access_tenant_regex_api_entity;
select * from t_simple_channel_access_tenant_signature_api_entity;
select * from t_simple_channel_access_tenant_url_api_entity;
select * from t_simple_channel_access_url_api_entity;
select * from t_simple_channel_entity;
select * from t_simple_rr_async_log_entity;
select * from t_simple_rr_audit_log_entity;
select * from t_simple_tenant_entity;
select * from t_simple_user_entity;


drop table if exists t_simple_user_entity_d;
create table if not exists t_simple_user_entity_d as select * from t_simple_user_entity where 1=0;
select * from t_simple_user_entity_d;
vacuum;
