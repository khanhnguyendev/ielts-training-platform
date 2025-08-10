# Scripts Usage

## build_push_frontend.sh
Build and push the frontend Docker image to Docker Hub.

### Basic usage
```bash
./scripts/build_push_frontend.sh <dockerhub_user>
```
- Default tag when omitted: `dev-<git-sha>`
- Default image name: `ielts-platform-frontend`

### Override tag
```bash
./scripts/build_push_frontend.sh <dockerhub_user> dev-001
```

### Env variable usage
```bash
DOCKERHUB_USER=<dockerhub_user> TAG=dev-002 IMAGE_NAME=ielts-platform-frontend ./scripts/build_push_frontend.sh
```

### Optional: buildx multi-arch
Enable buildx instead of plain `docker build`/`push`:
```bash
USE_BUILDX=1 PLATFORMS=linux/amd64,linux/arm64 ./scripts/build_push_frontend.sh <dockerhub_user> dev-001
```

### Notes
- The script assumes you are already logged in via `docker login`.
- If you prefer token-based login in CI, set `DOCKERHUB_USER` and `DOCKERHUB_TOKEN` env vars (the script will log in non-interactively when tokens are provided).