# Reseller

### 📦 Reseller API 서버

> 사기 방지와 가품 검증을 제공하는 Reseller 거래 플랫폼 - **API 서버**

<br/>

* 프로젝트 진행 중, 🛠 개발 중입니다. 

<br/><br/>

---

<br/><br/>

### 👬 팀원 소개 

| <img src="https://user-images.githubusercontent.com/76596316/189167606-11593e72-d111-4d48-9ec8-61da93585030.jpg" width="200" > |
|:------------------------------------------------------------------------------------------------------------------------------:|
|                                                              홍정완                                                               |
|                                             **Back-End** <br/><br/> **AWS Infra**                                              |

<br/><br/>

### 🗓 프로젝트 일정

* 2022.08 ~ 현재

<br/><br/>

## 프로젝트 개요

### 💡 문제 인식

[이호, "'당근 마켓 거래 주의'...지난해 온라인 사기 2만 9000건 범인 안 잡혀", 강원도민일보, 2022. 01. 31](https://www.kado.net/news/articleView.html?idxno=1110919)<br/>
[이소라, "무신사 같은 '짝퉁 논란' 피해야…패션·중고거래 앱도 '명품 검증' 사활", 한국일보, 2022. 05. 09](https://m.hankookilbo.com/News/Read/A2022050814420005837)<br/>

<img src="https://user-images.githubusercontent.com/76596316/173557677-828247ce-6371-4f67-83f8-b82da3c425a7.png" width="450" />

<br/><br/>

### ⚔ 프로젝트 목표 

<br/>

#### 1️⃣ 대용량 트래픽을 고려한 확장 용이한 서버 구조 및 설계 

#### 2️⃣ 국내 KREAM과 같은 중개거래 플랫폼 구현

#### 3️⃣ Rest API 서버로, 클라이언트는 프로토타입으로 제작하여 서버 공부에 좀 더 집중

<br/><br/>

### ⚙ 기능

📄 [요구사항 명세서](https://velog.io/@daydream/ReSeller-Project-%EC%A3%BC%EC%9A%94-API)

<br/><br/>

### 📄 정보 구조도

11월 예정

<br/><br/>

### 📄 Resell API GUIDE

11월 예정

<br/><br/>

### 📄 와이어프레임

11월 예정

<br/><br/>

### 🛠 기술 스택

<img src="https://user-images.githubusercontent.com/76596316/189166512-2b5f95ad-583e-4421-bced-4ee431bb2d8a.png" width="500">


<br/><br/>

### 📎 서버 구조도

12월 예정

<br/><br/>

### DB ERD

![ReSeller-ERD](https://user-images.githubusercontent.com/76596316/198881324-4974d10a-fccc-44c6-9417-2366fe3a3958.png)

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

📄 [Redis, 세션 저장소와 캐시 저장소를 분리해 보자 ❗](https://velog.io/@daydream/ReSeller-Project-Redis-%EC%84%B8%EC%85%98-%EC%A0%80%EC%9E%A5%EC%86%8C%EC%99%80-%EC%BA%90%EC%8B%9C-%EC%A0%80%EC%9E%A5%EC%86%8C-%EB%B6%84%EB%A6%AC)

📄 [Cache 적용으로 응답 속도를 개선해 보자 ❗](https://velog.io/@daydream/ReSeller-Project-Cache-%EC%A0%81%EC%9A%A9%EC%9C%BC%EB%A1%9C-%EC%9D%91%EB%8B%B5-%EC%86%8D%EB%8F%84%EB%A5%BC-%EA%B0%9C%EC%84%A0%ED%95%B4%EB%B3%B4%EC%9E%90)

📄 [특정 리소스에 액세스하지 못하도록 제한](https://velog.io/@daydream/ReSeller-Project-%ED%8A%B9%EC%A0%95-%EB%A6%AC%EC%86%8C%EC%8A%A4%EC%97%90-%EC%95%A1%EC%84%B8%EC%8A%A4-%ED%95%98%EC%A7%80-%EB%AA%BB%ED%95%98%EB%8F%84%EB%A1%9D-%EC%A0%9C%ED%95%9C)

📄 [AWS S3에 이미지 파일을 업로드해 보자 ❗](https://velog.io/@daydream/ReSeller-Project-AWS-S3%EC%97%90-%EC%9D%B4%EB%AF%B8%EC%A7%80-%ED%8C%8C%EC%9D%BC%EC%9D%84-%EC%97%85%EB%A1%9C%EB%93%9C%ED%95%B4-%EB%B3%B4%EC%9E%90)

📄 [반복되는 부가기능을 공통적으로 처리해 보자](https://velog.io/@daydream/ReSeller-Project-%EB%B0%98%EB%B3%B5%EB%90%98%EB%8A%94-%EB%B6%80%EA%B0%80%EA%B8%B0%EB%8A%A5%EC%9D%84-%EA%B3%B5%ED%86%B5%EC%A0%81%EC%9C%BC%EB%A1%9C-%EC%B2%98%EB%A6%AC%ED%95%B4-%EB%B3%B4%EC%9E%90)

📄 [Stateless한 HTTP가 로그인하는 방법](https://velog.io/@daydream/ReSeller-Project-Stateless%ED%95%9C-HTTP%EA%B0%80-%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)

📄 [세션 스토리지로 어떤 것이 더 적합한가 ❓](https://velog.io/@daydream/ReSeller-Project-%EC%84%B8%EC%85%98-%EC%8A%A4%ED%86%A0%EB%A6%AC%EC%A7%80%EB%A1%9C-%EC%96%B4%EB%96%A4-%EA%B2%83%EC%9D%B4-%EB%8D%94-%EC%A0%81%ED%95%A9%ED%95%9C%EA%B0%80-Redis-VS-Memcached)

📄 [Scale out 확장 구조에서 Session 불일치 문제를 어떻게 다뤄야 할까 🤔](https://velog.io/@daydream/ReSeller-Project-%EB%8B%A4%EC%A4%91-%EC%84%9C%EB%B2%84-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C-Session-%EB%B6%88%EC%9D%BC%EC%B9%98-%EB%AC%B8%EC%A0%9C%EB%A5%BC-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%8B%A4%EB%A4%84%EC%95%BC-%ED%95%A0%EA%B9%8C)

📄 [스케일 업 vs 스케일 아웃](https://velog.io/@daydream/ReSeller-Project-%EC%8A%A4%EC%BC%80%EC%9D%BC-%EC%97%85-vs-%EC%8A%A4%EC%BC%80%EC%9D%BC-%EC%95%84%EC%9B%83)

📄 [Spring Mail AuthenticationFailedException](https://velog.io/@daydream/ReSeller-Project-Spring-Mail-AuthenticationFailedException-%ED%95%B4%EA%B2%B0)

📄 [Spring Rest Docs를 프로젝트에 도입해 보자](https://velog.io/@daydream/ReSeller-Project-Spring-Rest-Docs%EB%A5%BC-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-%EB%8F%84%EC%9E%85%ED%95%B4-%EB%B3%B4%EC%9E%90)

📄 [Redis 설치 및 기본 명령어](https://velog.io/@daydream/Redis-%EC%84%A4%EC%B9%98-%EB%B0%8F-%EA%B8%B0%EB%B3%B8-%EB%AA%85%EB%A0%B9%EC%96%B4)

📄 [Mysql Workbench 8.0.28, Reverse Engineering 툴로 ERD 추출 시, 1:1 관계 👉 1:N 관계로 인식](https://velog.io/@daydream/ReSeller-Project-Mysql-Workbench-8.0.28-Reverse-Engineering-%ED%88%B4%EB%A1%9C-ERD-%EC%B6%94%EC%B6%9C-%EC%8B%9C-11-%EA%B4%80%EA%B3%84-1N-%EA%B4%80%EA%B3%84%EB%A1%9C-%EC%9D%B8%EC%8B%9D)

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