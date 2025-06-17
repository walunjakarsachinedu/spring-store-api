CREATE TABLE carts (
	id BINARY(16) DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY NOT NULL,
    created_at DATE DEFAULT (CURDATE()) NOT NULL
);


CREATE TABLE cart_items (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BINARY(16) NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,
    CONSTRAINT cart_item_cart_id_fk
		FOREIGN KEY(cart_id) REFERENCES carts(id)
        ON DELETE CASCADE,
	CONSTRAINT cart_item_product_id_fk
		foreign key(product_id) REFERENCES products(id)
        ON DELETE CASCADE,
	CONSTRAINT cart_item_cart_product_unique
		UNIQUE(cart_id, product_id)
)