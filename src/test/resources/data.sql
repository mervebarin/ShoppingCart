INSERT INTO category (id, title, parent_category_id)
VALUES
  (1, 'food', null);

INSERT INTO product (id, title, price, category_id)
VALUES
	(1, 'apple', 10.0, 1),
	(2, 'almond', 10.0, 1);

INSERT INTO campaign (id, campaign_price, min_product_amount, discount_type, category_id)
VALUES
  (1, 5.0, 3, 'RATE', 1);