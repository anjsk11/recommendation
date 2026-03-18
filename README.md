
# 📍 위치 데이터 기반 장소 추천 플랫폼 — 백엔드

> 졸업 캡스톤 프로젝트 (팀: 강진호, 김강년, 김준태, 박이준, 백재원, 최재원)

사용자의 **실시간 GPS 동선**과 **AI 협업 필터링**을 결합하여 개인화된 장소를 추천하는 Spring Boot 백엔드입니다.

---

## 📌 프로젝트 개요

스마트폰 GPS를 통해 수집된 사용자의 이동 경로를 **시간대별 히트맵**으로 분석하고, Yelp 데이터셋 기반의 **협업 필터링 AI 모델(Flask 서버)** 의 점수와 합산하여 최적의 장소 20곳을 추천합니다.

기존 서비스(Google Maps, 배달의 민족 등)가 정적인 리뷰·평점에 의존하는 한계를 극복하여, **사용자의 실제 생활 동선과 행동 패턴**을 추천에 반영하는 것이 핵심 차별점입니다.

---

## 🏗 시스템 아키텍처

<img width="3675" height="1725" alt="KakaoTalk_20260318_220420497" src="https://github.com/user-attachments/assets/79c77c02-06e0-43c0-b3e4-9bfed7d75be1" />

| 구성 요소 | 역할 |
|-----------|------|
| Android 앱 | GPS 수집, 사용자 인터페이스 |
| **Spring Boot (이 레포)** | 인증, 비즈니스 로직, DB 연동 |
| Flask AI 서버 (EC2) | 협업 필터링 추천 점수 계산 |
| PostgreSQL (AWS RDS) | 사용자/장소/리뷰/GPS 데이터 저장 |
| AWS Cognito | JWT 기반 사용자 인증 |

---

## 프로젝트 DB ERD
<img width="1811" height="622" alt="ERD" src="https://github.com/user-attachments/assets/032cee98-0239-45bd-8e23-96a3e8e78903" />

---

## ✨ 핵심 추천 알고리즘

<img width="486" height="1055" alt="algorithm_flow" src="https://github.com/user-attachments/assets/f2532471-15cb-4333-a064-20cdd1835d4a" />


추천은 두 가지 점수를 합산하는 **하이브리드 방식**으로 동작합니다.

### 1. 히트맵 기반 위치 점수
- 전체 지도를 **100m × 100m 격자(30×30 배열)** 로 분리
- 사용자 GPS를 5분 단위로 수집해 해당 격자 값을 누적
- 시간대별(아침 / 낮 / 밤)로 독립된 히트맵을 유지하여 생활 패턴 반영
- 각 장소의 좌표를 히트맵에 매핑 → 정규화 후 가중치(`HEATMAP_WEIGHT = 5`) 적용

### 2. 협업 필터링 점수 (Flask AI 서버)
- 사용자가 리뷰를 남긴 장소 ID 목록을 Flask 서버로 전송
- Yelp 오픈 데이터셋 기반 Matrix Factorization(ALS/SGD) 모델이 유사 사용자의 선호를 분석해 점수 반환

### 3. 점수 합산 및 Top-20 반환
```
최종 점수 = 히트맵 점수 + 협업 필터링 점수
→ 내림차순 정렬 → 상위 20개 장소 반환
```

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Database | PostgreSQL (AWS RDS) |
| ORM | Spring Data JPA / Hibernate |
| 인증 | AWS Cognito (JWT / OAuth2 Resource Server) |
| 클라우드 | AWS EC2, RDS, Cognito |

---

## 📡 API 엔드포인트

모든 API는 AWS Cognito JWT 토큰 인증이 필요합니다 (`Authorization: Bearer <token>`).

### 추천
| Method | Path | 설명 |
|--------|------|------|
| GET | `/me/recommend` | 히트맵 + AI 기반 장소 Top-20 추천 |

### 사용자
| Method | Path | 설명 |
|--------|------|------|
| POST | `/users/sync` | Cognito ID Token으로 사용자 정보 동기화 |
| GET | `/users/me` | 내 정보 조회 |
| DELETE | `/users/me` | 계정 삭제 |
| POST | `/users/me/heatmap` | GPS 좌표 수신 → 히트맵 업데이트 |
| GET | `/users/me/heatmap` | 시간대 통합 히트맵 조회 |

### 장소 / 리뷰
| Method | Path | 설명 |
|--------|------|------|
| GET | `/places/{placeId}` | 장소 상세 조회 |
| GET | `/places/{placeId}/reviews` | 장소별 리뷰 목록 |
| POST | `/reviews` | 리뷰 작성 |
| PATCH | `/reviews/{reviewId}` | 리뷰 수정 |

---

## 📂 프로젝트 구조

```
src/main/java/com/sensingbros/recommendation/
├── config/         # Security (Cognito JWT), WebClient 설정
├── filter/         # CognitoJwtAuthenticationFilter
├── domain/         # JPA Entity (Users, Place, Review, Gps)
├── repository/     # Spring Data JPA Repository
├── service/        # 비즈니스 로직 (추천, 히트맵, 유저, 리뷰)
├── rest/           # REST 컨트롤러
├── model/          # DTO
├── mapper/         # ModelMapper
├── converter/      # Integer[][] ↔ JSONB 변환 (IntArray2DConverter)
└── util/           # HeatmapUtils
```

---

## 📊 성능 평가

- **협업 필터링**: Yelp 데이터셋 기반, Leave-One-Out 방식으로 분할, RMSE / MAE / Hit Rate@N / NDCG 지표로 평가

---

## 🔮 향후 확장 계획

- 실시간 교통·날씨 외부 데이터 통합으로 추천 정밀도 향상
- 시계열 예측 모델을 이용한 사전 추천 기능
- 콘텐츠 기반 필터링(KoBERT 리뷰 임베딩)과 협업 필터링을 결합한 하이브리드 모델

