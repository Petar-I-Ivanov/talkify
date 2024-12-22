CREATE TABLE `channel` (
    `id` BIGINT PRIMARY KEY,
    `active` BOOLEAN NOT NULL,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `owner_id` BIGINT NOT NULL,
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);

CREATE TABLE `channel_admin` (
    `admin_id` BIGINT NOT NULL,
    `channel_id` BIGINT NOT NULL,
    PRIMARY KEY (`admin_id`, `channel_id`),
    FOREIGN KEY (`admin_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`) ON DELETE CASCADE
);

CREATE TABLE `channel_guest` (
    `guest_id` BIGINT NOT NULL,
    `channel_id` BIGINT NOT NULL,
    PRIMARY KEY (`guest_id`, `channel_id`),
    FOREIGN KEY (`guest_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`channel_id`) REFERENCES `channel` (`id`) ON DELETE CASCADE
);
