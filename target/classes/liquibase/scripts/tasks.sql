-- liquibase formatted sql

-- changeset mgubina:1
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    time TIMESTAMP NOT NULL
);