CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id UUID PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_by UUID REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_registrations (
    event_registration_id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
    event_id UUID REFERENCES events(event_id) ON DELETE CASCADE,
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_registration UNIQUE (user_id, event_id)
);
