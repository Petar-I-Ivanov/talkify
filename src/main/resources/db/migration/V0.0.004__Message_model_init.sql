CREATE TABLE `message` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `text` TEXT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `sent_at` TIMESTAMP NOT NULL,
    `edited_at` TIMESTAMP,
    `channel_id` BIGINT NOT NULL,
    FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`) ON DELETE CASCADE
);
