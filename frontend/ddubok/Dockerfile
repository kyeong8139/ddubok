# 단계 1: Base 이미지
FROM node:20.18-alpine AS base

# 기본 의존성 설치
RUN apk add --no-cache libc6-compat

# 단계 2: 의존성 설치
FROM base AS deps
WORKDIR /usr/src/app

# package.json 관련 파일만 복사
COPY package.json package-lock.json* .npmrc* ./

# 설치하고 캐시 정리
RUN npm ci && \
    npm cache clean --force

# 단계 3: 애플리케이션 빌드
FROM base AS builder
WORKDIR /usr/src/app

# 의존성 파일 복사
COPY --from=deps /usr/src/app/node_modules ./node_modules
COPY . .

# 빌드 실행 및 불필요한 파일 정리
RUN npm run build && \
    rm -rf ./.next/cache && \
    rm -rf ./node_modules/.cache

# 단계 4: 최종 프로덕션 이미지
FROM base AS runner
WORKDIR /usr/src/app

# 보안을 위한 비특권 사용자 설정
RUN addgroup --system --gid 1001 nodejs && \
    adduser --system --uid 1001 nextjs

# 필요한 파일만 복사
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/package.json ./
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/package-lock.json* ./
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/node_modules ./node_modules
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/public ./public
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next ./.next

# 보안 강화
USER nextjs
RUN chmod -R 500 ./.next && \
    chmod -R 500 ./node_modules

# 캐시 레이어 최적화를 위한 작업 디렉토리 설정
WORKDIR /usr/src/app

EXPOSE 3000

CMD ["npm", "run", "start"]