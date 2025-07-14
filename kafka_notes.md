# Apache Kafka Study Notes

## What is Apache Kafka?

Apache Kafka is a distributed streaming platform designed for building real-time data pipelines and streaming applications. It's horizontally scalable, fault-tolerant, and provides high-throughput, low-latency message processing.

**Key Characteristics:**
- Distributed system that runs as a cluster of brokers
- Handles trillions of events per day
- Provides durability through replication
- Supports both real-time and batch processing

## Core Concepts

### Topics and Partitions
- **Topic**: A category or feed name to which messages are published
- **Partition**: Topics are divided into partitions for parallelism and scalability
- Messages within a partition are ordered and immutable
- Each partition can be replicated across multiple brokers for fault tolerance

### Brokers
- Kafka servers that store and serve data
- Each broker handles multiple partitions
- Brokers form a cluster managed by Zookeeper (or KRaft in newer versions)

### Messages (Records)
- Basic unit of data in Kafka
- Contains key, value, timestamp, and optional headers
- Immutable once written to a partition

## Producer

The Producer is responsible for publishing messages to Kafka topics.

### Key Features:
- **Asynchronous**: Sends messages without waiting for acknowledgment by default
- **Batching**: Groups messages together for efficiency
- **Compression**: Supports GZIP, Snappy, LZ4, ZSTD
- **Partitioning**: Determines which partition to send messages to

### Producer Configuration:
```properties
bootstrap.servers=localhost:9092
key.serializer=org.apache.kafka.common.serialization.StringSerializer
value.serializer=org.apache.kafka.common.serialization.StringSerializer
acks=all
retries=3
batch.size=16384
linger.ms=5
```

### Acknowledgment Modes:
- **acks=0**: Fire and forget (no acknowledgment)
- **acks=1**: Leader acknowledgment only
- **acks=all/-1**: All in-sync replicas must acknowledge

### Partitioning Strategies:
- **Round-robin**: If no key is provided
- **Hash-based**: If key is provided (hash of key % number of partitions)
- **Custom**: Using custom partitioner class

## Consumer

The Consumer reads messages from Kafka topics.

### Key Features:
- **Pull-based**: Consumers pull messages from brokers
- **Offset management**: Tracks position in each partition
- **Parallel processing**: Multiple consumers can read from different partitions
- **Fault tolerance**: Can resume from last committed offset

### Consumer Configuration:
```properties
bootstrap.servers=localhost:9092
group.id=my-consumer-group
key.deserializer=org.apache.kafka.common.serialization.StringDeserializer
value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
auto.offset.reset=earliest
enable.auto.commit=true
auto.commit.interval.ms=1000
```

### Offset Management:
- **Current Offset**: Next message to be consumed
- **Committed Offset**: Last successfully processed message
- **Auto-commit**: Automatically commits offsets at intervals
- **Manual commit**: Explicit control over when to commit

### Auto Offset Reset:
- **earliest**: Start from beginning of partition
- **latest**: Start from end of partition (default)
- **none**: Throw exception if no offset found

## Consumer Group

A Consumer Group is a set of consumers that work together to consume a topic.

### Key Concepts:
- **Load balancing**: Partitions are distributed among consumers in the group
- **Fault tolerance**: If a consumer fails, its partitions are reassigned
- **Scalability**: Add more consumers to increase throughput
- **Unique consumption**: Each message is delivered to only one consumer in the group

### Rules:
- Maximum consumers in a group = number of partitions
- Each partition is assigned to exactly one consumer in the group
- Multiple consumer groups can read from the same topic independently

### Consumer Group Coordinator:
- Manages group membership and partition assignment
- Handles consumer failures and rebalancing
- Tracks offsets for the group

## Kafka Lag

Kafka Lag represents the delay between message production and consumption.

### Types of Lag:
- **Consumer Lag**: Difference between latest offset and consumer's current offset
- **End-to-end Lag**: Time from message production to consumption
- **Partition Lag**: Lag per partition

### Monitoring Lag:
```bash
# Check consumer group lag
kafka-consumer-groups --bootstrap-server localhost:9092 --group my-group --describe

# Output shows:
# TOPIC, PARTITION, CURRENT-OFFSET, LOG-END-OFFSET, LAG, CONSUMER-ID
```

### Causes of Lag:
- Slow message processing
- Consumer failures
- Insufficient consumer instances
- Network issues
- Broker performance problems

### Reducing Lag:
- Increase number of consumers (up to partition count)
- Optimize message processing logic
- Increase consumer fetch size
- Use async processing where possible
- Scale broker resources

## Kafka Rebalance

Rebalancing is the process of reassigning partitions to consumers within a consumer group.

### When Rebalance Occurs:
- Consumer joins the group
- Consumer leaves the group (graceful shutdown)
- Consumer fails (detected by heartbeat timeout)
- New partitions are added to subscribed topics
- Consumer group subscription changes

### Rebalance Process:
1. **Stop consumption**: All consumers stop processing messages
2. **Revoke partitions**: Current partition assignments are revoked
3. **Reassign partitions**: New assignments are calculated
4. **Resume consumption**: Consumers start processing with new assignments

### Rebalance Strategies:
- **Range**: Assigns consecutive partitions to consumers
- **Round-robin**: Distributes partitions evenly in round-robin fashion
- **Sticky**: Minimizes partition movement during rebalance
- **Cooperative Sticky**: Allows incremental rebalancing (Kafka 2.4+)

### Rebalance Configuration:
```properties
# Heartbeat interval
heartbeat.interval.ms=3000

# Session timeout
session.timeout.ms=30000

# Max poll interval
max.poll.interval.ms=300000

# Rebalance strategy
partition.assignment.strategy=org.apache.kafka.clients.consumer.CooperativeStickyAssignor
```

## Kafka Connect

Kafka Connect is a framework for connecting Kafka with external systems.

### Key Features:
- **Scalable**: Distributed framework for data integration
- **Fault tolerant**: Handles failures gracefully
- **No coding required**: Configuration-driven
- **Pluggable**: Extensive ecosystem of connectors

### Architecture:
- **Connect Cluster**: Group of worker processes
- **Connectors**: High-level abstraction for data copying
- **Tasks**: Actual work units that copy data
- **Workers**: Processes that run connectors and tasks

### Deployment Modes:
- **Standalone**: Single process (development/testing)
- **Distributed**: Multiple workers in cluster (production)

### Configuration:
```properties
# Worker configuration
bootstrap.servers=localhost:9092
group.id=connect-cluster
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter
offset.storage.topic=connect-offsets
config.storage.topic=connect-configs
status.storage.topic=connect-status
```

## Source Connectors

Source connectors import data from external systems into Kafka.

### Common Source Connectors:
- **Database**: MySQL, PostgreSQL, Oracle, SQL Server
- **File systems**: HDFS, S3, local files
- **Message queues**: ActiveMQ, RabbitMQ
- **APIs**: REST APIs, web services
- **Streaming**: Twitter, Kinesis

### Example - File Source Connector:
```json
{
  "name": "file-source",
  "config": {
    "connector.class": "FileStreamSource",
    "tasks.max": "1",
    "file": "/path/to/input.txt",
    "topic": "file-topic"
  }
}
```

### Key Concepts:
- **Offset management**: Tracks position in source system
- **Exactly-once delivery**: Prevents duplicate records
- **Schema evolution**: Handles changes in source data structure

## Sink Connectors

Sink connectors export data from Kafka to external systems.

### Common Sink Connectors:
- **Databases**: MySQL, PostgreSQL, Cassandra
- **Search engines**: Elasticsearch, Solr
- **Data warehouses**: Snowflake, BigQuery, Redshift
- **File systems**: HDFS, S3
- **Analytics**: Tableau, Grafana

### Example - Elasticsearch Sink Connector:
```json
{
  "name": "elasticsearch-sink",
  "config": {
    "connector.class": "ElasticsearchSinkConnector",
    "tasks.max": "1",
    "topics": "my-topic",
    "connection.url": "http://localhost:9200",
    "type.name": "doc"
  }
}
```

### Key Features:
- **Transformation**: Data can be transformed before writing
- **Error handling**: Dead letter queues for failed records
- **Exactly-once delivery**: Prevents duplicate writes

## Schema Registry

Schema Registry provides a centralized repository for managing Avro, JSON, and Protobuf schemas.

### Key Benefits:
- **Schema evolution**: Backward/forward compatibility
- **Data governance**: Centralized schema management
- **Serialization efficiency**: Compact binary format
- **Type safety**: Compile-time schema validation

### Schema Evolution Rules:
- **Backward compatibility**: New schema can read old data
- **Forward compatibility**: Old schema can read new data
- **Full compatibility**: Both backward and forward compatible

### Compatibility Types:
- **BACKWARD**: Default, new schema can read old data
- **FORWARD**: Old schema can read new data
- **FULL**: Both backward and forward compatible
- **NONE**: No compatibility checking

### Using Schema Registry:
```java
// Producer with Avro
Properties props = new Properties();
props.put("schema.registry.url", "http://localhost:8081");
props.put("key.serializer", KafkaAvroSerializer.class);
props.put("value.serializer", KafkaAvroSerializer.class);

// Consumer with Avro
props.put("key.deserializer", KafkaAvroDeserializer.class);
props.put("value.deserializer", KafkaAvroDeserializer.class);
props.put("specific.avro.reader", "true");
```

### Schema Registry REST API:
```bash
# List subjects
curl -X GET http://localhost:8081/subjects

# Get latest schema
curl -X GET http://localhost:8081/subjects/my-topic-value/versions/latest

# Register new schema
curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  --data '{"schema":"{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"id\",\"type\":\"int\"},{\"name\":\"name\",\"type\":\"string\"}]}"}' \
  http://localhost:8081/subjects/my-topic-value/versions
```

## Best Practices

### Producer Best Practices:
- Use appropriate serializers for your data
- Set proper `acks` level based on durability requirements
- Enable compression for better throughput
- Use batching for better performance
- Handle producer exceptions properly

### Consumer Best Practices:
- Process messages idempotently
- Commit offsets after successful processing
- Handle rebalances gracefully
- Monitor consumer lag
- Use appropriate `auto.offset.reset` strategy

### General Best Practices:
- Monitor cluster health and performance
- Plan for capacity and scaling
- Implement proper error handling
- Use meaningful topic and consumer group names
- Regular backup of important topics
- Keep Kafka version updated

## Common Commands

```bash
# Create topic
kafka-topics --create --topic my-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

# List topics
kafka-topics --list --bootstrap-server localhost:9092

# Describe topic
kafka-topics --describe --topic my-topic --bootstrap-server localhost:9092

# Console producer
kafka-console-producer --topic my-topic --bootstrap-server localhost:9092

# Console consumer
kafka-console-consumer --topic my-topic --bootstrap-server localhost:9092 --from-beginning

# Consumer groups
kafka-consumer-groups --bootstrap-server localhost:9092 --list
kafka-consumer-groups --bootstrap-server localhost:9092 --group my-group --describe
```