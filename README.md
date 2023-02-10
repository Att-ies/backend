<p align="middle" >
  <img width="200px;" src="https://user-images.githubusercontent.com/62178788/216916936-4ff2970f-6d8c-45e4-a306-1b6be76f2f70.svg"/>
</p>
<h1 align="middle">아띠즈</h1>
<p align="middle">예비작가들의 예술작품을 저렴하게’ 라는 슬로건 아래 웹사이트 개발 프로젝트를 진행하는 팀입니다.</p>

## 프로젝트 소개 📝

졸업전시회까지 치열하게 준비하지만 막상 전시회가 끝난 후 미대생의 졸업작품이 일회성으로 끝나고 방치,폐기된다는 점이 아쉬웠습니다. 졸업작품을 거래할 수 있는 플랫폼을 만들어 컬렉터는 저렴한 비용으로 구입할 수 있고 미대생들은 경제적 수입과 함께 자신의 작품을 세상에 알릴 수 있는 소중한 기회를 갖게하는 것이 아띠즈의 목표입니다.
<br>
<br />

## 팀원 👨‍👨‍👧‍👧👩‍👦‍👦

|                Backend                 |                Backend                 |              Backend               |     |
| :------------------------------------: | :------------------------------------: | :--------------------------------: | --- |
| <img src="https://avatars.githubusercontent.com/u/71515740?v=4" width=200px alt="Carrick"> |  <img src="https://avatars.githubusercontent.com/u/83302344?v=4" width=200px alt="Choo">   | <img src="https://user-images.githubusercontent.com/62178788/217484706-f58f77bd-4554-4852-9d8e-506f465ea41d.jpeg" width=200px alt="Poo"> |
|  [Carrick](https://github.com/Gyubam)  | [Choo](https://github.com/ChooSeoyeon) | [Poo](https://github.com/JunYoung) |
|                                       |
<br>
<br />

## ERD 🔨
![image](https://user-images.githubusercontent.com/83302344/217729214-bb115b0a-78ec-4950-880f-fc4d13ebb96a.png)
<br>
<br />

## 프로젝트 기술스택 💡
![image](https://user-images.githubusercontent.com/83302344/217729743-6add222b-e4c7-4936-aadd-79d5feb959c0.png)
<br>
<br />

## 프로젝트 아키텍쳐 🏛
![image](https://user-images.githubusercontent.com/83302344/217729455-38630323-d050-478e-8a85-1e1b507c932e.png)
<br>
<br />

## 백엔드 배포 과정 (GithubActions 이용해 아래 과정에 대해 CI/CD 구축함)

### 로컬 : Gradle build, Docker build
1. jar 빌드 : `gradle build`
2. 이미지 생성 : `docker build -t 계정명/atties_spring ./`
3. 도커 허브로 push : `docker push 계정명/atties_spring`

(`atties_spring`은 도커허브의 repository명)

### 서버 : Deploy
1. 도커 허브에서 pull : `docker pull 계정명/atties_spring`
2. 도커 yml에서 설정한 이미지 생성 : `docker tag 계정명/atties_spring atties_spring`
3. 도커 컴포즈 실행 : `docker-compose up`
<br>
<br />


## Git 전략
### 1) Git Workflow

### main → develop → feature/이슈번호-기능, fix/이슈번호-기능, refactor/이슈번호-기능

1. local - feature/이슈번호-기능 에서 각자 작업
2. 작업 완료 후 remote - develop 에 PR
3. 코드 리뷰 후 Approve 받고 Merge
4. remote - develop 에 Merge 될 때 마다 모든 팀원 remote - develop pull 받아 최신 상태 유지
</details>

### 2) Commit Convention

| 태그 이름  | 설명                                                                 |
| ---------- | ------------------------------------------------------------------- |
| feat     | 새로운 기능에 대한 커밋                                              |
| fix      | 버그 수정에 대한 커밋                                                |
| hotfix   | issue나 QA에서 급한 버그 수정                                        |
| build    | 빌드 관련 파일 수정에 대한 커밋                                       |
| chore    | 그 외 자잘한 수정에 대한 커밋                                         |
| style   | 코드 스타일 혹은 포맷 등에 관한 커밋                                   |
| docs     | 문서 수정에 대한 커밋                                                |
| test     | 테스트 코드 수정에 대한 커밋                                         |
| refactor | 코드 리팩토링에 대한 커밋                                            |
<br>
<br />

## 코딩 컨벤션

### 1) 네이밍 규칙

1. 변수나 함수, 클래스명은 `camelCase`를 사용한다.
2. 함수의 경우 동사+명사 사용한다.

- ex) `getInfo()`

3. DB에 저장되는 컬럼명은 `snakeCase`를 사용한다.

- ex) `member_id`

4. Url 명은 `kebabCase`를 사용하며, 명사와 소문자로 구성한다.
5. 구분자로 하이픈(-)을 사용하며, 되도록이면 구분자 없이 구성한다.

- ex) `www.example.com/user`

### 2) 빌더

1. 가독성 향상을 위해 생성자 대신 빌더를 필수적으로 사용한다.
