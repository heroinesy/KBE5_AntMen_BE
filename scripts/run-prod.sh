#!/bin/bash

# 운영 환경용 실행 스크립트
# 로그 파일 생성 및 S3 백업 포함

echo "=== 운영 환경 실행 시작 ==="

# 환경 변수 설정
export SPRING_PROFILES_ACTIVE=prod

echo "Spring Profile: $SPRING_PROFILES_ACTIVE"
echo "로그 디렉토리: /logs/"
echo "로그 파일: 날짜별 폴더에 생성"

# 로그 디렉토리 설정 확인
if [ ! -d "/logs" ]; then
    echo "로그 디렉토리가 없습니다. 설정 스크립트를 실행하세요:"
    echo "  ./scripts/setup-logging.sh"
    exit 1
fi

# m-common 모듈 실행
echo "m-common 모듈 실행 중..."
./gradlew :m-common:bootRun

echo "=== 운영 환경 실행 완료 ==="
echo ""
echo "로그 확인:"
echo "  - tail -f /logs/m-common/application.log"
echo "  - tail -f /logs/m-common/error.log"
echo "  - ls -la /logs/m-common/\$(date +%Y-%m-%d)/"
echo ""
echo "S3 백업:"
echo "  - ./scripts/log-backup-s3.sh" 