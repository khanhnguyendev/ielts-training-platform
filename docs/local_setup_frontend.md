# Local Setup — Frontend (`frontend`)

## Prereqs
- Node 20 + pnpm (or npm/yarn)
- Docker Desktop (only if you run backend infra locally)

## Bootstrap
```bash
cd frontend
cp .env.example .env
pnpm install
pnpm dev
# http://localhost:3000
```

## Point to backend
- Set `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080` in `.env` for the gateway.
- For audio uploads, request a **presigned URL** from the gateway then PUT the blob to MinIO/S3.
