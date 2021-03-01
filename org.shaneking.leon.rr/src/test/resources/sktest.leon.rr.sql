drop table if exists t_hello_tenant_entity;
drop table if exists t_hello_user_entity;
drop table if exists t_hello_channel_entity;
drop table if exists t_hello_api_access_entity;
drop table if exists t_hello_audit_log_entity;



delete
from t_hello_tenant_entity;
delete
from t_hello_user_entity;
delete
from t_hello_channel_entity;
delete
from t_hello_api_access_entity;
delete
from t_hello_audit_log_entity;

insert into t_hello_tenant_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, name, description)
select 0, '1612262610215_LoHqeZBGrVYm3MlYmpH', 'N', '', '', 'tsttenant', 'tsttenant';
insert into t_hello_user_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, tenant_id, no, name, haha, mobile, email)
select 0,
       '1612262610216_koFVLCNZrhezbgULWqW',
       'N',
       '',
       '',
       '1612262610215_LoHqeZBGrVYm3MlYmpH',
       'tstuserno',
       'tstusername',
       '[C3]494c6f7665596f75',
       '18888888888',
       'email@email.com';
insert into t_hello_channel_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, tenant_id, name, description, token_value, token_force, token_algorithm_type, token_value_type)
select 0,
       '1612263653223_oGFvE5Hyndf0njoFhyK',
       'N',
       '',
       '',
       '1612262610215_LoHqeZBGrVYm3MlYmpH',
       'tstchannel',
       'tsttenant',
       '494c6f7665596f75',
       'N',
       'C3',
       'SELF';
insert into t_hello_api_access_entity (version, id, invalid, last_modify_date_time, last_modify_user_id, tenant_id, channel_id, allow_url_regex, allow_signature_regex, deny_url_regex, deny_signature_regex)
select 0,
       '1612263668482_jaHu6tmguyKo2xWgHPj',
       'N',
       '',
       '',
       '1612262610215_LoHqeZBGrVYm3MlYmpH',
       '1612263653223_oGFvE5Hyndf0njoFhyK',
       '^[\s\S]*(user/mge|user/delById)\([\s\S]*$',
       '^[\s\S]*(add|lst)\([\s\S]*$',
       '',
       '';

select *
from t_hello_tenant_entity;
select *
from t_hello_user_entity;
select *
from t_hello_channel_entity;
select *
from t_hello_api_access_entity;
select *
from t_hello_audit_log_entity;

vacuum;
