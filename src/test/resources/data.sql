INSERT INTO category (id, title, parent_category_id)
VALUES
  (1, 'food', null),
  (2, 'cloth', null),
  (3, 'electronics', null);

INSERT INTO product (id, title, price, category_id)
VALUES
	(1, 'apple', 3.99, 1),
	(2, 'almond', 15.0, 1),
	(3, 'shirt', 75.0, 2);

INSERT INTO campaign (id, campaign_price, min_product_amount, discount_type, category_id)
VALUES
  (1, 5.0, 3, 'RATE', 1),
  (2, 1.0, 3, 'AMOUNT', 1),
  (3, 40.0, 3, 'AMOUNT', 1),
  (4, 10.0, 1, 'AMOUNT', 2),
  (5, 30.0, 1, 'RATE', 2),
  (6, 20.0, 2, 'RATE', 3);