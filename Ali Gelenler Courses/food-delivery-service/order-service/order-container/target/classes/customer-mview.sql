DROP SCHEMA IF EXISTS "customer" CASCADE;

CREATE SCHEMA "customer";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS "customer".customer_order_m_view CASCADE;

CREATE TABLE "customer".customer_order_m_view
(
    id uuid NOT NULL,
    user_name varchar(50) NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

-- Inserting the first entry
INSERT INTO "customer".customer_order_m_view (id, user_name)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb41', 'John Doe');