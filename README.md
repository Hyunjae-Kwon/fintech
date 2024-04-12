<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=150&section=header" />

## 📝 프로젝트 주제

- 스프링 부트를 활용한 서버 API 기반 간단한 핀테크 프로젝트

## 🛢 프로젝트 구조

![DIAGRAM](doc/img/diagram.png)

### 🔗 ERD

![ERD](doc/img/erd.png)

### ⚙ Tech Stack

- Language : `Java 17`
- Build : `Gradle`
- Database
  - RDB : `Mysql`
- Test : `Junit5`
- Login Token : `JWT`
- Test UI : `Swagger`
- Server : `Tomcat v8.5`
- JDK : `JDK 17`
- Library : `Lombok`, `MySQL-Connector`

---

## 👦 회원가입과 로그인

### 회원가입

- [x] 회원 가입 (아이디, 비밀번호, 이름, 이메일, 생년월일)
- [x] 유효성 검사 (기가입 아이디, 이름 & 생년월일)

### 로그인

- [x] 로그인 토큰 발행
- [x] 로그인 토큰을 통한 제어 확인 (JWT, Filter를 사용해서 간략하게)
- [x] 로그인 유효성 검사 (미가입 아이디, 비밀번호 비일치)

---

## 💵 계좌 관리 기능

### 계좌 관리

- [x] 계좌 생성
- [x] 계좌 삭제
  - [x] 유효성 검사 (계좌 잔고)

### 계좌 금액 관리

- [x] 계좌 금액 입금
- [x] 계좌 금액 출금
  - [ ] 유효성 검사 (계좌 잔고)
- [ ] 계좌 입출금 내역 조회 (전체, 입금만, 출금만)


- 추가 구현 계획
  - [ ] 하루 최대 입출금 금액 제한
  - [ ] 하루 최대 입출금 회수 제한

## 🔍 계좌 검색 기능

- [ ] 본인 계좌 검색 (소유 계좌 리스트 (계좌번호, 잔액, 생성일자))
- [ ] 계좌 상세 검색
  - [ ] 검색일 1개월 입출금 내역 조회 (최신순)
  - [ ] 날짜 지정 (최대 1개월 검색 가능)
  - [ ] 송금 내용 (보내는 사람, 받는 사람) 으로 검색
- [ ] 검색 내용에 따른 페이징 처리 (최신순, 1페이지당 10개)

## 💸 계좌 송금 기능

- [ ] 계좌 송금
  - [ ] 유효성 검사 (계좌 잔고)
- [ ] 송금 내역 조회


- 추가 구현 계획
  - [ ] 대량 송금 시 이메일 인증 후 송금 (mailgun)

---

## 📅 주차별 개발 계획

- 1주차 - 프로젝트 주제 선정 및 기능 구성
- 2주차 - 로그인, 회원가입 기능
- 3주차 - 계좌 관리, 검색 기능
- 4주차 - 계좌 금액 관리 기능
- 5주차 - 계좌 송금 기능

## ⚠ Trouble Shooting

[문제 해결 과정은 이 경로에서 확인할 수 있습니다.](doc/TROUBLE_SHOOTING.md)

---

<img src="https://capsule-render.vercel.app/api?type=waving&color=auto&height=150&section=footer" />
