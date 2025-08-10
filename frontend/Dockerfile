# syntax=docker/dockerfile:1.7

ARG NODE_VERSION=20.14.0

FROM node:${NODE_VERSION}-alpine AS base
WORKDIR /app
# General env useful across stages
ENV NEXT_TELEMETRY_DISABLED=1
RUN apk add --no-cache libc6-compat

# Install dependencies (with cache)
FROM base AS deps
ENV NODE_ENV=production
COPY package.json package-lock.json* ./
RUN --mount=type=cache,target=/root/.npm \
    if [ -f package-lock.json ]; then npm ci --include=dev; else npm install --include=dev; fi

# Build the app
FROM base AS builder
ENV NODE_ENV=production
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN --mount=type=cache,target=/root/.npm npm run build
# Prune dev dependencies for runtime
RUN npm prune --omit=dev

# Runtime image
FROM node:${NODE_VERSION}-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production \
    PORT=3000 \
    HOSTNAME=0.0.0.0 \
    NEXT_TELEMETRY_DISABLED=1
RUN apk add --no-cache libc6-compat && addgroup -g 1001 -S nodejs && adduser -S nextjs -u 1001

# Copy necessary build artifacts and runtime deps
COPY --chown=nextjs:nodejs --from=builder /app/public ./public
COPY --chown=nextjs:nodejs --from=builder /app/.next ./.next
COPY --chown=nextjs:nodejs --from=builder /app/node_modules ./node_modules
COPY --chown=nextjs:nodejs package.json next.config.* ./

USER nextjs
EXPOSE 3000
CMD ["npm", "run", "start"]


