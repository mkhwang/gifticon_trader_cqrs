alter table reviews
    rename column user_id to reviewer;

alter table reviews
    add user_id bigint;

alter table reviews
    add constraint fk_user_review_user_id
        foreign key (user_id) references users;

UPDATE reviews r
SET user_id = g.seller_id
    FROM gifticons g
WHERE r.gifticon_id = g.id;