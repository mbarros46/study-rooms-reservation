CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    role VARCHAR(32) NOT NULL DEFAULT 'STUDENT'
);
ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);

CREATE TABLE rooms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    capacity INT,
    location VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);
ALTER TABLE rooms ADD CONSTRAINT uk_rooms_name UNIQUE (name);

CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES rooms(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    start_at TIMESTAMPTZ NOT NULL,
    end_at   TIMESTAMPTZ NOT NULL,
    status   VARCHAR(16) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_res_room_start_end ON reservations (room_id, start_at, end_at);
CREATE INDEX idx_res_status ON reservations (status);
