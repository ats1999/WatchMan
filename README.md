# WatchMan

An query and alerting engine...!

Kafka Installation
https://github.com/apache/kafka/blob/trunk/docker/examples/README.md

```sh
docker pull apache/kafka:latest
docker run -p 9092:9092 apache/kafka
```

MySql Docker

```sh
docker pull mysql
docker run --name mysql -e MYSQL_ROOT_PASSWORD=123456  -p 3308:3306 -d mysql
```

Create Meta Data Base

```sql
# login
mysql -p

# create database
CREATE DATABASE meta
```

Api key mapping

```sql
SELECT user_events.user_user_name AS user_name,
       event_api_keys.event_event_id AS event_id,
       api_key.api_key,
       api_key.expiry_time
FROM api_key
JOIN event_api_keys ON api_key.id = event_api_keys.api_keys_id
JOIN user_events ON user_events.events_event_id = event_api_keys.event_event_id
WHERE api_key='51018598c935462189a4941d57388456';
```

Send Log => Pulish to kafka for validation => store data into clickhouse once validated => query data
