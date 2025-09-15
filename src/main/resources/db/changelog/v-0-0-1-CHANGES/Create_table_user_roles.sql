CREATE TABLE user_roles
(
    user_id BIGINT       NOT NULL,
    role    VARCHAR(200) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);