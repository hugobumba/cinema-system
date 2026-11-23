#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$ROOT_DIR/.env"

if [[ -f "$ENV_FILE" ]]; then
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
fi

if [[ -z "${JWT_SECRET:-}" ]]; then
  echo "Missing required env var: JWT_SECRET"
  exit 1
fi

if [[ -z "${JWT_EXPIRATION_MS:-}" ]]; then
  echo "Missing required env var: JWT_EXPIRATION_MS"
  exit 1
fi

cd "$ROOT_DIR"
mvn spring-boot:run -Dspring-boot.run.profiles=test
