# --- !Ups

CREATE TABLE "app_user"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "providerId"  VARCHAR NOT NULL,
    "providerKey" VARCHAR NOT NULL,
    "email"       VARCHAR NOT NULL
);

CREATE TABLE "region"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "user_id" INTEGER NOT NULL,
    "address" VARCHAR NOT NULL,
    "city" VARCHAR NOT NULL,
    "zip" VARCHAR NOT NULL,
    "state" VARCHAR NOT NULL,
    "country" VARCHAR NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE "category"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL,
    "description" VARCHAR NOT NULL
);

CREATE TABLE "brand"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL,
    "foundation_year" INTEGER NOT NULL,
    "description" VARCHAR NOT NULL
);

CREATE TABLE "voucher"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "code" VARCHAR NOT NULL,
    "value" VARCHAR NOT NULL
);

CREATE TABLE "payment"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "value" INTEGER NOT NULL,
    "name_on_card" VARCHAR NOT NULL,
    "card_number" VARCHAR NOT NULL,
    "cv_code" VARCHAR NOT NULL,
    "exp_date" VARCHAR NOT NULL
);

CREATE TABLE "shop_order"
(
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "user_id" INTEGER NOT NULL,
  "payment_id" INTEGER NOT NULL,
  "voucher_id" INTEGER,
  FOREIGN KEY (user_id) REFERENCES app_user(id),
  FOREIGN KEY (payment_id) REFERENCES payment(id),
  FOREIGN KEY (voucher_id) REFERENCES voucher(id)
);

CREATE TABLE "product"
(
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "category_id" INT NOT NULL,
    "brand_id" INT NOT NULL,
    "name" VARCHAR NOT NULL,
    "amount" VARCHAR NOT NULL,
    "unit_price" VARCHAR NOT NULL,
    "description" VARCHAR NOT NULL,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE "review"
(
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "user_id" INTEGER NOT NULL,
  "product_id" INTEGER NOT NULL,
  "rating" VARCHAR NOT NULL,
  "description" VARCHAR NOT NULL,

  FOREIGN KEY (user_id) REFERENCES app_user (id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE "cart"
(
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "order_id" INTEGER NOT NULL,
  "product_id" INTEGER NOT NULL,
  "amount" INTEGER NOT NULL,
  FOREIGN KEY (order_id) REFERENCES shop_order(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

# --- !Downs
DROP TABLE "app_user"
DROP TABLE "region"
DROP TABLE "category"
DROP TABLE "brand"
DROP TABLE "voucher"
DROP TABLE "payment"
DROP TABLE "shop_order"
DROP TABLE "product"
DROP TABLE "review"
DROP TABLE "cart"