
create table category
(
    category_no     int auto_increment
        primary key,
    first_category  varchar(10) null,
    second_category varchar(10) null,
    third_category  varchar(10) null,
    create_date     datetime(6) not null,
    modify_date     datetime(6) not null
);

create index first_idx
    on category (first_category);

create index second_idx
    on category (second_category);

create index second_thrid_idx
    on category (category_no, second_category, third_category);

create index third_idx
    on category (third_category);

create table products
(
    product_no                 int auto_increment
        primary key,
    product_name               varchar(100) not null,
    product_category_no        int          not null,
    product_as_expiration_date date         not null,
    product_status             varchar(10)  not null,
    product_exchange_status    char         not null,
    product_purchase_date      date         not null,
    product_sales_status       varchar(10)  not null,
    product_price              int          not null,
    product_content            text         not null,
    product_stock              int          not null,
    product_view_count         int          not null,
    product_thumbnail_no       int          null,
    create_date                datetime(6)  not null,
    modify_date                datetime(6)  not null,
    seller_no                  int          not null,
    like_count                 int          null,
    delete_date                datetime(6)  null
);

create table chatroom
(
    chat_room_no int         not null
        primary key,
    product_no   int         not null,
    seller_no    int         not null,
    seller_name  varchar(20) not null,
    buyer_no     int         not null,
    buyer_name   varchar(20) not null,
    create_date  datetime(6) not null,
    modify_date  datetime(6) not null,
    constraint fk_ChatRoom_product_no
        foreign key (product_no) references products (product_no)
);

create index buyer_no_idx
    on chatroom (buyer_no);

create index seller_no_idx
    on chatroom (seller_no);

create table chats
(
    charts_no    int          not null
        primary key,
    chat_room_no int          not null,
    user_from_no int          not null,
    user_to_no   int          not null,
    chat_content varchar(500) not null,
    create_date  datetime(6)  not null,
    constraint fk_Chats_chat_room_no
        foreign key (chat_room_no) references chatroom (chat_room_no)
);

create table product_images
(
    image_no           int auto_increment
        primary key,
    product_no         int          not null,
    original_file_name varchar(80)  not null,
    file_link          varchar(255) not null,
    file_size          int          not null,
    create_date        datetime(6)  not null,
    modify_date        datetime(6)  not null,
    constraint fk_product_no
        foreign key (product_no) references products (product_no)
);

create index fk_Product_Images_product_no
    on product_images (product_no);

create index product_category_fk_idx
    on products (product_category_no);

create index product_modify_idx
    on products (modify_date);

create index product_name_and_like_idx
    on products (product_name asc, like_count desc);

create index product_name_and_modify_idx
    on products (product_name asc, modify_date desc);

create index product_name_and_price_idx
    on products (product_name, product_price);

create index product_seller_fk_idx
    on products (seller_no);


create table search
(
    search_no   int          not null
        primary key,
    search_name varchar(100) not null,
    create_date datetime(6)  not null
);

create table users_profile_images
(
    image_no           int auto_increment
        primary key,
    original_file_name varchar(80)  not null,
    file_link          varchar(200) not null,
    file_size          int          not null,
    create_date        datetime(6)  not null,
    modify_date        datetime(6)  not null
);

create table users
(
    user_no       int auto_increment
        primary key,
    user_id       varchar(20)  not null,
    user_name     varchar(20)  not null,
    user_password varchar(100) not null,
    user_phone    varchar(12)  not null,
    user_birth    date         not null,
    create_date   datetime(6)  not null,
    modify_date   datetime(6)  not null,
    image_no      int          null,
    constraint user_id_UNIQUE
        unique (user_id),
    constraint Users_Profile_Images_users
        foreign key (image_no) references users_profile_images (image_no)
);

create table likes
(
    like_no     int auto_increment
        primary key,
    user_no     int         not null,
    product_no  int         not null,
    create_date datetime(6) not null,
    constraint product_no_user_no_uni
        unique (user_no, product_no),
    constraint fk_Likes_product_no
        foreign key (product_no) references products (product_no)
            on update cascade on delete cascade,
    constraint fk_Likes_user_no
        foreign key (user_no) references users (user_no)
);

create index fk_Likes_user_no_idx
    on likes (user_no);

create index Users_Profile_Images_users_idx
    on users (image_no);

create table users_search
(
    user_search_no   int         not null
        primary key,
    user_no          int         not null,
    search_no        int         not null,
    user_search_date datetime(6) not null,
    constraint fk_Search_search_no
        foreign key (search_no) references search (search_no),
    constraint fk_Users_Search_user_no
        foreign key (user_no) references users (user_no)
);

create index fk_Search_search_no_idx
    on users_search (search_no);

create index fk_Users_Search_user_no_idx
    on users_search (user_no);

