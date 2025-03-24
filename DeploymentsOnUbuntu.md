## Ubuntu 的 JDK 17 + Cassandra 5 + Kafka 3.9

#### 安装 JDK 17 https://adoptium.net/installation/linux/

```
apt install -y wget apt-transport-https gpg
wget --no-check-certificate -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
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
systemctl status cassandra
nodetool status
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

/etc/kafka/bin/kafka-storage.sh random-uuid  # 用于 /etc/systemd/system/kafka.service 的 ExecStartPre
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
