# 🚀 운영 수준 로깅 시스템 설정 가이드

## 📋 개요

이 로깅 시스템은 **실제 운영 환경에서 이슈 추적과 분석**을 위해 설계되었습니다.

### 🎯 목표
- 사용자가 어떤 행위를 했는지 추적
- 문제 발생 시 원인 분석 가능
- 성능 이슈 자동 감지
- 3일치 로그 보관 + S3 백업

## 📁 로그 분류

```
/logs/YYYY-MM-DD/
├── m-common/
│   ├── application.log     # INFO/WARN 일반 로그
│   ├── error.log          # ERROR 이상 에러 로그
│   ├── access.log         # HTTP 요청/응답 로그
│   ├── business.log       # 비즈니스 플로우 추적
│   └── performance.log    # 성능 메트릭
├── m-customer/            # 동일 구조
├── m-manager/             # 동일 구조
└── m-admin/               # 동일 구조
```

## 🏗️ 핵심 컴포넌트

### 📂 새로운 폴더 구조 (정리 완료!)
```
m-common/src/main/java/com/antmen/antwork/common/util/
├── 📁 log/                           # 로깅 전용 패키지 (새로 정리!)
│   ├── 🔧 BusinessLogger.java        # 비즈니스 플로우 로깅
│   ├── 🔧 LoggingInterceptor.java    # HTTP 요청/응답 로깅  
│   ├── 🔧 PerformanceMonitoringAspect.java # 성능 모니터링 AOP
│   └── 📖 LoggingExampleService.java # 사용법 예제
├── 🔧 WebMvcConfig.java              # 인터셉터 등록 (import 경로 수정됨)
├── 🔧 UserSecurityConfig.java        # 보안 설정
├── 🔧 JwtTokenProvider.java          # JWT 처리
└── ... 기타 유틸리티들
```

### 1. BusinessLogger (비즈니스 로거)
```java
// 📍 위치: m-common/.../util/log/BusinessLogger.java
// 📦 패키지: com.antmen.antwork.common.util.log

// 사용 예시
@Autowired
private BusinessLogger businessLogger;

businessLogger.logReservationCreate(userId, reservationId, categoryId, amount, memo);
```

### 2. LoggingInterceptor (HTTP 요청/응답 로거)
```java
// 📍 위치: m-common/.../util/log/LoggingInterceptor.java
// 📦 패키지: com.antmen.antwork.common.util.log
// ⚙️ 자동 등록: WebMvcConfig에서 자동으로 모든 API 요청에 적용
```

### 3. PerformanceMonitoringAspect (성능 모니터링)
```java
// 📍 위치: m-common/.../util/log/PerformanceMonitoringAspect.java
// 📦 패키지: com.antmen.antwork.common.util.log
// ⚙️ 자동 적용: @Service, @Controller, @Repository 메서드에 자동 AOP 적용
```

### 4. LoggingExampleService (사용법 예제)
```java
// 📍 위치: m-common/.../util/log/LoggingExampleService.java
// 📦 패키지: com.antmen.antwork.common.util.log
// 📖 실제 사용법 예제와 테스트 메서드들 포함
```

## 🔧 설치 단계

### 1. 의존성 추가 (이미 완료)
```gradle
// build.gradle에 이미 포함됨
implementation 'org.springframework.boot:spring-boot-starter-aop'
```

### 2. 서버 디렉토리 생성
```bash
# EC2에서 로깅 시스템 초기 설정 스크립트 실행
cd KBE5_AntMen_BE
chmod +x scripts/setup-logging.sh
./scripts/setup-logging.sh

# 또는 수동으로 생성
sudo mkdir -p /logs
sudo chmod 755 /logs
sudo mkdir -p /tmp/thread-dumps
sudo chmod 755 /tmp/thread-dumps
```

### 3. S3 백업 설정
```bash
# AWS CLI 설치 (Amazon Linux 2)
sudo yum install -y aws-cli

# AWS 자격 증명 설정
aws configure
# Access Key ID: [S3 접근 가능한 키]
# Secret Access Key: [비밀 키]
# Default region: ap-northeast-2

# S3 버킷 생성 (없다면)
aws s3 mb s3://your-antmen-logs-bucket
```

### 4. 백업 스크립트 설정
```bash
# 스크립트 수정
vim KBE5_AntMen_BE/scripts/log-backup-s3.sh
# S3_BUCKET="your-antmen-logs-bucket" 으로 변경

# 크론 작업 등록 (매일 새벽 2시)
sudo crontab -e
# 다음 줄 추가:
0 2 * * * /path/to/KBE5_AntMen_BE/scripts/log-backup-s3.sh >> /var/log/log-backup.log 2>&1
```

### 5. Docker Compose 재배포
```bash
# 볼륨 마운트가 추가된 Docker Compose 재시작
docker-compose down
docker-compose up -d
```

## 📝 사용 방법

### 기본 로깅 (자동)
```java
// 일반 로그 - 자동으로 application.log에 기록
log.info("사용자 예약 요청: userId={}", userId);

// 에러 로그 - 자동으로 error.log에 기록  
log.error("결제 처리 실패: {}", e.getMessage(), e);
```

### 비즈니스 로깅 (수동 추가)
```java
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final BusinessLogger businessLogger;
    
    public void createReservation(ReservationRequestDto dto) {
        try {
            // 기존 비즈니스 로직
            Reservation reservation = saveReservation(dto);
            
            // 🎯 비즈니스 로깅 추가
            businessLogger.logReservationCreate(
                dto.getCustomerId(), 
                reservation.getId(), 
                dto.getCategoryId(), 
                dto.getAmount(), 
                dto.getMemo()
            );
            
        } catch (Exception e) {
            // 🎯 에러 로깅 추가
            businessLogger.logBusinessError(
                BusinessLogger.ACTION_RESERVATION_CREATE,
                dto.getCustomerId(),
                null,
                "예약 생성 실패: " + e.getMessage(),
                e
            );
            throw e;
        }
    }
}
```

### HTTP 요청 로깅 (자동)
```
// access.log 자동 기록 예시
2024-01-15 14:23:12.123 [req-12ab] [user:12345] POST /api/v1/reservations 200 1234ms user:12345 192.168.1.100
```

### 성능 모니터링 (자동)
```java
// 📊 단계별 성능 모니터링
// 3초 이상: 경고 로그
// 10초 이상: 비즈니스 로그 추가 기록  
// 15초 이상: Thread Dump 생성
// 30초 이상: 긴급 Thread Dump + 심각한 문제로 분류
```

## 🔍 로그 분석 예시

### 예약 실패 원인 추적
```bash
# 1. 특정 사용자의 예약 실패 찾기
grep "userId:12345" /logs/2024-01-15/*/business.log | grep "RESERVATION_CREATE"

# 2. 매칭 실패 원인 분석
grep "MATCHING_FAILED" /logs/2024-01-15/*/business.log

# 3. 결제 실패 통계
grep "PAYMENT_FAILED" /logs/2024-01-15/*/business.log | wc -l
```

### 성능 이슈 분석
```bash
# 1. 느린 API 요청 찾기
grep "30000ms" /logs/2024-01-15/*/access.log

# 2. 느린 비즈니스 로직 찾기
grep "느린" /logs/2024-01-15/*/performance.log

# 3. Thread Dump 확인
ls -la /tmp/thread-dumps/
```

### 실시간 모니터링
```bash
# 실시간 에러 모니터링
tail -f /logs/$(date +%Y-%m-%d)/*/error.log

# 실시간 비즈니스 로그 모니터링
tail -f /logs/$(date +%Y-%m-%d)/*/business.log
```

## 🚨 알림 설정 (선택사항)

### 심각한 에러 발생 시 Slack 알림
```bash
# 크론으로 5분마다 에러 체크
*/5 * * * * /path/to/error-alert.sh

# error-alert.sh 예시
ERROR_COUNT=$(grep -c "ERROR" /logs/$(date +%Y-%m-%d)/*/error.log)
if [ $ERROR_COUNT -gt 10 ]; then
    curl -X POST -H 'Content-type: application/json' \
        --data '{"text":"🚨 ERROR 급증: '${ERROR_COUNT}'건"}' \
        YOUR_SLACK_WEBHOOK_URL
fi
```

## 📊 예상 로그 분석 시나리오

### 시나리오 1: "예약이 안 된다"는 고객 문의
```bash
# 1단계: 해당 사용자의 예약 시도 로그 확인
grep "userId:12345" /logs/2024-01-15/*/business.log

# 결과 예시:
# 2024-01-15 14:23:12 [RESERVATION_CREATE] 사용자 예약 생성: categoryId=1 amount=40000
# 2024-01-15 14:23:13 [MATCHING_START] 매칭 시작: availableManagers=0 searchRadius=5km
# 2024-01-15 14:23:13 [MATCHING_FAILED] 매칭 실패: reason='검색 반경 내 매니저 없음'

# 🎯 원인 파악: 5km 내에 매니저가 없어서 매칭 실패
# 🎯 해결: 검색 반경 확대 또는 해당 지역 매니저 모집 필요
```

### 시나리오 2: "결제가 자꾸 실패한다"는 문의
```bash
# 1단계: 결제 실패 로그 확인
grep "PAYMENT_FAILED" /logs/2024-01-15/*/business.log | grep "userId:12345"

# 결과 예시:
# 2024-01-15 15:30:45 [PAYMENT_FAILED] 결제 실패: payMethod=CARD amount=40000 errorCode=CARD_DECLINED errorMsg='카드 한도 초과'

# 🎯 원인 파악: 카드 한도 초과로 결제 실패
# 🎯 해결: 고객에게 다른 결제 수단 안내
```

### 시나리오 3: "서버가 느려진다"는 모니터링 알람
```bash
# 1단계: 느린 요청 확인
grep "30000ms" /logs/$(date +%Y-%m-%d)/*/access.log

# 2단계: 느린 메서드 확인
grep "느린" /logs/$(date +%Y-%m-%d)/*/performance.log

# 3단계: Thread Dump 분석
ls -la /tmp/thread-dumps/ | tail -5

# 🎯 원인 파악: 특정 매칭 알고리즘이 30초 이상 소요
# 🎯 해결: 매칭 알고리즘 최적화 또는 타임아웃 설정 조정
```

## ✅ 확인 사항

### 로깅 시스템 정상 동작 확인
```bash
# 1. 로그 디렉토리 생성 확인
ls -la /logs/$(date +%Y-%m-%d)/

# 2. 로그 파일 생성 확인 (API 요청 후)
ls -la /logs/$(date +%Y-%m-%d)/*/

# 3. 백업 스크립트 테스트
bash /path/to/log-backup-s3.sh

# 4. S3 백업 확인
aws s3 ls s3://your-antmen-logs-bucket/antmen-logs/
```

### 주의사항
- 📁 로그 디렉토리 용량 모니터링 (85% 이상 시 자동 정리)
- 🔐 S3 접근 권한 확인
- ⏰ 크론 작업 정상 실행 확인
- 🚀 성능 임계치 조정 (필요시)

### 📈 성능 임계치 설정 가이드
```java
// PerformanceMonitoringAspect.java에서 조정 가능
private static final long SLOW_METHOD_THRESHOLD = 3000;        // 3초: 경고
private static final long VERY_SLOW_METHOD_THRESHOLD = 15000;  // 15초: Thread Dump
private static final long CRITICAL_SLOW_THRESHOLD = 30000;     // 30초: 긴급 상황
```

**권장 설정:**
- **개발 환경**: 3초 → 1초, 15초 → 5초 (더 엄격하게)
- **운영 환경**: 현재 설정 유지 (3초, 15초, 30초)
- **고부하 시스템**: 5초, 20초, 60초 (더 관대하게)

## 🎉 완료!

이제 **실제 운영 환경에서 이슈 추적이 가능한 로깅 시스템**이 구축되었습니다!

- ✅ 사용자 행동 추적
- ✅ 에러 원인 분석  
- ✅ 성능 이슈 감지
- ✅ 자동 백업 및 정리
- ✅ 실시간 모니터링 