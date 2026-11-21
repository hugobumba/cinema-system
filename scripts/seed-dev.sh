#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

mvn -q -DskipTests test-compile \
  -Dexec.mainClass=com.hugobumba.cinemasystem.devseed.DevSeedSqlGenerator \
  -Dexec.classpathScope=test \
  org.codehaus.mojo:exec-maven-plugin:3.5.0:java

echo "Generated src/main/resources/db/changelog/changes/010-seed-dev.sql"
