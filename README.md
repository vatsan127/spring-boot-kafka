## Kafka AVro Implementation

- feature/kafka-avro

## Schema Registry URL

- Get All Subjects

```
curl -X GET http://<schema-registry-url>/subjects"
```

- Register a Schema

```
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "schema": "{\"type\":\"record\",\"name\":\"myrecord\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"}]}"
  }' \
  "http://<schema-registry-url>/subjects/<subject-name>/versions"
```

- Get Schema by Subject Name and Version

```
  curl -X GET "http://<schema-registry-url>/subjects/<subject-name>/versions/<version-number>"
```

- Get the Latest Schema for a Subject

```
curl -X GET "http://<schema-registry-url>/subjects/<subject-name>/versions/latest"
```

- Delete the schema by ID

```
curl -X DELETE "http://<schema-registry-url>/subjects/<subject-name>/versions/<version-number>"
```

- Delete all versions of a subject

```
curl -X DELETE "http://<schema-registry-url>/subjects/<subject-name>"
```

## Avro Console Command

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --list

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --delete --topic <TOPIC_NAME>

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --create --topic <TOPIC_NAME> --replication-factor 1 --partitions
> 1

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --describe --topic <TOPIC_NAME>

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --describe --topic <TOPIC_NAME>

> kafka-topics --bootstrap-server <BROKER_HOST>:<PORT> --alter --topic <TOPIC_NAME> --partitions 40

> kafka-console-producer --bootstrap-server <BROKER_HOST>:<PORT> --topic <TOPIC_NAME>

> kafka-console-producer --bootstrap-server <BROKER_HOST>:<PORT> --topic <TOPIC_NAME> --property "key.separator=-"
> --property "parse.key=true"

> kafka-console-consumer --bootstrap-server <BROKER_HOST>:<PORT> --topic <TOPIC_NAME> --from-beginning

> kafka-console-consumer --bootstrap-server <BROKER_HOST>:<PORT> --topic <TOPIC_NAME> --from-beginning --property "
> key.separator= - " --property "print.key=true"

> kafka-avro-console-consumer \
> --bootstrap-server <BROKER> \
> --topic <TOPIC_NAME> \
> --from-beginning \
> --group <GROUP_ID> \
> --property schema.registry.url=<SCHEMA_REGISTRY_URL>


> kafka-avro-console-producer \
> --bootstrap-server <BROKER> \
> --topic <TOPIC_NAME> \
> --property schema.registry.url=<SCHEMA_REGISTRY_URL> \
> --key-schema-file <KEY_SCHEMA_PATH> \
> --value-schema-file <VALUE_SCHEMA_PATH>
