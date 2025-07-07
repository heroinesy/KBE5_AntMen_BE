#!/bin/bash

# 로그 디렉토리 생성 및 권한 설정 스크립트
# EC2 배포 시 실행

echo "=== 원격 서버 로깅 시스템 디렉토리 설정 시작 ==="

# 루트 로그 디렉토리 생성 (절대 경로)
LOG_ROOT="/logs"
echo "루트 로그 디렉토리 생성: $LOG_ROOT"
sudo mkdir -p $LOG_ROOT
sudo chown -R $USER:$USER $LOG_ROOT
sudo chmod -R 755 $LOG_ROOT

# 각 모듈별 로그 디렉토리 생성
MODULES=("m-common" "m-customer" "m-manager" "m-admin")

for module in "${MODULES[@]}"; do
    echo "모듈 로그 디렉토리 생성: $LOG_ROOT/$module"
    mkdir -p "$LOG_ROOT/$module"
    
    # 기본 로그 파일들 생성 (권한 문제 방지)
    touch "$LOG_ROOT/$module/application.log"
    touch "$LOG_ROOT/$module/error.log"
    touch "$LOG_ROOT/$module/access.log"
    touch "$LOG_ROOT/$module/business.log"
    touch "$LOG_ROOT/$module/performance.log"
    
    # Thread Dump 디렉토리 생성
    mkdir -p "$LOG_ROOT/$module/thread-dumps"
    
    # 권한 설정
    chmod 644 "$LOG_ROOT/$module"/*.log
    chmod 755 "$LOG_ROOT/$module/thread-dumps"
    
    echo "  - $module 로그 디렉토리 설정 완료"
done

# 시스템 서비스용 권한 설정
echo "시스템 서비스 권한 설정..."
sudo chown -R root:root $LOG_ROOT
sudo chmod -R 755 $LOG_ROOT

# Docker 사용자 권한 설정 (필요시)
if command -v docker &> /dev/null; then
    echo "Docker 사용자 권한 설정..."
    if getent group docker > /dev/null 2>&1; then
        sudo chown -R root:docker $LOG_ROOT
        sudo chmod -R 775 $LOG_ROOT
    fi
fi

echo "=== 원격 서버 로깅 시스템 디렉토리 설정 완료 ==="
echo ""
echo "생성된 디렉토리 구조:"
echo "  /logs/"
echo "  ├── m-common/"
echo "  │   ├── application.log"
echo "  │   ├── error.log"
echo "  │   ├── access.log"
echo "  │   ├── business.log"
echo "  │   ├── performance.log"
echo "  │   └── thread-dumps/"
echo "  ├── m-customer/"
echo "  ├── m-manager/"
echo "  └── m-admin/"
echo ""
echo "각 모듈 디렉토리에는 다음 파일들이 포함됩니다:"
echo "  - application.log (애플리케이션 로그)"
echo "  - error.log (에러 로그)"
echo "  - access.log (접근 로그)"
echo "  - business.log (비즈니스 로그)"
echo "  - performance.log (성능 로그)"
echo "  - thread-dumps/ (Thread Dump 저장소)"
echo ""
echo "날짜별 폴더 구조:"
echo "  /logs/m-common/2025-07-02/"
echo "  ├── application.2025-07-02.0.log"
echo "  ├── error.2025-07-02.0.log"
echo "  └── ..."
echo ""
echo "로그 보관 정책:"
echo "  - 최대 3일간 보관"
echo "  - 파일 크기 제한: 50MB-200MB"
echo "  - 총 용량 제한: 500MB-2GB"
echo ""
echo "S3 백업 스크립트: scripts/log-backup-s3.sh"

# 6. 디스크 용량 확인
echo "💾 디스크 용량 확인 중..."
df -h /logs

# 7. 설정 완료 메시지
echo "✅ 로깅 시스템 초기 설정 완료!"
echo ""
echo "📊 생성된 디렉토리:"
echo "   - /logs/ (메인 로그 디렉토리)"
echo "   - /tmp/thread-dumps/ (Thread Dump 저장소)"
echo ""
echo "📝 다음 단계:"
echo "   1. Docker Compose로 애플리케이션 실행"
echo "   2. 로그 파일 자동 생성 확인"
echo "   3. S3 백업 스크립트 설정 (선택사항)" 