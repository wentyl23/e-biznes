# --- !Ups

INSERT INTO brand (name, foundation_year, description)
VALUES ('Chivas Regal', 1801, 'robia glownie szkockie'),
       ('Jim Beam', 1795, 'whiskey typu bourbon');

INSERT INTO category(name, description)
VALUES ('szkoczka', 'ze szkocji'),
       ('bourbon', 'whiskey amerykanska, z kukurydzy i innych zboz');

INSERT INTO product (category_id, brand_id, name, amount, unit_price, description)
VALUES (1, 1, 'Chivas Regal 12YO 0.7L', '21', '119.99','produkt wysokiej klasy'),
       (1, 1, 'Chivas Regal 18YO PE 40% 0,7 l', '12','369.00','Jest to wyjątkowa whisky mieszana.'),
       (2, 2, 'Jim Beam White Label / 40% / 0,7l', '20', '68.00', 'najpopularniejszy bourbon na świecie');

# --- !Downs