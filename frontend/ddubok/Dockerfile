# reference: https://github.com/vercel/next.js/blob/canary/examples/with-docker/Dockerfile

# 단계 1: Base 이미지
FROM node:20.18-alpine AS base

# 단계 2: 의존성 설치
FROM base AS deps
RUN apk add --no-cache libc6-compat

WORKDIR /usr/src/app

COPY package.json package-lock.json* .npmrc* ./
RUN npm ci;
RUN rm -rf ./.next/cache

# 단계 3: 애플리케이션 빌드
FROM base AS builder
WORKDIR /usr/src/app
COPY --from=deps /usr/src/app/node_modules ./node_modules
COPY . .

RUN npm run build;

# 단계 4: 최종 프로덕션 이미지
FROM base AS runner
WORKDIR /usr/src/app

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

RUN mkdir .next
RUN chown nextjs:nodejs .next

COPY --from=builder /usr/src/app/public ./public
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

CMD ["npm", "run", "start"]