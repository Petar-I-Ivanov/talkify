CREATE SEQUENCE `hibernate_sequence` START WITH 1 INCREMENT BY 1;

CREATE TABLE `roles` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `name` VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE `permission` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `role_id` BIGINT NOT NULL,
    CONSTRAINT `unique_value_role` UNIQUE (`name`, `role_id`),
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
);

CREATE TABLE `users` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `username` VARCHAR(64) NOT NULL UNIQUE,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password` VARCHAR(150) NOT NULL
);

CREATE TABLE `user_role` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
);
