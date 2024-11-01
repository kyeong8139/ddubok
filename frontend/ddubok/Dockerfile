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
    npm cache clean --force && \
    rm -rf node_modules && \
    rm -f package-lock.json && \


# 단계 3: 애플리케이션 빌드
FROM base AS builder
WORKDIR /usr/src/app

# 의존성 파일 복사
COPY --from=deps /usr/src/app/node_modules ./node_modules
COPY . .

# Next.js standalone 모드로 빌드
RUN npm run build

# 단계 4: 최종 프로덕션 이미지
FROM base AS runner
WORKDIR /usr/src/app

# 보안을 위한 비특권 사용자 설정
RUN addgroup --system --gid 1001 nodejs && \
    adduser --system --uid 1001 nextjs

# standalone 출력과 필요한 public 파일들만 복사
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/public ./public
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next/static ./.next/static

# 환경 변수 설정
ENV NODE_ENV production
ENV PORT 3000

# 보안 강화
USER nextjs

EXPOSE 3000

CMD ["node", "server.js"]