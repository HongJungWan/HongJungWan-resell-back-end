# Resell

### 📦 Resell API 서버

> 사기 방지와 가품 검증을 제공하는 Resell API 서버

<br/><br/>

프로젝트 진행 중, 🛠 개발 중입니다. <br/>

---

<br/><br/>

### 👬 팀원 소개

|              사진 찍는 중 ...              |
|:-------------------------------------:|
|                **홍정완**                |
| **Back-End** <br/><br/> **AWS Infra** |
|               "습관이 전부다"               |

<br/><br/>

### 🗓 프로젝트 일정

* 2022.08 ~ 현재

<br/><br/>

## 프로젝트 개요

### 💡 문제 인식

[이호, "'당근 마켓 거래 주의'...지난해 온라인 사기 2만 9000건 범인 안 잡혀", 강원도민일보, 2022. 01. 31](https://www.kado.net/news/articleView.html?idxno=1110919)<br/>
[이소라, "무신사 같은 '짝퉁 논란' 피해야…패션·중고거래 앱도 '명품 검증' 사활", 한국일보, 2022. 05. 09](https://m.hankookilbo.com/News/Read/A2022050814420005837)<br/>

<img src="https://user-images.githubusercontent.com/76596316/173557677-828247ce-6371-4f67-83f8-b82da3c425a7.png" width="450" />

<br/>

### ⚔ 개발 목표

e커머스 시장이 성장한 만큼 사기 수법도 더욱 치밀해지고 있는 점을 고려하여, <br/><br/>

1️⃣ 중고 제품 **가품 검증** <br/>
2️⃣ 유저 간 **사기 방지** <br/>
3️⃣ 클라이언트는 와이어프레임으로 대체, **API 서버 개발**에 집중

<br/><br/>

### ⚙ 기능, 22-08-25 기준

#### User Domain

| 서비스 제공 | 요구사항 |
|----|------|
| 비회원 |회원 가입|
|비회원|이메일 중복 검사|
|비회원|닉네임 중복 검사|
|비회원|이메일 인증|
|회원|로그인 / 로그아웃|
|회원|비밀번호 변경|
|회원|계좌 설정|
|회원|주소록 설정|
|회원|마이페이지|

<br/><br/>

### 📄 정보 구조도

작성 중 ...

<br/><br/>

### Resell API GUIDE

<br/><br/>

### 📄 와이어프레임

작성 중 ...

<br/><br/>

### 🛠 기술 스택

작성 중 ...

<br/><br/>

### 📎 서버 구조도

작성 중 ...

<br/><br/>

### DB ERD, 22-08-25 기준

![readme01](https://user-images.githubusercontent.com/76596316/186622159-1330e551-48d1-4954-9c8f-f7d77dc11fd2.png)

<br/><br/>

### 프로젝트 개발환경

```
Web 개발 환경

• IDE : IntelliJ IDEA Ultimate
• 언어 : Java 11
• 프레임워크 : SpringBoot 2.4.1
• 빌드도구 : gradle 6.9.1
• 데이터베이스 : MySQL 8.0.x
```

<br/><br/>

### 🧐 프로젝트 진행 시, 고민했던 점들

<br/>

📄 [스케일 업 vs 스케일 아웃](https://velog.io/@daydream/ReSeller-Project-%EC%8A%A4%EC%BC%80%EC%9D%BC-%EC%97%85-vs-%EC%8A%A4%EC%BC%80%EC%9D%BC-%EC%95%84%EC%9B%83)

📄 [Spring Mail AuthenticationFailedException](https://velog.io/@daydream/ReSeller-Project-Spring-Mail-AuthenticationFailedException-%ED%95%B4%EA%B2%B0)

📄 [Spring Rest Docs를 프로젝트에 도입해 보자](https://velog.io/@daydream/ReSeller-Project-Spring-Rest-Docs%EB%A5%BC-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-%EB%8F%84%EC%9E%85%ED%95%B4-%EB%B3%B4%EC%9E%90)

📄 [요구사항 명세서](https://velog.io/@daydream/ReSeller-Project-%EC%A3%BC%EC%9A%94-API)

<br/><br/>

### 커밋 컨벤션

---

✅ 기본적으로 커밋 메시지는 제목 / 본문 / 관련 이슈로 구분 <br/>
✅ feat : 새로운 기능 추가 <br/>
✅ fix : 버그 수정 <br/>
✅ docs : 문서 수정 <br/>
✅ style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 <br/>
✅ refactor : 코드 리펙토링 <br/>
✅ test : 테스트 코드, 리펙토링 테스트 코드 추가 <br/>
✅ chore : 기타 변경사항 <br/>
✅ 제목은 50자를 넘기지 않고, 맞침표를 붙이지 않는다. <br/>
✅ 본문은 "어떻게" 보다 "무엇을" 과 "왜"를 설명한다. <br/>
✅ 제목과 구분되기 위해 한 칸 띄워 작성한다. <br/>

<br/><br/>