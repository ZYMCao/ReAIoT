## Ubuntu 的 JAVA 生态圈

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

## Ubuntu 的 Python 生态圈
#### 安装 Python 3.11.11 (根据情况，更新版本) https://radwanelourhmati7.medium.com/installing-python-3-11-on-ubuntu-step-by-step-a46631d4e293
```
cd /opt
apt update
apt upgrade
apt install -y pkg-config build-essential zlib1g-dev libncurses5-dev libgdbm-dev libnss3-dev libssl-dev libreadline-dev libffi-dev libsqlite3-dev libbz2-dev

wget https://www.python.org/ftp/python/3.11.11/Python-3.11.11.tgz --no-check-certificate
tar -xf Python-3.11.11.tgz
cd Python-3.11.11
./configure --enable-optimizations --with-lto --enable-shared
make -j$(nproc)
make altinstall

update-alternatives --set python3 /usr/local/bin/python3.11
python3 --version
update-alternatives --config python3
```

#### Pytorch
```
python3 -m ensurepip --upgrade
python3 -m pip install --upgrade pip
python3 -m pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu

python3 -c "import torch; import torchvision; print(torch.__version__, torchvision.__version__)"


```
```
Installing collected packages: mpmath, typing-extensions, sympy, pillow, numpy, networkx, MarkupSafe, fsspec, filelock, jinja2, torch, torchvision, torchaudio
WARNING: The script isympy is installed in '/usr/local/bin' which is not on PATH.
Consider adding this directory to PATH or, if you prefer to suppress this warning, use --no-warn-script-location.
WARNING: The scripts f2py and numpy-config are installed in '/usr/local/bin' which is not on PATH.
Consider adding this directory to PATH or, if you prefer to suppress this warning, use --no-warn-script-location.
WARNING: The scripts torchfrtrace and torchrun are installed in '/usr/local/bin' which is not on PATH.
Consider adding this directory to PATH or, if you prefer to suppress this warning, use --no-warn-script-location.
Successfully installed MarkupSafe-2.1.5 filelock-3.13.1 fsspec-2024.6.1 jinja2-3.1.4 mpmath-1.3.0 networkx-3.3 numpy-2.1.2 pillow-11.0.0 sympy-1.13.1 torch-2.6.0+cpu torchaudio-2.6.0+cpu torchvision-0.21.0+cpu typing-extensions-4.12.2
WARNING: Running pip as the 'root' user can result in broken permissions and conflicting behaviour with the system package manager, possibly rendering your system unusable. It is recommended to use a virtual environment instead: https://pip.pypa.io/warnings/venv. Use the --root-user-action option if you know what you are doing and want to suppress this warning.
```

ModuleNotFoundError: No module named '_lzma' 修复
```
apt-get update
apt-get install liblzma-dev libbz2-dev libncurses5-dev libreadline-dev libssl-dev zlib1g-dev libsqlite3-dev libffi-dev
make clean
./configure --enable-optimizations --with-lto --enable-shared


```

## Ubuntu 的升级

#### 查看当前版本

```
lsb_release -a
```

#### 升级
```
apt update && apt upgrade -y
apt dist-upgrade -y
apt autoremove -y

apt install update-manager-core

do-release-upgrade -d
```