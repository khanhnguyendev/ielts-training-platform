#!/usr/bin/env bash
set -euo pipefail

# Build and push the frontend Docker image to Docker Hub
#
# Usage (simple docker build + push):
#   ./scripts/build_push_frontend.sh <dockerhub_user> <tag>
#   DOCKERHUB_USER=<user> [IMAGE_NAME=ielts-platform-frontend] [TAG=<tag>] ./scripts/build_push_frontend.sh
#
# Optional: enable buildx multi-arch by setting USE_BUILDX=1 and PLATFORMS
#   USE_BUILDX=1 PLATFORMS=linux/amd64,linux/arm64 ./scripts/build_push_frontend.sh <dockerhub_user> <tag>
#
# Env vars:
# - DOCKERHUB_USER: Docker Hub username (required if not provided as arg 1)
# - DOCKERHUB_TOKEN: Optional token for non-interactive docker login
# - IMAGE_NAME: Repo name (default: ielts-platform-frontend)
# - TAG: Image tag (default: dev-YYYYmmdd-HHMM-<gitsha>)
# - USE_BUILDX: if set to 1, use buildx build --push (default: 0)
# - PLATFORMS: buildx platforms, e.g., linux/amd64,linux/arm64 (only when USE_BUILDX=1)

ROOT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")"/.. && pwd)
FRONTEND_DIR="${ROOT_DIR}/frontend"

if [[ ! -d "${FRONTEND_DIR}" ]]; then
  echo "[ERROR] Frontend directory not found at ${FRONTEND_DIR}" >&2
  exit 1
fi

DOCKERHUB_USER=${DOCKERHUB_USER:-${1:-}}
TAG=${TAG:-${2:-}}
if [[ -z "${DOCKERHUB_USER}" ]]; then
  echo "[ERROR] First arg (dockerhub_user) is required or set DOCKERHUB_USER env." >&2
  exit 1
fi

IMAGE_NAME=${IMAGE_NAME:-ielts-platform-frontend}

# Compute default tag: dev-<gitsha>
SHORT_SHA=$(git -C "${ROOT_DIR}" rev-parse --short HEAD 2>/dev/null || echo "nogit")
DEFAULT_TAG="dev-${SHORT_SHA}"
TAG=${TAG:-${DEFAULT_TAG}}

REPO="${DOCKERHUB_USER}/${IMAGE_NAME}"

echo "[INFO] Building ${REPO}:${TAG} from ${FRONTEND_DIR}"

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker is not installed or not in PATH" >&2
  exit 1
fi

if [[ "${USE_BUILDX:-0}" == "1" ]]; then
  if ! docker buildx version >/dev/null 2>&1; then
    echo "[ERROR] docker buildx is not available. Install/enable Buildx or unset USE_BUILDX." >&2
    exit 1
  fi
  # Ensure a usable builder exists
  if ! docker buildx ls | grep -q "\*"; then
    echo "[INFO] No active buildx builder. Creating one named 'frontend-builder'..."
    docker buildx create --name frontend-builder --use >/dev/null
  fi
  PLATFORMS=${PLATFORMS:-linux/amd64}
  echo "[INFO] buildx platforms: ${PLATFORMS}"
  echo "[INFO] Starting buildx build and push..."
  docker buildx build \
    --platform "${PLATFORMS}" \
    -t "${REPO}:${TAG}" \
    "${FRONTEND_DIR}" \
    --push
else
  echo "[INFO] Building image with plain 'docker build'..."
  docker build -t "${REPO}:${TAG}" "${FRONTEND_DIR}"
  echo "[INFO] Pushing image with plain 'docker push'..."
  docker push "${REPO}:${TAG}"
fi

echo "[SUCCESS] Image available: ${REPO}:${TAG}"
echo "[HINT] Update your K8s deployment to use this tag if needed: ${REPO}:${TAG}"


