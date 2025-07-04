#!/bin/bash

# 로컬 테스트용 실행 스크립트
# H2 인메모리 데이터베이스 사용
# 로그는 콘솔에만 출력 (파일 생성 안함)

echo "=== 로컬 테스트 환경 실행 시작 ==="

# 환경 변수 설정
export SPRING_PROFILES_ACTIVE=test

echo "Spring Profile: $SPRING_PROFILES_ACTIVE"
echo "데이터베이스: H2 인메모리"
echo "로그 출력: 콘솔만 (파일 생성 안함)"

# m-common 모듈 실행
echo "m-common 모듈 실행 중..."
./gradlew :m-common:bootRun

echo "=== 로컬 테스트 환경 실행 완료 ==="
echo ""
echo "접속 정보:"
echo "  - 애플리케이션: http://localhost:9090"
echo "  - H2 콘솔: http://localhost:9090/h2-console"
echo "  - Swagger: http://localhost:9090/api/v1/docs"
echo ""
echo "로그 확인:"
echo "  - 콘솔에서 직접 확인"
echo "  - 로그 파일은 생성되지 않습니다 (테스트 환경)" 