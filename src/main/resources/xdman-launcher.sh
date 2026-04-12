#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JRE_PATH="$SCRIPT_DIR/jre/bin/java"

if [ -x "$JRE_PATH" ]; then
    JAVA_CMD="$JRE_PATH"
else
    JAVA_CMD=$(which java 2>/dev/null)
fi

if [ -z "$JAVA_CMD" ]; then
    echo "ERROR: Java not found. Install with: sudo apt install default-jdk" >&2
    exit 1
fi

JAVA_VER=$("$JAVA_CMD" -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [ -z "$JAVA_VER" ] || [ "$JAVA_VER" -lt 11 ] 2>/dev/null; then
    echo "ERROR: Java 11+ required. Current: $JAVA_VER" >&2
    exit 1
fi

exec "$JAVA_CMD" -jar "$SCRIPT_DIR/xdman.jar" "$@"
