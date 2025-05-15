#!/bin/bash

ROOT_DIR=$(dirname "$(dirname "$0")")
cd "$ROOT_DIR"

set -a
source .env
set +a

TAG=${1:-latest}
FULL_IMAGE_NAME=$DOCKER_USERNAME/$IMAGE_NAME:$TAG

echo "✅ Gradle 빌드 시작..."
./gradlew bootJar || { echo "❌ 빌드 실패"; exit 1; }

JAR_FILE=$(ls build/libs/*.jar | head -n 1)

echo "✅ Docker 이미지 빌드 중: $FULL_IMAGE_NAME"
docker build -t $FULL_IMAGE_NAME .

echo "✅ 이미지 빌드 완료!"
