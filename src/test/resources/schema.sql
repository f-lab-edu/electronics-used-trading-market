DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `category_no`     int         NOT NULL,
    `first_category`  varchar(10) NOT NULL,
    `second_category` varchar(10) NOT NULL,
    `thirth_category` varchar(10) NOT NULL,
    `create_date`     datetime(6) NOT NULL,
    `modify_date`     datetime(6) NOT NULL,
    PRIMARY KEY (`category_no`)
);

DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`
(
    `search_no`   int          NOT NULL,
    `search_name` varchar(100) NOT NULL,
    `create_date` datetime(6)  NOT NULL,
    PRIMARY KEY (`search_no`)
);

DROP TABLE IF EXISTS `users_profile_images`;
CREATE TABLE `users_profile_images`
(
    `image_no`         int         NOT NULL AUTO_INCREMENT,
    `original_file_name` varchar(80) NOT NULL,
    `file_link`        varchar(80) NOT NULL,
    `file_size`        int         NOT NULL,
    `create_date`      datetime(6) NOT NULL,
    `modify_date`      datetime(6) NOT NULL,
    PRIMARY KEY (`image_no`)
);


DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `product_no`                 int          NOT NULL,
    `product_name`               varchar(100) NOT NULL,
    `product_category_no`        int          NOT NULL,
    `product_as_expiration_date` date         NOT NULL,
    `product_status`             varchar(10)  NOT NULL,
    `product_exchange_status`    char(1)      NOT NULL,
    `product_purchase_date`      date         NOT NULL,
    `product_sales_status`       varchar(10)  NOT NULL,
    `product_price`              int          NOT NULL,
    `product_content`            text         NOT NULL,
    `product_stock`              int          NOT NULL,
    `product_view_count`         int          NOT NULL,
    `product_thumbnail_no`       int          NOT NULL,
    `create_date`                datetime(6)  NOT NULL,
    `modify_date`                datetime(6)  NOT NULL,
    `seller_no`                  int          not null,
    `like_count`                 int          null,
    PRIMARY KEY (`product_no`)
);
DROP TABLE IF EXISTS `product_images`;
CREATE TABLE `product_images`
(
    `image_no`         int         NOT NULL,
    `product_no`       int         NOT NULL,
    `original_file_name` varchar(80) NOT NULL,
    `file_link`        varchar(80) NOT NULL,
    `file_size`        int         NOT NULL,
    `create_date`      datetime(6) NOT NULL,
    PRIMARY KEY (`image_no`)
);



DROP TABLE IF EXISTS `chats`;
CREATE TABLE `chats`
(
    `charts_no`    int          NOT NULL,
    `chat_room_no` int          NOT NULL,
    `user_from_no` int          NOT NULL,
    `user_to_no`   int          NOT NULL,
    `chat_content` varchar(500) NOT NULL,
    `create_date`  datetime(6)  NOT NULL,
    PRIMARY KEY (`charts_no`)
);

DROP TABLE IF EXISTS `chatroom`;
CREATE TABLE `chatroom`
(
    `chat_room_no` int         NOT NULL,
    `product_no`   int         NOT NULL,
    `seller_no`    int         NOT NULL,
    `seller_name`  varchar(20) NOT NULL,
    `buyer_no`     int         NOT NULL,
    `buyer_name`   varchar(20) NOT NULL,
    `create_date`  datetime(6) NOT NULL,
    `modify_date`  datetime(6) NOT NULL,
    PRIMARY KEY (`chat_room_no`)
);


DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes`
(
    `like_no`     int         NOT NULL AUTO_INCREMENT,
    `user_no`     int         NOT NULL,
    `product_no`  int         NOT NULL,
    `create_date` datetime(6) NOT NULL,
    PRIMARY KEY (`like_no`)
);


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `user_no`       int          NOT NULL AUTO_INCREMENT,
    `user_id`       varchar(20)  NOT NULL,
    `user_name`     varchar(20)  NOT NULL,
    `user_password` varchar(100) NOT NULL,
    `user_phone`    varchar(12)  NOT NULL,
    `user_birth`    date         NOT NULL,
    `create_date`   datetime(6)  NOT NULL,
    `modify_date`   datetime(6)  NOT NULL,
    `image_no`      int DEFAULT NULL,
    PRIMARY KEY (`user_no`),
    CONSTRAINT `user_id_UNIQUE` UNIQUE (`user_id`)
);


DROP TABLE IF EXISTS `users_search`;
CREATE TABLE `users_search`
(
    `user_search_no`   int         NOT NULL,
    `user_no`          int         NOT NULL,
    `search_no`        int         NOT NULL,
    `user_search_date` datetime(6) NOT NULL,
    PRIMARY KEY (`user_search_no`)
);


-- users_search --
ALTER TABLE `users_search` ADD
    CONSTRAINT `fk_Search_search_no` FOREIGN KEY (`search_no`) REFERENCES `search` (`search_no`);
ALTER TABLE `users_search` ADD
    CONSTRAINT `fk_Users_Search_user_no` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`);

-- users --
ALTER TABLE `users` ADD
    CONSTRAINT `Users_Profile_Images_users` FOREIGN KEY (`image_no`) REFERENCES `users_profile_images` (`image_no`);

-- likes --
ALTER TABLE `likes` ADD
    CONSTRAINT `fk_Likes_product_no` FOREIGN KEY (`product_no`) REFERENCES `products` (`product_no`);
ALTER TABLE `likes` ADD
    CONSTRAINT `fk_Likes_user_no` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`);

-- chatroom --
ALTER TABLE `chatroom` ADD
    CONSTRAINT `fk_ChatRoom_product_no` FOREIGN KEY (`product_no`) REFERENCES `products` (`product_no`);

-- chats --
ALTER TABLE `chats` ADD
    CONSTRAINT `fk_Chats_chat_room_no` FOREIGN KEY (`chat_room_no`) REFERENCES `chatroom` (`chat_room_no`);

-- product_images --
ALTER TABLE `product_images` ADD
    CONSTRAINT `fk_Product_Images_product_no` FOREIGN KEY (`product_no`) REFERENCES `products` (`product_no`);

-- products --
ALTER Table `products` ADD
    CONSTRAINT `fk_Products_product_product_category_no` FOREIGN KEY (`product_category_no`) REFERENCES `category` (`category_no`);
ALTER Table `products` ADD
    CONSTRAINT `fk_Products_product_thumbnail_no` FOREIGN KEY (`product_thumbnail_no`) REFERENCES `product_images` (`image_no`);