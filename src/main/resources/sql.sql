create table accounts
(
    account_id bigint auto_increment
        primary key,
    birthday   datetime(6)  not null,
    create_at  datetime(6)  not null,
    email      varchar(255) not null,
    fullname   varchar(255) not null,
    gender     int          not null,
    password   varchar(255) not null,
    role       int          not null,
    status     int          not null
);

create table categories
(
    category_id   bigint auto_increment
        primary key,
    create_at     datetime(6)  not null,
    name_category varchar(255) not null
);

create table images_slide
(
    image_id    bigint auto_increment
        primary key,
    description varchar(255) not null,
    link        varchar(255) not null,
    url         varchar(255) not null
);

create table types
(
    type_id   bigint auto_increment
        primary key,
    create_at datetime(6)  not null,
    type_name varchar(255) not null
);

create table posts
(
    post_id       bigint auto_increment
        primary key,
    brief_content varchar(255) not null,
    content       varchar(255) not null,
    create_at     datetime(6)  not null,
    picture       varchar(255) not null,
    rate          int          not null,
    status        int          not null,
    title         varchar(255) not null,
    account_id    bigint       not null,
    category_id   bigint       not null,
    type_id       bigint       not null,
    constraint FK2lkhwrqhy2gfs1ops6uqekoxr
        foreign key (type_id) references types (type_id),
    constraint FKf9l8slr2hdv6us03g5fkn5tcd
        foreign key (account_id) references accounts (account_id),
    constraint FKijnwr3brs8vaosl80jg9rp7uc
        foreign key (category_id) references categories (category_id)
);

create table comments
(
    account_id bigint      not null,
    post_id    bigint      not null,
    comment_at datetime(6) not null,
    content    text        not null,
    primary key (account_id, post_id),
    constraint FKagkmt4oa6cdwdop1odcp2ala4
        foreign key (account_id) references accounts (account_id),
    constraint FKh4c7lvsc298whoyd4w9ta25cr
        foreign key (post_id) references posts (post_id)
);

create table rate
(
    account_id   bigint not null,
    post_id      bigint not null,
    rating_value bigint not null,
    primary key (account_id, post_id),
    constraint FKcc6fqtvnr8ebg64d1nv3ku3i5
        foreign key (post_id) references posts (post_id),
    constraint FKhk0vrpgg64bxpo2sp9xvai9bk
        foreign key (account_id) references accounts (account_id)
);
