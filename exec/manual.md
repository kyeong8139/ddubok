# 포팅 매뉴얼

## 사용 도구
- 이슈 관리: Jira
- 형상 관리: GitLab
- 커뮤니케이션: Notion, MatterMost
- 디자인: Figma
- 빌드 도구: Jenkins

## 개발 도구
- Visual Studio Code: ver 1.90.2
- IntelliJ IDEA Ultimate: 2024.1.4
- DataGrip: 2024.1.4

## 외부 서비스
- 카카오 로그인 (Kakao Oauth2.0)
- 구글 로그인
- X 로그인
- 네이버 로그인
- OpenAI

## 개발 환경

**Frontend**
- Node.js: 20.18.0
- React: 18.3.3
- Typescript: 5.6.3
- NextJs: 14.0.1
- Next-PWA: 5.6.0

**Backend**
- Java: openjdk 17.0.12 (2024-07-16)
- Spring Boot: 3.2.7
- Redis: 7.4.0
- MySQL: Ver 9.0.1 for Linux on x86_64

**Server**
- AWS S3
- AWS EC2 (test)
- AWS EC2 (main)

**Infra**
- Docker: 27.1.1
- Ubuntu: 20.04.6 LTS
- Jenkins: 2.452.3

## 환경 변수

**백엔드 .env**
```
# S3
S3_BUCKET_NAME=
S3_ACCESS_KEY
S3_SECRET_KEY=

# KAKAO
KAKAO_ID=
KAKAO_ADMIN=

# NAVER
NAVER_ID=
NAVER_SECRET=

# X
X_ID=
X_SECRET=

# GOOGLE
GOOGLE_ID=
GOOGLE_SECRET=

# JWT
JWT_SECRET=
ACCESS_EXPIRATION=
REFRESH_EXPIRATION=

JWT_REDIRECT_URL=

# GPT
OPEN_AI_KEY=
OPEN_AI_URL=
OPEN_AI_FINETUNING=

# MARIADB
MARIADB_ROOT_PASSWORD=
MARIADB_DATABASE=
MARIADB_USER=
MARIADB_PASSWORD=

DOMAIN_URL=
```

**프론트엔드 .env**
```
- NEXT_PUBLIC_BASE_URL=
- NEXT_PUBLIC_BASE_URL_V2=
- NEXT_PUBLIC_LOGIN_URL=
- NEXT_PUBLIC_FETCH_URL=
- NEXT_PUBLIC_SALT_KEY=
- NEXT_PUBLIC_SHARE_URL=
- NEXT_PUBLIC_KAKAO_KEY=
- NEXT_PUBLIC_SPLIT_KEY=
- NEXT_PUBLIC_FIREBASE_API_KEY=
- NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=
- NEXT_PUBLIC_FIREBASE_PROJECT_ID=
- NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=
- NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=
- NEXT_PUBLIC_FIREBASE_APP_ID=
- NEXT_PUBLIC_FIREBASE_VAPID_KEY=
```

## DB 덤프 파일
```
INSERT INTO member (role, social_provider, social_id, nickname, state, created_at, updated_at, deleted_at, notification_consent) 
VALUES 
    ('ROLE_USER', 'KAKAO', 'user12345', '사용자', 'ACTIVATED', NOW(), NOW(), NULL, 'DISABLED');

INSERT INTO member (role, social_provider, social_id, nickname, state, created_at, updated_at, deleted_at, notification_consent) 
VALUES 
    ('ROLE_ADMIN', 'GOOGLE', 'admin12345', '관리자', 'ACTIVATED', NOW(), NOW(), NULL, 'DISABLED');
```

# CI/CD 및 INFRA

## 1. UFW 설정

```
### UFW 상태 확인
sudo ufw status
# 결과 예시: Status: inactive

### 사용할 포트 허용 및 등록된 포트 조회
# 포트 허용
sudo ufw allow [포트번호]

# 등록된 포트 확인
sudo ufw show added

### UFW 활성화
sudo ufw enable
# SSH 연결 경고 메시지 확인 후 y 입력

### UFW 상태 확인 및 삭제
# 상태 확인 (번호 확인 가능)
sudo ufw status numbered

# 규칙 삭제 (번호 입력)
sudo ufw delete [번호]

### UFW 비활성화
sudo ufw disable
```

## 2. Docker 및 Docker Compose 설치

### Docker 및 Docker Compose 설치

```
sudo apt-get update && \
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common && \
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - && \
sudo apt-key fingerprint 0EBFCD88 && \
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" && \
sudo apt-get update && \
sudo apt-get install -y docker-ce && \
sudo usermod -aG docker ubuntu && \
newgrp docker && \
sudo curl -L "https://github.com/docker/compose/releases/download/2.27.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
sudo chmod +x /usr/local/bin/docker-compose && \
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

### 설치 확인

```
docker -v
# Docker version 27.3.1, build ce12230

docker compose version
# Docker Compose version v2.24.5
```


## 3. Jenkins 설치

### 마운트할 볼륨 디렉토리 생성
```
cd /home/ubuntu
mkdir -p data/jenkins
```

### Docker 권한 변경
```
sudo chmod 666 /var/run/docker.sock
```

### `compose.service.yml` 작성
```
mkdir config
cd config
vi compose.service.yml
```
```
services:
  jenkins:
    container_name: jenkins
    ports:
      - "8080:8080"
    image: jenkins/jenkins:jdk17
    volumes:
      - /home/ubuntu/data/jenkins:/var/jenkins_home
      - /usr/bin/docker:/usr/bin/docker
      - /usr/local/lib/docker/cli-plugins:/usr/local/lib/docker/cli-plugins
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - TZ=Asia/Seoul
    networks:
      - ddubok-network

networks:
  ddubok-network:
```

### Jenkins 실행 및 초기 비밀번호 확인
```
docker compose -f compose.service.yml up -d --build
docker logs jenkins


# 로그에서 비밀번호 확인:
# *************************************************************
# Jenkins initial setup is required. An admin user has been created and a password generated.
# Please use the following password to proceed to installation:
# [초기 비밀번호]
# *************************************************************
```

### Jenkins 초기 설정
ID: my-id  
PW: my-password  

### 플러그인 설치
- GitLab
- Publish Over SSH
- SSH Agent

---

## 4. Nginx 설치 및 설정

### `compose.service.yml`에 Nginx 추가
```
nginx:
  container_name: nginx
  image: nginx
  ports:
    - 80:80
    - 443:443
  environment:
    - TZ=Asia/Seoul
  volumes:
    - ./nginx/nginx.conf:/etc/nginx/nginx.conf
    - ./nginx/conf.d:/etc/nginx/conf.d
    - ../logs/nginx:/var/log/nginx
    - ./certbot:/etc/letsencrypt
    - ../data/certbot:/var/www/certbot
  networks:
    - ddubok-network
```

### `nginx.conf` 파일
```
user nginx;
worker_processes auto;

error_log /var/log/nginx/error.log warn;
pid /var/run/nginx.pid;

events {
  worker_connections 1024;
}

http {
  include /etc/nginx/mime.types;
  default_type application/octet-stream;

  log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

  access_log  /var/log/nginx/access.log  main;

  sendfile        on;
  tcp_nopush      on;
  tcp_nodelay     on;
  keepalive_timeout  65;
  types_hash_max_size 2048;

  include /etc/nginx/conf.d/*.conf;
}
```

### HTTPS SSL 인증서 발급
```
nslookup [도메인]  # DNS 등록 확인

docker run -it --rm \
  -v /home/ubuntu/config/certbot:/etc/letsencrypt \
  -v /home/ubuntu/data/certbot:/var/www/certbot \
  certbot/certbot certonly --webroot \
  --webroot-path=/var/www/certbot \
  -d [도메인 이름]
```

### HTTPS 적용을 위한 `default.conf` 변경
```
server {
  listen 80;
  server_name [도메인];

  location /.well-known/acme-challenge/ {
    root /var/www/certbot;
  }

  location / {
    return 301 https://$host$request_uri;
  }
}

server {
  listen 443 ssl;
  server_name [도메인];

  ssl_certificate /etc/letsencrypt/live/[도메인]/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/[도메인]/privkey.pem;

  location /admin/jenkins {
    proxy_pass http://jenkins:8080;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header Host $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }
}
```

### 젠킨스 설정 변경 (compose.service.yml)

```
        environment:
          - JENKINS_OPTS="--prefix=/admin/jenkins"
```


# 5. Backend 빌드
    
### compose.backend.yml
```
services:
  backend-server:
    container_name: backend-server
    image: bokyeong8139/ddubok:backend
    expose:
      - "8080"
    depends_on:
      mariadb:
        condition: service_healthy
      redis:
        condition: service_healthy
    env_file:
      - .env.backend
    environment:
      - TZ=Asia/Seoul

  mariadb:
    container_name: mariadb
    image: mariadb
    env_file:
      - .env.backend
    environment:
      - TZ=Asia/Seoul
    healthcheck:
      test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
      start_period: 5s
      interval: 10s
      timeout: 5s
      retries: 3

  redis:
    container_name: redis
    image: redis
    environment:
      - TZ=Asia/Seoul
    volumes:
      - ../data/redis:/data
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      start_period: 5s
      interval: 1s
      timeout: 3s
      retries: 5
```

### nginx 설정 추가

```
# default.conf

    location /api {
        proxy_pass http://backend-server:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
    
```

```
### compose.service.yml

    nginx:
        networks:
          - ddubok-network
          - default

```


# 6. Frontend 빌드

### compose.frontend.yml

```
services:
    frontend-server:
            container_name: frontend-server
            image: bokyeong8139/ddubok:frontend
```


### nginx 설정 추가

``` 
# default.conf

    location / {
        proxy_pass http://frontend-server:3000;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # Next.js 특정 설정
        proxy_buffering off;
        proxy_redirect off;

        # WebSocket 연결을 위한 타임아웃 설정
        proxy_read_timeout 300;
        proxy_connect_timeout 300;
    }

```
