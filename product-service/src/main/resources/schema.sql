CREATE TABLE products (
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(100),
    description VARCHAR(200) NOT NULL,
    image       VARCHAR(100)
);