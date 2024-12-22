SET @user_r = nextval('hibernate_sequence');
SET @owner_r = nextval('hibernate_sequence');
SET @admin_r = nextval('hibernate_sequence');

INSERT INTO `roles` VALUES
    (@user_r, true, 'USER'),
    (@owner_r, true, 'CHANNEL_OWNER'),
    (@admin_r, true, 'CHANNEL_ADMIN'),
    (nextval('hibernate_sequence'), true, 'CHANNEL_GUEST');

INSERT INTO `permission`
VALUES
    (nextval('hibernate_sequence'), true, 'user:create', @user_r),
    (nextval('hibernate_sequence'), true, 'user:select', @user_r),
    (nextval('hibernate_sequence'), true, 'user:search', @user_r),
    (nextval('hibernate_sequence'), true, 'user:update', @user_r),
    (nextval('hibernate_sequence'), true, 'user:delete', @user_r),
    (nextval('hibernate_sequence'), true, 'channel:create', @user_r),
    (nextval('hibernate_sequence'), true, 'channel:select', @user_r),
    (nextval('hibernate_sequence'), true, 'channel:search', @user_r),
    (nextval('hibernate_sequence'), true, 'channel:update', @admin_r),
    (nextval('hibernate_sequence'), true, 'channel:update', @owner_r),
    (nextval('hibernate_sequence'), true, 'channel:delete', @owner_r);

-- password is Pe123456
SET @users = nextval('hibernate_sequence');
INSERT INTO `users` VALUES (@users, true, 'pecata', 'pecata@gmail.com', '$2a$10$IWQ90iF8gtrGBZ1kk/qYUe0ji5fRAMfBmwU4P/vTmk0UjGZQiz9M.');

INSERT INTO `user_role` VALUES (@users, @user_r);
