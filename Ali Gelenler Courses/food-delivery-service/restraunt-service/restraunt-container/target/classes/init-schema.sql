DROP SCHEMA IF EXISTS restraunt CASCADE;

CREATE SCHEMA restraunt;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS restraunt.restraunts CASCADE;

CREATE TABLE restraunt.restraunts
(
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    active boolean NOT NULL,
    CONSTRAINT restraunts_pkey PRIMARY KEY (id)
);

DROP TYPE IF EXISTS approval_status;

CREATE TYPE approval_status AS ENUM ('APPROVED', 'REJECTED');

DROP TABLE IF EXISTS restraunt.order_approval CASCADE;

CREATE TABLE restraunt.order_approval
(
    id uuid NOT NULL,
    restraunt_id uuid NOT NULL,
    order_id uuid NOT NULL,
    status approval_status NOT NULL,
    CONSTRAINT order_approval_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restraunt.products CASCADE;

CREATE TABLE restraunt.products
(
    id uuid NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    available boolean NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS restraunt.restraunt_products CASCADE;

CREATE TABLE restraunt.restraunt_products
(
    id uuid NOT NULL,
    restraunt_id uuid NOT NULL,
    product_id uuid NOT NULL,
    CONSTRAINT restraunt_products_pkey PRIMARY KEY (id)
);

ALTER TABLE restraunt.restraunt_products
    ADD CONSTRAINT "FK_RESTAURANT_ID" FOREIGN KEY (restraunt_id)
    REFERENCES restraunt.restraunts (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

ALTER TABLE restraunt.restraunt_products
    ADD CONSTRAINT "FK_PRODUCT_ID" FOREIGN KEY (product_id)
    REFERENCES restraunt.products (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE RESTRICT
    NOT VALID;

DROP MATERIALIZED VIEW IF EXISTS restraunt.order_restraunt_m_view;

CREATE MATERIALIZED VIEW restraunt.order_restraunt_m_view
TABLESPACE pg_default
AS
 SELECT r.id AS restraunt_id,
    r.name AS restraunt_name,
    r.active AS restraunt_active,
    p.id AS product_id,
    p.name AS product_name,
    p.price AS product_price,
    p.available AS product_available
   FROM restraunt.restraunts r,
    restraunt.products p,
    restraunt.restraunt_products rp
  WHERE r.id = rp.restraunt_id AND p.id = rp.product_id
WITH DATA;

refresh materialized VIEW restraunt.order_restraunt_m_view;

DROP function IF EXISTS restraunt.refresh_order_restraunt_m_view;

CREATE OR replace function restraunt.refresh_order_restraunt_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW restraunt.order_restraunt_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_restraunt_m_view ON restraunt.restraunt_products;

CREATE trigger refresh_order_restraunt_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON restraunt.restraunt_products FOR each statement
EXECUTE PROCEDURE restraunt.refresh_order_restraunt_m_view();