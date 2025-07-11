CREATE TABLE orders(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	customer_id BIGINT NOT NULL,
	status VARCHAR(20) NOT NULL,
	created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	total_price DECIMAL(10,2) NOT NULL,
	CONSTRAINT fk_orders_users
	 FOREIGN KEY (customer_id) REFERENCES users(id)
);


CREATE TABLE order_items(
	id bigint PRIMARY KEY AUTO_INCREMENT,
	product_id BIGINT NOT NULL,
	order_id BIGINT NOT NULL,
	unit_price DECIMAL(10,2) NOT NULL,
	quantity INT NOT NULL,
	total_price DECIMAL(10,2) NOT NULL,
	CONSTRAINT fk_order_items_products
		FOREIGN KEY (product_id) REFERENCES products(id),
	constraint fk_order_items_orders
		FOREIGN KEY (order_id) REFERENCES orders(id)
);