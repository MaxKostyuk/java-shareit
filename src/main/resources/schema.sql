-- DELETE FROM users;
-- DELETE FROM items;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR NOT NULL UNIQUE,
    email VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id INTEGER NOT NULL
);

