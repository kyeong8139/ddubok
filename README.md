<h1 align="center">뚜복 : 당신을 위한 행운 배달부</h1>
<p>
  <img alt="Version"  src="https://img.shields.io/badge/version-2.2.0-blue.svg?cacheSeconds=2592000" />
</p>

<div align="center">
  
  <img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FUyVf9%2FbtsKL1HfI5v%2FKHa06ewqiKD6O6amly0lKK%2Fimg.png" alt="ddubok" width="300">


### 뚜복 DDUBOK : 익명 응원 서비스

</div>

## 목차
1. [시작하기](#시작하기)
2. [기술 스택](#기술-스택)
3. [시스템 아키텍처](#시스템-아키텍처)
4. [ERD](#erd-entity-relationship-diagram)
5. [주요 기능](#주요-기능)
5. [개발 환경](#개발-환경)
5. [Author](#author)

## 시작하기

[뚜복 바로가기](https://ddubok.com)

## 기획 배경

최근에 사회는 빠르게 변화하고 있고 많은 사람들이 치열한 경쟁과 높은 스트레스 속에서 살아가고 있습니다. 특히 코로나 이후, 사람들은 정서적 교감과 따뜻한 응원이 절실히 필요한 시대를 살아가고 있습니다. 이런 상황에서 우리는 누군가에게 작은 **격려**와 **응원**을 전하는 것만으로도 삶에 긍정적인 영향을 미칠 수 있다는 점에 주목했습니다.

뚜복 서비스는 이러한 현대인의 정서적인 교류를 충족시키기 위해 기획되었습니다. 사용자가 **응원과 격려의 메시지를 담은 편지를 작성**하고 **행운 카드를 통해 상대방에게 희망을 전달**할 수 있게 도와 줍니다. 또한 행운 지수를 확인하며 날 마다의 소소한 재미를 더해 단순한 커뮤니케이션을 넘어 감정적 교감과 위로를 나누는 새로운 경험을 제공합니다.

저희의 서비스는 사람 간의 따뜻한 연결을 통해 더 나은 일상을 만들어 나가고자 하는 마음에서 출발했습니다. 작은 행운 카드 한장이 그리고 한 마디의 응원이 우리의 삶을 얼마나 풍요롭게 만들 수 있는지 보여주는 플랫폼으로, 더 많은 사람들이 서로에게 힘이 되어주는 세상을 꿈꾸며 기획하게 되었습니다.

## 성과



## 기술 스택

  #### Frontend
  <p>
    <img src="https://img.shields.io/badge/-Next.js-000000?style=flat-square&logo=nextdotjs&logoColor=white"/>
    <img src="https://img.shields.io/static/v1?style=flat-square&message=PWA&color=5A0FC8&logo=PWA&logoColor=FFFFFF&label="/>
    <img src="https://img.shields.io/badge/node.js-339933?style=flat-square&logo=Node.js&logoColor=white"/>
    <img src="https://img.shields.io/badge/firebase-DD2C00?style=flat-square&logo=firebase&logoColor=white"/>
    <img src="https://img.shields.io/badge/typescript-3178C6?style=flat-square&logo=typescript&logoColor=white"/>
    <img src="https://img.shields.io/badge/javascript-F7DF1E?style=flat-square&logo=javascript&logoColor=black"/>
  </p>

  #### Backend
  <p>
    <img src="https://img.shields.io/badge/-SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring_data_jpa-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
    <img src="https://img.shields.io/badge/-Maria DB-003545?style=flat-square&logo=mariadb&logoColor=white"/>
    <img src="https://img.shields.io/badge/-Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
  </p>
  
  #### Infra
  <p>
    <img src="https://img.shields.io/badge/-Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>
    <img src="https://img.shields.io/badge/-Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/>
    <img src="https://img.shields.io/badge/-AWS EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=white"/>
    <img src="https://img.shields.io/badge/-Nginx-269539?style=flat-square&logo=Nginx&logoColor=white"/>
    <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat-square&logo=Ubuntu&logoColor=white"/>
    <img src="https://img.shields.io/badge/AWS_S3-569A31?logo=amazons3&logoColor=fff&style=flat-square"/>
  </p>

  #### Tools
  <p>
    <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira&logoColor=white"/>
    <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/>
    <img src="https://img.shields.io/badge/git-%23F05033.svg?style=flat-square&logo=git&logoColor=white"/>
    <img src="https://img.shields.io/badge/-Notion-000000?style=flat-square&logo=notion&logoColor=white"/>
  </p>


## 시스템 아키텍처

![아키텍처_구조](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbXuMk6%2FbtsKNxx3kxI%2FtyN2jQZkv68ol50f2E8pp1%2Fimg.png)


## ERD (Entity Relationship Diagram)

![erd](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdlsVJ9%2FbtsKNrdTIll%2FPxUAVQmje5x4KVhg8zdAB1%2Fimg.png)


## 주요 기능

- 행운 카드 생성
- 행운 카드 조르기 및 공유
- 관리자 페이지를 통한 개발과 운영 분리

## 개발 환경

- Backend
  - Java 17
  - Spring Boot 3.2.7
  - Redis 7.4.0
  - MySQL 9.0.1
- Frontend
  - React 18.3.3
  - Node.js 20.18.0
  - Typescript 5.6.3
  - Next Js 14.0.1
  - Next-PWA 5.6.0
- Server
  - AWS S3
  - AWS EC2
- Infra
  - Docker 27.1.1
  - Ubuntu 20.04.6 LTS
  - Jenkins 2.452.3

## Author

🐩 **김윤**

* Github: [@Eunicekk](https://github.com/Eunicekk)

🍞 **김경민**

* Github: [@gyungmean](https://github.com/gyungmean)

🍕 **박상용**

* Github: [@sangypar](https://github.com/sangypar)

🐼 **박성혁**

* Github: [@goldenkiwi-hyeuk](https://github.com/goldenkiwi-hyeuk)

💡 **이보경**

* Github: [@kyeong8139](https://github.com/kyeong8139)

🤦‍♂️ **최다환**

* Github: [@hwan-da](https://github.com/hwan-da)

## 📝 라이선스
Copyright © 2024 뚜복초. [Eunicekk](https://github.com/Eunicekk). [gyungmean](https://github.com/gyungmean). [sangypar](https://github.com/sangypar). [goldenkiwi-hyeuk](https://github.com/goldenkiwi-hyeuk). [kyeong8139](https://github.com/kyeong8139). [hwan-da](https://github.com/hwan-da) <br />
