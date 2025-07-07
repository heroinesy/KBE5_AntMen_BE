#!/bin/bash

# =====================================================
# 🧹 로그 백업 스크립트 주석처리됨
# =====================================================
# 
# 복잡한 로그 백업 및 S3 업로드 기능을 주석처리했습니다.
# 개발 중에는 간단한 로그만 사용하고,
# 필요시 주석을 해제하여 운영 환경에서 사용할 수 있습니다.
# 
# =====================================================

echo "🧹 로그 백업 스크립트는 현재 주석처리되어 있습니다."
echo "💡 개발 중에는 간단한 콘솔 로그만 사용합니다."
echo "📝 운영 환경에서는 주석을 해제하여 사용하세요."

exit 0

# ========================================================================================
# 🗂️ 아래는 모두 주석처리된 로그 백업 기능들
# ========================================================================================

# 로그 백업 및 정리 스크립트
# 사용법: ./log-backup-s3.sh
# 크론탭 등록: 0 2 * * * /path/to/log-backup-s3.sh >> /var/log/log-backup.log 2>&1

# 설정
LOG_BASE_DIR="/logs"
S3_BUCKET="your-s3-bucket-name"
S3_PREFIX="antmen-logs"
KEEP_DAYS=3
DATE_TODAY=$(date +%Y-%m-%d)
DATE_DELETE=$(date -d "${KEEP_DAYS} days ago" +%Y-%m-%d)

# AWS CLI 확인
if ! command -v aws &> /dev/null; then
    echo "❌ ERROR: AWS CLI가 설치되지 않았습니다."
    exit 1
fi

# S3 버킷 접근 확인
if ! aws s3 ls "s3://${S3_BUCKET}" &> /dev/null; then
    echo "❌ ERROR: S3 버킷 '${S3_BUCKET}'에 접근할 수 없습니다."
    exit 1
fi

echo "🚀 로그 백업 시작: $(date)"
echo "📁 로그 디렉토리: ${LOG_BASE_DIR}"
echo "🪣 S3 버킷: s3://${S3_BUCKET}/${S3_PREFIX}"
echo "📅 삭제 대상 날짜: ${DATE_DELETE} (${KEEP_DAYS}일 이전)"

# 삭제할 날짜의 로그 디렉토리가 있는지 확인
DELETE_DIR="${LOG_BASE_DIR}/${DATE_DELETE}"
if [ -d "$DELETE_DIR" ]; then
    echo "📦 백업 대상 발견: ${DELETE_DIR}"
    
    # 각 모듈별로 백업
    for MODULE_DIR in "${DELETE_DIR}"/*; do
        if [ -d "$MODULE_DIR" ]; then
            MODULE_NAME=$(basename "$MODULE_DIR")
            echo "  📁 모듈: ${MODULE_NAME}"
            
            # 로그 파일들을 압축
            ARCHIVE_NAME="${DATE_DELETE}_${MODULE_NAME}_logs.tar.gz"
            ARCHIVE_PATH="/tmp/${ARCHIVE_NAME}"
            
            echo "    🗜️  압축 중..."
            if tar -czf "$ARCHIVE_PATH" -C "$MODULE_DIR" .; then
                echo "    ✅ 압축 완료: ${ARCHIVE_PATH}"
                
                # S3에 업로드
                S3_KEY="${S3_PREFIX}/${DATE_DELETE}/${ARCHIVE_NAME}"
                echo "    ☁️  S3 업로드 중: ${S3_KEY}"
                
                if aws s3 cp "$ARCHIVE_PATH" "s3://${S3_BUCKET}/${S3_KEY}" \
                   --storage-class STANDARD_IA \
                   --metadata "backup-date=${DATE_TODAY},module=${MODULE_NAME},retention=30days"; then
                    echo "    ✅ S3 업로드 완료"
                    
                    # 임시 압축 파일 삭제
                    rm -f "$ARCHIVE_PATH"
                    echo "    🗑️  임시 파일 삭제"
                else
                    echo "    ❌ S3 업로드 실패"
                    rm -f "$ARCHIVE_PATH"
                    continue
                fi
            else
                echo "    ❌ 압축 실패"
                continue
            fi
        fi
    done
    
    echo "🗑️  로컬 로그 디렉토리 삭제: ${DELETE_DIR}"
    if rm -rf "$DELETE_DIR"; then
        echo "✅ 삭제 완료"
    else
        echo "❌ 삭제 실패"
    fi
else
    echo "📭 삭제할 로그 없음: ${DATE_DELETE}"
fi

# Docker 로그도 백업
echo "🐳 Docker 로그 백업 중..."
DOCKER_LOG_DIR="/var/lib/docker/containers"
DOCKER_BACKUP_DIR="/tmp/docker-logs-${DATE_DELETE}"

if [ -d "$DOCKER_LOG_DIR" ]; then
    mkdir -p "$DOCKER_BACKUP_DIR"
    
    # AntMen 관련 컨테이너 로그만 필터링
    for CONTAINER_DIR in "${DOCKER_LOG_DIR}"/*; do
        if [ -d "$CONTAINER_DIR" ]; then
            CONTAINER_ID=$(basename "$CONTAINER_DIR")
            
            # 컨테이너 이름으로 AntMen 관련인지 확인
            CONTAINER_NAME=$(docker inspect --format='{{.Name}}' "$CONTAINER_ID" 2>/dev/null | sed 's/\///')
            
            if [[ "$CONTAINER_NAME" =~ ^(m-common|m-customer|m-manager|m-admin|apps|admin)$ ]]; then
                echo "  🐳 Docker 컨테이너 로그: ${CONTAINER_NAME}"
                
                # 3일 전 로그만 복사 (날짜 기준 필터링)
                find "$CONTAINER_DIR" -name "*-json.log*" -mtime +$KEEP_DAYS -exec cp {} "$DOCKER_BACKUP_DIR/${CONTAINER_NAME}-{}" \; 2>/dev/null
            fi
        fi
    done
    
    # Docker 로그 압축 및 S3 업로드
    if [ "$(ls -A $DOCKER_BACKUP_DIR 2>/dev/null)" ]; then
        DOCKER_ARCHIVE="/tmp/docker-logs-${DATE_DELETE}.tar.gz"
        
        if tar -czf "$DOCKER_ARCHIVE" -C "$DOCKER_BACKUP_DIR" .; then
            S3_DOCKER_KEY="${S3_PREFIX}/${DATE_DELETE}/docker-logs-${DATE_DELETE}.tar.gz"
            
            if aws s3 cp "$DOCKER_ARCHIVE" "s3://${S3_BUCKET}/${S3_DOCKER_KEY}" \
               --storage-class STANDARD_IA \
               --metadata "backup-date=${DATE_TODAY},type=docker-logs,retention=30days"; then
                echo "  ✅ Docker 로그 S3 업로드 완료"
            else
                echo "  ❌ Docker 로그 S3 업로드 실패"
            fi
            
            rm -f "$DOCKER_ARCHIVE"
        fi
        
        rm -rf "$DOCKER_BACKUP_DIR"
    else
        echo "  📭 백업할 Docker 로그 없음"
    fi
fi

# 오래된 S3 객체 정리 (30일 이상)
echo "🧹 S3 오래된 백업 정리 중..."
CLEANUP_DATE=$(date -d "30 days ago" +%Y-%m-%d)

aws s3api list-objects-v2 \
    --bucket "$S3_BUCKET" \
    --prefix "${S3_PREFIX}/" \
    --query "Contents[?LastModified<=\`${CLEANUP_DATE}T23:59:59.000Z\`].Key" \
    --output text | \
while read -r key; do
    if [ "$key" != "None" ] && [ -n "$key" ]; then
        echo "  🗑️  삭제: s3://${S3_BUCKET}/${key}"
        aws s3 rm "s3://${S3_BUCKET}/${key}"
    fi
done

# 디스크 사용량 체크
echo "💾 디스크 사용량 체크..."
DISK_USAGE=$(df -h "$LOG_BASE_DIR" | awk 'NR==2 {print $5}' | sed 's/%//')
if [ "$DISK_USAGE" -gt 85 ]; then
    echo "⚠️  경고: 로그 디스크 사용률이 높습니다: ${DISK_USAGE}%"
    
    # 긴급 정리: 2일 이전 로그도 삭제
    EMERGENCY_DATE=$(date -d "2 days ago" +%Y-%m-%d)
    EMERGENCY_DIR="${LOG_BASE_DIR}/${EMERGENCY_DATE}"
    
    if [ -d "$EMERGENCY_DIR" ]; then
        echo "🚨 긴급 정리: ${EMERGENCY_DIR}"
        rm -rf "$EMERGENCY_DIR"
    fi
fi

# 통계 출력
echo ""
echo "📊 백업 완료 리포트:"
echo "  📅 백업 날짜: ${DATE_DELETE}"
echo "  🪣 S3 위치: s3://${S3_BUCKET}/${S3_PREFIX}/${DATE_DELETE}/"
echo "  💾 현재 디스크 사용률: ${DISK_USAGE}%"
echo "  🕐 완료 시간: $(date)"

# 로그 백업 성공 알림 (선택사항)
# curl -X POST -H 'Content-type: application/json' \
#     --data '{"text":"✅ 로그 백업 완료: '${DATE_DELETE}'"}' \
#     YOUR_SLACK_WEBHOOK_URL

echo "✅ 로그 백업 스크립트 완료" 