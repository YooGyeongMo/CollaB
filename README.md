# CollaB

## 👥 팀원 역할

### 프론트엔드
- **유경모**
- **김대환**

### 백엔드
- **정세곤**: 로그인, 알람, 프로필, 친구 관리
- **양경빈**: 채팅, 피어리뷰, 투두, 매칭, 프로젝트
- **서세환**: 서버 관리
- **이승윤**: 매칭, 프로젝트

---

## 📄 ERD (Entity-Relationship Diagram)

프로젝트의 주요 데이터베이스 테이블:
- **Member**: 사용자 정보 저장
- **Authority**: 사용자 권한 관리
- **Friend**: 친구 관계 관리
- **Profile**: 사용자 프로필 정보
- **Alarm**: 알림 데이터 저장
- **Technology**: 기술 항목 관리
- **TechnologyLevel**: 기술 수준 관리

---

## 🛠 기술 스택

### 백엔드
| 구성 요소       | 사용 기술                                |
|----------------|-----------------------------------------|
| 프로그래밍 언어| Java 11                                 |
| 프레임워크      | Spring Boot 2.7.17                      |
| 보안           | Spring Security, JWT                    |
| 데이터베이스    | PostgreSQL                              |
| API 문서화      | Swagger (Springfox 3.0.0)               |
| 실시간 통신     | Spring WebSocket                        |
| 기타           | ModelMapper, Lombok                     |

### 프론트엔드
| 구성 요소       | 사용 기술 및 라이브러리                 |
|----------------|-----------------------------------------|
| 언어 및 도구   | Kotlin, Android Studio, MVVM           |
| 인증 및 로그인  | Firebase Auth, Kakao SDK, Google Auth   |
| 네트워킹       | Retrofit2, OkHttp3, SSE, StompProtocol |
| 비동기 및 반응형| RxJava, Coroutine                      |
| 이미지 처리     | Glide                                  |
| UI             | Lottie, ConstraintLayout               |

---

## 📄 API 설명

### 1. 사용자 인증
- **`POST /login`**  
  - **설명**: 사용자 로그인.  
  - **요청**:
    ```json
    {
      "account": "testuser",
      "password": "password123"
    }
    ```
  - **응답**:
    ```json
    {
      "token": "jwt_token",
      "roles": ["ROLE_USER"]
    }
    ```

- **`POST /register`**  
  - **설명**: 사용자 회원가입.  
  - **요청**:
    ```json
    {
      "account": "newuser",
      "password": "password123",
      "email": "newuser@example.com"
    }
    ```
  - **응답**:
    ```json
    {
      "message": "User registered successfully."
    }
    ```

---

### 2. 알람 관리
- **`GET /alarms`**  
  - **설명**: 현재 사용자의 알림 목록 조회.  
  - **응답**:
    ```json
    [
      {
        "id": 1,
        "message": "You have a new friend request.",
        "timestamp": "2024-11-13T10:30:00",
        "isRead": false
      }
    ]
    ```

- **`POST /alarms`**  
  - **설명**: 새로운 알림 생성.  
  - **요청**:
    ```json
    {
      "receiverUserId": 101,
      "senderUserId": 102,
      "message": "You have a new message."
    }
    ```
  - **응답**:
    ```json
    {
      "id": 1,
      "message": "You have a new message.",
      "timestamp": "2024-11-13T10:30:00",
      "isRead": false
    }
    ```

---

### 3. 프로필 관리
- **`GET /profile`**  
  - **설명**: 현재 사용자의 프로필 정보 조회.  
  - **응답**:
    ```json
    {
      "id": 1,
      "nickname": "testuser",
      "instruction": "Software Engineer",
      "role": "Developer"
    }
    ```

- **`PUT /profile`**  
  - **설명**: 사용자 프로필 업데이트.  
  - **요청**:
    ```json
    {
      "nickname": "Updated User",
      "instruction": "Experienced Developer",
      "role": "Tech Lead"
    }
    ```
  - **응답**:
    ```json
    {
      "message": "Profile updated successfully."
    }
    ```

---

### 4. 친구 관리
- **`GET /friend/list`**  
  - **설명**: 친구 목록 조회.  
  - **응답**:
    ```json
    [
      {
        "id": 1,
        "memberId": 101,
        "friendId": 102,
        "status": "accepted"
      }
    ]
    ```

- **`POST /friend/add`**  
  - **설명**: 친구 추가 요청.  
  - **요청**:
    ```json
    {
      "memberId": 101,
      "friendId": 102
    }
    ```
  - **응답**:
    ```json
    {
      "id": 1,
      "status": "pending"
    }
    ```

---

### 5. 기술 및 레벨 관리
- **`POST /technology`**  
  - **설명**: 새로운 기술 추가.  
  - **요청**:
    ```json
    {
      "name": "Java"
    }
    ```
  - **응답**:
    ```json
    {
      "id": 1,
      "name": "Java"
    }
    ```

- **`POST /technology-level`**  
  - **설명**: 특정 기술 레벨 추가.  
  - **요청**:
    ```json
    {
      "technologyId": 1,
      "level": 3
    }
    ```
  - **응답**:
    ```json
    {
      "id": 1,
      "technologyId": 1,
      "level": 3
    }
    ```

---
