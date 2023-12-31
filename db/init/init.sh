#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" <<-EOSQL
    CREATE USER $PROCESSING_DB_USER WITH PASSWORD '$PROCESSING_DB_PASSWORD';
    CREATE DATABASE $PROCESSING_DB_USER;
    GRANT ALL PRIVILEGES ON DATABASE $PROCESSING_DB_USER TO $PROCESSING_DB_USER;
    CREATE DATABASE $PROCESSING_DB_NAME;
    GRANT ALL PRIVILEGES ON DATABASE $PROCESSING_DB_NAME TO $PROCESSING_DB_USER;
EOSQL