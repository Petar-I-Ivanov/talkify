CREATE TABLE `friendship` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `user_id` BIGINT NOT NULL,
    `friend_id` BIGINT NOT NULL,
    `channel_id` BIGINT NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`),
    FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`)
);
