CREATE TABLE if not exists oid_group(group_name TEXT NOT NULL,uuid TEXT NOT NULL PRIMARY KEY);
CREATE TABLE if not exists oid(oid_id TEXT NOT NULL PRIMARY KEY, oid TEXT NOT NULL,oid_name TEXT,group_id TEXT);
CREATE TABLE if not exists device(device_id TEXT NOT NULL PRIMARY KEY,device_ip TEXT NOT NULL, device_name TEXT, sys_id_value TEXT);
CREATE TABLE if not exists oid_value(value_id TEXT NOT NULL PRIMARY KEY,oid_id TEXT NOT NULL, device_id TEXT, oid_value TEXT, value_time TEXT);
CREATE TABLE if not exists device_group(device_group_id TEXT NOT NULL PRIMARY KEY,group_id TEXT NOT NULL, device_id TEXT NOT NULL);