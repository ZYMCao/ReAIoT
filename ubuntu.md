## Ubuntu 的 JDK 17 + Cassandra 5 + Kafka 3.9

#### 安装 jdk 17 https://adoptium.net/installation/linux/
```
apt install -y wget apt-transport-https gpg
wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list
apt update
apt install temurin-17-jdk
java -version
```
#### 安装 Cassandra 5 https://cassandra.apache.org/doc/stable/cassandra/getting_started/installing.html
```
echo "deb [signed-by=/etc/apt/keyrings/apache-cassandra.asc] https://debian.cassandra.apache.org 50x main" | tee -a /etc/apt/sources.list.d/cassandra.sources.list
curl -o /etc/apt/keyrings/apache-cassandra.asc https://downloads.apache.org/cassandra/KEYS
apt-get update
apt-get install cassandra
```

#### 修改 /etc/cassandra/cassandra.yaml (两个节点的案例)
```
cluster_name: 'ReAIoT Cluster'
listen_address: 10.0.0.66
rpc_address: 0.0.0.0
broadcast_rpc_address: 10.0.0.66
seeds: "10.0.0.66,10.0.0.68"
```

```
cluster_name: 'ReAIoT Cluster'
listen_address: 10.0.0.68
rpc_address: 0.0.0.0
broadcast_rpc_address: 10.0.0.68
seeds: "10.0.0.66,10.0.0.68"
```

#### 安装 Kafka 3.9 https://medium.com/@vonschnappi/setting-up-kafka-with-kraft-in-aws-837310466591
```
adduser --no-create-home  --shell=/sbin/nologin --disabled-password --disabled-login --gecos "" kafka
mkdir -p /data/kafka
chown -R kafka:kafka /data/kafka

mkdir -p /var/log/kafka
chown -R kafka:kafka /var/log/kafka

mkdir -p /etc/kafka
curl https://downloads.apache.org/kafka/3.9.0/kafka_2.13-3.9.0.tgz -o /etc/kafka/kafka.tgz
tar -xzvf /etc/kafka/kafka.tgz -C /etc/kafka --strip 1
rm kafka.tgz
chown -R kafka:kafka /etc/kafka
```

#### 修改 /etc/kafka/config/kraft/server.properties (两个节点的案例)
```
node.id=1 
controller.quorum.voters=1@10.0.0.68:9093,2@10.0.0.66:9093
advertised.listeners=PLAINTEXT://10.0.0.66:9092,CONTROLLER://10.0.0.66:9093
log.dirs=/data/kafka/kraft-combined-logs
```

```
node.id=2
controller.quorum.voters=1@10.0.0.68:9093,2@10.0.0.66:9093
advertised.listeners=PLAINTEXT://10.0.0.66:9092,CONTROLLER://10.0.0.66:9093
log.dirs=/data/kafka/kraft-combined-logs
```

#### 用于 /etc/systemd/system/kafka.service 的 ExecStartPre
```
/etc/kafka/bin/kafka-storage.sh random-uuid
```

#### 创建 /etc/systemd/system/kafka.service
```
[Unit]
Description=Apache Kafka Server (Broker)
Requires=network.target
After=network.target

[Service]
Type=simple
User=kafka
Environment=KAFKA_HEAP_OPTS="-Xmx1G -Xms1G"
Environment=KAFKA_JVM_PERFORMANCE_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 -XX:+ExplicitGCInvokesConcurrent"
ExecStartPre=/bin/bash -c '/etc/kafka/bin/kafka-storage.sh format -t 7GupSuu6Tp-a8Ghx5zjDFQ -c /etc/kafka/config/kraft/server.properties --ignore-formatted'
ExecStart=/bin/bash -c '/etc/kafka/bin/kafka-server-start.sh /etc/kafka/config/kraft/server.properties'
ExecStop=/etc/kafka/bin/kafka-server-stop.sh
Environment="LOG_DIR=/var/log/kafka"
Restart=on-failure
LimitNOFILE=65536

[Install]
WantedBy=multi-user.target
```

#### 生效 /etc/systemd/system/kafka.service
```
systemctl daemon-reload
systemctl enable kafka
systemctl start kafka
systemctl status kafka
```


## PostgreSQL 数据库
```
-u postgres psql
systemctl start postgresql
systemctl stop postgresql
systemctl status postgresql
journalctl -u postgresql -f
```

#### 日志 /var/log/postgresql/postgresql-15-main.log
#### 数据 /var/lib/postgresql/15/main
#### 身份配置文件 /etc/postgresql/15/main/pg_hba.conf
#### 运行时pid /var/run/postgresql/15-main.pid
#### 核心配置文件 /etc/postgresql/15/main/postgresql.conf
#### https://blog.csdn.net/windows_2006/article/details/137342998

## Apache Cassandra 数据库
```
cqlsh
nodetool status
systemctl start cassandra
systemctl stop cassandra
systemctl status cassandra
netstat -tulpn | grep 9042
journalctl -u cassandra -f
```

#### 日志 /var/log/cassandra
#### 数据/var/lib/cassandra
#### 核心配置文件 /etc/cassandra
#### 其它配置文件 /etc/default/cassandra
#### https://cassandra.apache.org/_/download.html
#### https://blog.csdn.net/u011250186/article/details/102455694

## Apache Kafka
```
systemctl start kafka
systemctl stop kafka
systemctl status kafka
journalctl -u kafka -f
```
#### https://kafka.apache.org/quickstart
#### Create topic:
```
-u kafka /opt/kafka/bin/kafka-topics.sh \
  --create \
  --topic test-topic \
  --bootstrap-server localhost:9092 \
  --partitions 1 \
  --replication-factor 1
```
#### Producer:
```
-u kafka /opt/kafka/bin/kafka-console-producer.sh \
  --topic test-topic \
  --bootstrap-server localhost:9092
```
#### Consumer:
```
-u kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --topic test-topic \
  --from-beginning \
  --bootstrap-server localhost:9092
```


#### https://gist.github.com/kovalchukrs/b088722bcb6cdcb2ec200a8029b91ba4
#### /opt/kafka/config/kraft/server.properties 要把 controller.quorum.voters 和 advertised.listeners 的 localhost 改成服务器的地址，才能让外部服务器访问Kafka

## 服务器的端口开放计划
#### 8080 HTTP
#### 9090 HTTP 备用
#### 1883 MQTT
#### 3881 MQTT 备用
#### 7070 Edge RPC
#### 5683-5688 UDP, COAP, LwM2M
#### 5432 PostgreSQL
#### 9092, 9093 Kafka
#### 9042, 9043 Cassandra
#### 10560-10570 TCP 备用


## 字体报错
```
apt-get update
apt-get install -y fontconfig fonts-dejavu-core
fc-cache -fv
export JAVA_OPTS="$JAVA_OPTS -Djava.awt.headless=true -Dsun.java2d.fontpath=/usr/share/fonts"
```

## Redis
```
systemctl status redis-server
```

#### 日志 /var/log/redis
#### 数据 /var/lib/redis
#### 配置文件 /etc/redis/redis.conf

## Thingsboard
#### /opt/thingsboard/systemmd/thingsboard.service
```
[Unit]
Description=物联看板
After=syslog.target
Requires=syslog.target postgresql.service cassandra.service kafka.service

[Service]
User=thingsboard
Group=sudo
Environment="JAVA_HOME=/usr/lib/jvm/temurin-11-jdk-amd64"
ExecStart=/opt/thingsboard/start.sh
ExecStop=/opt/thingsboard/stop.sh
Restart=on-failure
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```


