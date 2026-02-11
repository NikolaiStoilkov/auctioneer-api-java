#!/bin/sh
# ----------------------------------------------------------------------------
# Auctioneer API - Application Startup Script
# ----------------------------------------------------------------------------

# Find JAVA_HOME if not set
if [ -z "$JAVA_HOME" ]; then
  if command -v java > /dev/null 2>&1; then
    JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
    export JAVA_HOME
  fi
fi

# Set Java command
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

# Verify Java is available
if ! command -v "$JAVACMD" > /dev/null 2>&1; then
  echo "Error: Java is not installed or JAVA_HOME is not set correctly." >&2
  exit 1
fi

# Get script directory (project root)
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Optional JVM options
JAVA_OPTS="${JAVA_OPTS:--Xms256m -Xmx512m}"

# Run the application
echo "Starting Auctioneer API..."
echo "Using Java: $JAVACMD"

# Build and run with Maven wrapper
if [ -f "$SCRIPT_DIR/mvnw" ]; then
  cd "$SCRIPT_DIR"
  ./mvnw spring-boot:run
else
  echo "Error: Maven wrapper not found. Run from project root." >&2
  exit 1
fi
