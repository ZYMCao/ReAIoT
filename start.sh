#!/bin/bash

JAVA_WHERE="/usr/lib/jvm/temurin-21-jdk-amd64/bin/java"
JAR_FILE="agentic-0.1.NorthSea.jar"

echo "AgenticApplication starting ..."

nohup $JAVA_WHERE -Dloader.main=cn.easttrans.reaiot.agentic.AgenticApplication \
            -DBEAM_CONSTRUCTION_USERNAME=nylcsuper \
            -DBEAM_CONSTRUCTION_PASSWORD=nylc@888888 \
            -DDEEPSEEK_API_KEY=sk-yrhmhiwiewjtqvdfcbiyxlbqbxbuwgisbsglyxgrtfrqpeql \
            -DDEEPSEEK_BASE_URL=https://api.siliconflow.cn \
            -DDEEPSEEK_MODEL=deepseek-ai/DeepSeek-V3 \
            -Dlogging.config=/opt/reaiot/northsea/config/logback-spring.xml \
            -jar $JAR_FILE > /dev/null 2>&1 &

echo "Done!! Pay attention to /var/log/northsea/reaiot.log"
