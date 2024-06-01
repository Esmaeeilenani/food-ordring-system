PGDMP         '                |            postgres    15.1    15.1 �    [           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            \           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            ]           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            ^           1262    5    postgres    DATABASE     �   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE postgres;
                postgres    false            _           0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    3678            
            2615    42396    customer    SCHEMA        CREATE SCHEMA customer;
    DROP SCHEMA customer;
                postgres    false                        2615    42164    order    SCHEMA        CREATE SCHEMA "order";
    DROP SCHEMA "order";
                postgres    false                        2615    42427    payment    SCHEMA        CREATE SCHEMA payment;
    DROP SCHEMA payment;
                postgres    false            	            2615    42321 
   restaurant    SCHEMA        CREATE SCHEMA restaurant;
    DROP SCHEMA restaurant;
                postgres    false                        3079    16384 	   adminpack 	   EXTENSION     A   CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;
    DROP EXTENSION adminpack;
                   false            `           0    0    EXTENSION adminpack    COMMENT     M   COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';
                        false    2                        3079    41610 	   uuid-ossp 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;
    DROP EXTENSION "uuid-ossp";
                   false            a           0    0    EXTENSION "uuid-ossp"    COMMENT     W   COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';
                        false    3            �           1247    42166    order_status    TYPE     {   CREATE TYPE "order".order_status AS ENUM (
    'PENDING',
    'PAID',
    'APPROVED',
    'CANCELLED',
    'CANCELLING'
);
     DROP TYPE "order".order_status;
       order          postgres    false    8            �           1247    42429    payment_status    TYPE     _   CREATE TYPE payment.payment_status AS ENUM (
    'COMPLETED',
    'CANCELLED',
    'FAILED'
);
 "   DROP TYPE payment.payment_status;
       payment          postgres    false    11            �           1247    42446    transaction_type    TYPE     L   CREATE TYPE payment.transaction_type AS ENUM (
    'DEBIT',
    'CREDIT'
);
 $   DROP TYPE payment.transaction_type;
       payment          postgres    false    11            �           1247    41629    approval_status    TYPE     O   CREATE TYPE public.approval_status AS ENUM (
    'APPROVED',
    'REJECTED'
);
 "   DROP TYPE public.approval_status;
       public          postgres    false            �           1247    42330    approval_status    TYPE     S   CREATE TYPE restaurant.approval_status AS ENUM (
    'APPROVED',
    'REJECTED'
);
 &   DROP TYPE restaurant.approval_status;
    
   restaurant          postgres    false    9                       1255    42415    refresh_order_customer_m_view()    FUNCTION     �   CREATE FUNCTION customer.refresh_order_customer_m_view() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    refresh materialized VIEW customer.order_customer_m_view;
    return null;
END;
$$;
 8   DROP FUNCTION customer.refresh_order_customer_m_view();
       customer          postgres    false    10                       1255    42373 !   refresh_order_restaurant_m_view()    FUNCTION     �   CREATE FUNCTION restaurant.refresh_order_restaurant_m_view() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    refresh materialized VIEW restaurant.order_restaurant_m_view;
    return null;
END;
$$;
 <   DROP FUNCTION restaurant.refresh_order_restaurant_m_view();
    
   restaurant          postgres    false    9            	           1259    42397 	   customers    TABLE     �   CREATE TABLE customer.customers (
    id uuid NOT NULL,
    username character varying NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL
);
    DROP TABLE customer.customers;
       customer         heap    postgres    false    10            
           1259    42404    order_customer_m_view    MATERIALIZED VIEW     �   CREATE MATERIALIZED VIEW customer.order_customer_m_view AS
 SELECT customers.id,
    customers.username,
    customers.first_name,
    customers.last_name
   FROM customer.customers
  WITH NO DATA;
 7   DROP MATERIALIZED VIEW customer.order_customer_m_view;
       customer         heap    postgres    false    265    265    265    265    10                       1259    42194    order_address    TABLE     �   CREATE TABLE "order".order_address (
    id uuid NOT NULL,
    order_id uuid NOT NULL,
    street character varying NOT NULL,
    postal_code character varying NOT NULL,
    city character varying NOT NULL
);
 "   DROP TABLE "order".order_address;
       order         heap    postgres    false    8                       1259    42184    order_items    TABLE     �   CREATE TABLE "order".order_items (
    id bigint NOT NULL,
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    quantity integer NOT NULL,
    sub_total numeric(10,2) NOT NULL
);
     DROP TABLE "order".order_items;
       order         heap    postgres    false    8                       1259    42177    orders    TABLE       CREATE TABLE "order".orders (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    tracking_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    order_status "order".order_status NOT NULL,
    failure_messages character varying
);
    DROP TABLE "order".orders;
       order         heap    postgres    false    904    8                       1259    42440    credit_entry    TABLE     �   CREATE TABLE payment.credit_entry (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    total_credit_amount numeric(10,2) NOT NULL
);
 !   DROP TABLE payment.credit_entry;
       payment         heap    postgres    false    11                       1259    42451    credit_history    TABLE     �   CREATE TABLE payment.credit_history (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    type payment.transaction_type NOT NULL
);
 #   DROP TABLE payment.credit_history;
       payment         heap    postgres    false    11    1012                       1259    42435    payments    TABLE     �   CREATE TABLE payment.payments (
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    status payment.payment_status NOT NULL
);
    DROP TABLE payment.payments;
       payment         heap    postgres    false    11    1003            �            1259    41360 	   authority    TABLE     {   CREATE TABLE public.authority (
    name character varying(100) NOT NULL,
    full_name character varying(100) NOT NULL
);
    DROP TABLE public.authority;
       public         heap    postgres    false            �            1259    41413    brand    TABLE     v   CREATE TABLE public.brand (
    id bigint NOT NULL,
    code integer NOT NULL,
    name character varying NOT NULL
);
    DROP TABLE public.brand;
       public         heap    postgres    false            �            1259    41412    brand_id_seq    SEQUENCE     �   ALTER TABLE public.brand ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.brand_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    233            �            1259    41425    brand_model    TABLE     �   CREATE TABLE public.brand_model (
    id bigint NOT NULL,
    brand_id bigint,
    code integer NOT NULL,
    name character varying NOT NULL
);
    DROP TABLE public.brand_model;
       public         heap    postgres    false            �            1259    41424    brand_model_id_seq    SEQUENCE     �   ALTER TABLE public.brand_model ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.brand_model_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    235            �            1259    41403    car    TABLE       CREATE TABLE public.car (
    id bigint NOT NULL,
    car_provider_id bigint NOT NULL,
    registration_number uuid NOT NULL,
    brand_code integer NOT NULL,
    brand_model_code integer NOT NULL,
    production_year date NOT NULL,
    max_speed integer NOT NULL,
    horse_power integer NOT NULL,
    rgb_code character varying,
    description character varying,
    category_code integer,
    booking_status_code integer NOT NULL,
    price double precision NOT NULL,
    img_url character varying NOT NULL,
    is_visible boolean NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
    DROP TABLE public.car;
       public         heap    postgres    false            �            1259    41567    car_booking    TABLE     !  CREATE TABLE public.car_booking (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    car_user_id bigint NOT NULL,
    check_in_date date NOT NULL,
    check_out_date date NOT NULL,
    status_code integer NOT NULL,
    tax numeric(10,2) NOT NULL,
    final_price numeric(10,2) NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
    DROP TABLE public.car_booking;
       public         heap    postgres    false            �            1259    41566    car_booking_id_seq    SEQUENCE     �   ALTER TABLE public.car_booking ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_booking_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    254            �            1259    41402 
   car_id_seq    SEQUENCE     �   ALTER TABLE public.car ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    231            �            1259    41437    car_provider    TABLE     �  CREATE TABLE public.car_provider (
    id bigint NOT NULL,
    name character varying NOT NULL,
    cr_number character varying NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
     DROP TABLE public.car_provider;
       public         heap    postgres    false                        1259    41585    car_provider_address    TABLE     �  CREATE TABLE public.car_provider_address (
    id bigint NOT NULL,
    car_provider_id bigint,
    city_id bigint,
    location character varying(50),
    postal_code character varying(50),
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
 (   DROP TABLE public.car_provider_address;
       public         heap    postgres    false            �            1259    41584    car_provider_address_id_seq    SEQUENCE     �   ALTER TABLE public.car_provider_address ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_provider_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    256            �            1259    41477    car_provider_coupon    TABLE     W  CREATE TABLE public.car_provider_coupon (
    id bigint NOT NULL,
    car_provider_id bigint NOT NULL,
    coupon_code character varying NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    num_of_user_per_user integer NOT NULL,
    is_available boolean DEFAULT true NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false,
    discount_percentage double precision NOT NULL
);
 '   DROP TABLE public.car_provider_coupon;
       public         heap    postgres    false            �            1259    41476    car_provider_coupon_id_seq    SEQUENCE     �   ALTER TABLE public.car_provider_coupon ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_provider_coupon_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    244            �            1259    41436    car_provider_id_seq    SEQUENCE     �   ALTER TABLE public.car_provider ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_provider_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    237            �            1259    41467    car_provider_user    TABLE     �  CREATE TABLE public.car_provider_user (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    car_provider_id bigint,
    company_admin boolean,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
 %   DROP TABLE public.car_provider_user;
       public         heap    postgres    false            �            1259    41466    car_provider_user_id_seq    SEQUENCE     �   ALTER TABLE public.car_provider_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_provider_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    242            �            1259    41540    car_user    TABLE       CREATE TABLE public.car_user (
    id bigint NOT NULL,
    car_id bigint NOT NULL,
    registration_number uuid NOT NULL,
    brand_code integer NOT NULL,
    brand_model_code integer NOT NULL,
    production_year date NOT NULL,
    max_speed integer NOT NULL,
    horse_power integer NOT NULL,
    rgb_code character varying,
    description character varying NOT NULL,
    category_code integer NOT NULL,
    booking_status_code integer NOT NULL,
    price double precision NOT NULL,
    img_url character varying NOT NULL
);
    DROP TABLE public.car_user;
       public         heap    postgres    false            �            1259    41539    car_user_id_seq    SEQUENCE     �   ALTER TABLE public.car_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.car_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    250            �            1259    41502    city    TABLE        CREATE TABLE public.city (
    id bigint NOT NULL,
    country_id bigint NOT NULL,
    name character varying(255) NOT NULL
);
    DROP TABLE public.city;
       public         heap    postgres    false            �            1259    41501    city_id_seq    SEQUENCE     �   ALTER TABLE public.city ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    248            �            1259    41490    country    TABLE     �   CREATE TABLE public.country (
    id bigint NOT NULL,
    name character varying NOT NULL,
    cc_code character varying(3) NOT NULL,
    calling_code character varying(5) NOT NULL
);
    DROP TABLE public.country;
       public         heap    postgres    false            �            1259    41489    country_id_seq    SEQUENCE     �   ALTER TABLE public.country ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.country_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    246            �            1259    41345    cr_user    TABLE     v  CREATE TABLE public.cr_user (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    first_name character varying(50) NOT NULL,
    last_name character varying(50),
    email character varying(255) NOT NULL,
    nin character varying(15) NOT NULL,
    password character varying(255) NOT NULL,
    type_code integer NOT NULL,
    status_code integer NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
    DROP TABLE public.cr_user;
       public         heap    postgres    false            �            1259    41344    cr_user_id_seq    SEQUENCE     �   ALTER TABLE public.cr_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.cr_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    223            �            1259    41339    databasechangelog    TABLE     Y  CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);
 %   DROP TABLE public.databasechangelog;
       public         heap    postgres    false            �            1259    41334    databasechangeloglock    TABLE     �   CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);
 )   DROP TABLE public.databasechangeloglock;
       public         heap    postgres    false            �            1259    41457    driver_license    TABLE     �  CREATE TABLE public.driver_license (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    number character varying NOT NULL,
    issued_in bigint NOT NULL,
    issued_at date NOT NULL,
    expired_date date NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
 "   DROP TABLE public.driver_license;
       public         heap    postgres    false            �            1259    41456    driver_license_id_seq    SEQUENCE     �   ALTER TABLE public.driver_license ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.driver_license_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    240            �            1259    41379    lookup_code    TABLE     �   CREATE TABLE public.lookup_code (
    id bigint NOT NULL,
    lookup_key character varying(100) NOT NULL,
    code integer NOT NULL,
    name character varying(200) NOT NULL
);
    DROP TABLE public.lookup_code;
       public         heap    postgres    false            �            1259    41378    lookup_code_id_seq    SEQUENCE     �   ALTER TABLE public.lookup_code ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.lookup_code_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    227            �            1259    41455 
   seq__brand    SEQUENCE     �   CREATE SEQUENCE public.seq__brand
    AS integer
    START WITH 1000
    INCREMENT BY 1000
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.seq__brand;
       public          postgres    false            �            1259    41388    user_address    TABLE     �  CREATE TABLE public.user_address (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    city_id bigint NOT NULL,
    location character varying(200),
    postal_code character varying(200) NOT NULL,
    created_by character varying(50) NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    last_modified_by character varying(50),
    last_modified_date timestamp without time zone,
    is_deleted boolean DEFAULT false
);
     DROP TABLE public.user_address;
       public         heap    postgres    false            �            1259    41387    user_address_id_seq    SEQUENCE     �   ALTER TABLE public.user_address ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_address_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    229            �            1259    41365    user_authority    TABLE     x   CREATE TABLE public.user_authority (
    authority_name character varying(100) NOT NULL,
    user_id bigint NOT NULL
);
 "   DROP TABLE public.user_authority;
       public         heap    postgres    false            �            1259    41553    user_notification    TABLE     �   CREATE TABLE public.user_notification (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    message character varying NOT NULL,
    medium_code integer NOT NULL,
    is_sent boolean NOT NULL,
    sent_date timestamp without time zone
);
 %   DROP TABLE public.user_notification;
       public         heap    postgres    false            �            1259    41552    user_notification_id_seq    SEQUENCE     �   ALTER TABLE public.user_notification ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    252                       1259    42335    order_approval    TABLE     �   CREATE TABLE restaurant.order_approval (
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    order_id uuid NOT NULL,
    status restaurant.approval_status NOT NULL
);
 &   DROP TABLE restaurant.order_approval;
    
   restaurant         heap    postgres    false    982    9                       1259    42340    products    TABLE     �   CREATE TABLE restaurant.products (
    id uuid NOT NULL,
    name character varying NOT NULL,
    price numeric(10,2) NOT NULL,
    available boolean NOT NULL
);
     DROP TABLE restaurant.products;
    
   restaurant         heap    postgres    false    9                       1259    42347    restaurant_products    TABLE     �   CREATE TABLE restaurant.restaurant_products (
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    product_id uuid NOT NULL
);
 +   DROP TABLE restaurant.restaurant_products;
    
   restaurant         heap    postgres    false    9                       1259    42322    restaurants    TABLE     �   CREATE TABLE restaurant.restaurants (
    id uuid NOT NULL,
    name character varying NOT NULL,
    active boolean NOT NULL
);
 #   DROP TABLE restaurant.restaurants;
    
   restaurant         heap    postgres    false    9                       1259    42362    order_restaurant_m_view    MATERIALIZED VIEW     �  CREATE MATERIALIZED VIEW restaurant.order_restaurant_m_view AS
 SELECT r.id AS restaurant_id,
    r.name AS restaurant_name,
    r.active AS restaurant_active,
    p.id AS product_id,
    p.name AS product_name,
    p.price AS product_price,
    p.available AS product_available
   FROM restaurant.restaurants r,
    restaurant.products p,
    restaurant.restaurant_products rp
  WHERE ((r.id = rp.restaurant_id) AND (p.id = rp.product_id))
  WITH NO DATA;
 ;   DROP MATERIALIZED VIEW restaurant.order_restaurant_m_view;
    
   restaurant         heap    postgres    false    262    263    260    263    262    262    260    262    260    9            T          0    42397 	   customers 
   TABLE DATA           J   COPY customer.customers (id, username, first_name, last_name) FROM stdin;
    customer          postgres    false    265   N�       N          0    42194    order_address 
   TABLE DATA           Q   COPY "order".order_address (id, order_id, street, postal_code, city) FROM stdin;
    order          postgres    false    259   ��       M          0    42184    order_items 
   TABLE DATA           \   COPY "order".order_items (id, order_id, product_id, price, quantity, sub_total) FROM stdin;
    order          postgres    false    258   ��       L          0    42177    orders 
   TABLE DATA           u   COPY "order".orders (id, customer_id, restaurant_id, tracking_id, price, order_status, failure_messages) FROM stdin;
    order          postgres    false    257   n�       W          0    42440    credit_entry 
   TABLE DATA           M   COPY payment.credit_entry (id, customer_id, total_credit_amount) FROM stdin;
    payment          postgres    false    268   ��       X          0    42451    credit_history 
   TABLE DATA           H   COPY payment.credit_history (id, customer_id, amount, type) FROM stdin;
    payment          postgres    false    269   �       V          0    42435    payments 
   TABLE DATA           Y   COPY payment.payments (id, customer_id, order_id, price, created_at, status) FROM stdin;
    payment          postgres    false    267   ��       +          0    41360 	   authority 
   TABLE DATA           4   COPY public.authority (name, full_name) FROM stdin;
    public          postgres    false    224   ��       4          0    41413    brand 
   TABLE DATA           /   COPY public.brand (id, code, name) FROM stdin;
    public          postgres    false    233   �       6          0    41425    brand_model 
   TABLE DATA           ?   COPY public.brand_model (id, brand_id, code, name) FROM stdin;
    public          postgres    false    235   R�       2          0    41403    car 
   TABLE DATA           1  COPY public.car (id, car_provider_id, registration_number, brand_code, brand_model_code, production_year, max_speed, horse_power, rgb_code, description, category_code, booking_status_code, price, img_url, is_visible, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    231   ��       I          0    41567    car_booking 
   TABLE DATA           �   COPY public.car_booking (id, user_id, car_user_id, check_in_date, check_out_date, status_code, tax, final_price, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    254   i�       8          0    41437    car_provider 
   TABLE DATA           �   COPY public.car_provider (id, name, cr_number, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    237   ��       K          0    41585    car_provider_address 
   TABLE DATA           �   COPY public.car_provider_address (id, car_provider_id, city_id, location, postal_code, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    256   ��       ?          0    41477    car_provider_coupon 
   TABLE DATA           �   COPY public.car_provider_coupon (id, car_provider_id, coupon_code, start_date, end_date, num_of_user_per_user, is_available, created_by, created_date, last_modified_by, last_modified_date, is_deleted, discount_percentage) FROM stdin;
    public          postgres    false    244   �       =          0    41467    car_provider_user 
   TABLE DATA           �   COPY public.car_provider_user (id, user_id, car_provider_id, company_admin, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    242   $�       E          0    41540    car_user 
   TABLE DATA           �   COPY public.car_user (id, car_id, registration_number, brand_code, brand_model_code, production_year, max_speed, horse_power, rgb_code, description, category_code, booking_status_code, price, img_url) FROM stdin;
    public          postgres    false    250   n�       C          0    41502    city 
   TABLE DATA           4   COPY public.city (id, country_id, name) FROM stdin;
    public          postgres    false    248   ��       A          0    41490    country 
   TABLE DATA           B   COPY public.country (id, name, cc_code, calling_code) FROM stdin;
    public          postgres    false    246   ��       *          0    41345    cr_user 
   TABLE DATA           �   COPY public.cr_user (id, username, first_name, last_name, email, nin, password, type_code, status_code, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    223   ��       (          0    41339    databasechangelog 
   TABLE DATA           �   COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
    public          postgres    false    221   ��       '          0    41334    databasechangeloglock 
   TABLE DATA           R   COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
    public          postgres    false    220   �      ;          0    41457    driver_license 
   TABLE DATA           �   COPY public.driver_license (id, user_id, number, issued_in, issued_at, expired_date, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    240   �      .          0    41379    lookup_code 
   TABLE DATA           A   COPY public.lookup_code (id, lookup_key, code, name) FROM stdin;
    public          postgres    false    227   K      0          0    41388    user_address 
   TABLE DATA           �   COPY public.user_address (id, user_id, city_id, location, postal_code, created_by, created_date, last_modified_by, last_modified_date, is_deleted) FROM stdin;
    public          postgres    false    229         ,          0    41365    user_authority 
   TABLE DATA           A   COPY public.user_authority (authority_name, user_id) FROM stdin;
    public          postgres    false    225         G          0    41553    user_notification 
   TABLE DATA           b   COPY public.user_notification (id, user_id, message, medium_code, is_sent, sent_date) FROM stdin;
    public          postgres    false    252   Z      P          0    42335    order_approval 
   TABLE DATA           Q   COPY restaurant.order_approval (id, restaurant_id, order_id, status) FROM stdin;
 
   restaurant          postgres    false    261   w      Q          0    42340    products 
   TABLE DATA           B   COPY restaurant.products (id, name, price, available) FROM stdin;
 
   restaurant          postgres    false    262   +      R          0    42347    restaurant_products 
   TABLE DATA           P   COPY restaurant.restaurant_products (id, restaurant_id, product_id) FROM stdin;
 
   restaurant          postgres    false    263   �      O          0    42322    restaurants 
   TABLE DATA           ;   COPY restaurant.restaurants (id, name, active) FROM stdin;
 
   restaurant          postgres    false    260   �      b           0    0    brand_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.brand_id_seq', 3, true);
          public          postgres    false    232            c           0    0    brand_model_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.brand_model_id_seq', 3, true);
          public          postgres    false    234            d           0    0    car_booking_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.car_booking_id_seq', 1, false);
          public          postgres    false    253            e           0    0 
   car_id_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.car_id_seq', 4, true);
          public          postgres    false    230            f           0    0    car_provider_address_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.car_provider_address_id_seq', 1, false);
          public          postgres    false    255            g           0    0    car_provider_coupon_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.car_provider_coupon_id_seq', 1, false);
          public          postgres    false    243            h           0    0    car_provider_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.car_provider_id_seq', 1, true);
          public          postgres    false    236            i           0    0    car_provider_user_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.car_provider_user_id_seq', 1, true);
          public          postgres    false    241            j           0    0    car_user_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.car_user_id_seq', 1, false);
          public          postgres    false    249            k           0    0    city_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.city_id_seq', 1, false);
          public          postgres    false    247            l           0    0    country_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.country_id_seq', 1, false);
          public          postgres    false    245            m           0    0    cr_user_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.cr_user_id_seq', 8, true);
          public          postgres    false    222            n           0    0    driver_license_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.driver_license_id_seq', 1, true);
          public          postgres    false    239            o           0    0    lookup_code_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.lookup_code_id_seq', 12, true);
          public          postgres    false    226            p           0    0 
   seq__brand    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.seq__brand', 3000, true);
          public          postgres    false    238            q           0    0    user_address_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.user_address_id_seq', 1, false);
          public          postgres    false    228            r           0    0    user_notification_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.user_notification_id_seq', 1, false);
          public          postgres    false    251            z           2606    42403    customers customers_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY customer.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY customer.customers DROP CONSTRAINT customers_pkey;
       customer            postgres    false    265            n           2606    42202 (   order_address order_address_order_id_key 
   CONSTRAINT     h   ALTER TABLE ONLY "order".order_address
    ADD CONSTRAINT order_address_order_id_key UNIQUE (order_id);
 S   ALTER TABLE ONLY "order".order_address DROP CONSTRAINT order_address_order_id_key;
       order            postgres    false    259            p           2606    42200     order_address order_address_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY "order".order_address
    ADD CONSTRAINT order_address_pkey PRIMARY KEY (id, order_id);
 K   ALTER TABLE ONLY "order".order_address DROP CONSTRAINT order_address_pkey;
       order            postgres    false    259    259            l           2606    42188    order_items order_items_pkey 
   CONSTRAINT     e   ALTER TABLE ONLY "order".order_items
    ADD CONSTRAINT order_items_pkey PRIMARY KEY (id, order_id);
 G   ALTER TABLE ONLY "order".order_items DROP CONSTRAINT order_items_pkey;
       order            postgres    false    258    258            j           2606    42183    orders orders_pkey 
   CONSTRAINT     Q   ALTER TABLE ONLY "order".orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);
 =   ALTER TABLE ONLY "order".orders DROP CONSTRAINT orders_pkey;
       order            postgres    false    257            ~           2606    42444    credit_entry credit_entry_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY payment.credit_entry
    ADD CONSTRAINT credit_entry_pkey PRIMARY KEY (id);
 I   ALTER TABLE ONLY payment.credit_entry DROP CONSTRAINT credit_entry_pkey;
       payment            postgres    false    268            �           2606    42455 "   credit_history credit_history_pkey 
   CONSTRAINT     a   ALTER TABLE ONLY payment.credit_history
    ADD CONSTRAINT credit_history_pkey PRIMARY KEY (id);
 M   ALTER TABLE ONLY payment.credit_history DROP CONSTRAINT credit_history_pkey;
       payment            postgres    false    269            |           2606    42439    payments payments_pkey 
   CONSTRAINT     U   ALTER TABLE ONLY payment.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);
 A   ALTER TABLE ONLY payment.payments DROP CONSTRAINT payments_pkey;
       payment            postgres    false    267            4           2606    41364    authority authority_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.authority
    ADD CONSTRAINT authority_pkey PRIMARY KEY (name);
 B   ALTER TABLE ONLY public.authority DROP CONSTRAINT authority_pkey;
       public            postgres    false    224            D           2606    41431    brand_model brand_model_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.brand_model
    ADD CONSTRAINT brand_model_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.brand_model DROP CONSTRAINT brand_model_pkey;
       public            postgres    false    235            >           2606    41419    brand brand_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.brand
    ADD CONSTRAINT brand_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.brand DROP CONSTRAINT brand_pkey;
       public            postgres    false    233            f           2606    41571    car_booking car_booking_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.car_booking
    ADD CONSTRAINT car_booking_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.car_booking DROP CONSTRAINT car_booking_pkey;
       public            postgres    false    254            <           2606    41409    car car_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.car
    ADD CONSTRAINT car_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.car DROP CONSTRAINT car_pkey;
       public            postgres    false    231            h           2606    41589 .   car_provider_address car_provider_address_pkey 
   CONSTRAINT     l   ALTER TABLE ONLY public.car_provider_address
    ADD CONSTRAINT car_provider_address_pkey PRIMARY KEY (id);
 X   ALTER TABLE ONLY public.car_provider_address DROP CONSTRAINT car_provider_address_pkey;
       public            postgres    false    256            V           2606    41484 ,   car_provider_coupon car_provider_coupon_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.car_provider_coupon
    ADD CONSTRAINT car_provider_coupon_pkey PRIMARY KEY (id);
 V   ALTER TABLE ONLY public.car_provider_coupon DROP CONSTRAINT car_provider_coupon_pkey;
       public            postgres    false    244            J           2606    41447 '   car_provider car_provider_cr_number_key 
   CONSTRAINT     g   ALTER TABLE ONLY public.car_provider
    ADD CONSTRAINT car_provider_cr_number_key UNIQUE (cr_number);
 Q   ALTER TABLE ONLY public.car_provider DROP CONSTRAINT car_provider_cr_number_key;
       public            postgres    false    237            L           2606    41445 "   car_provider car_provider_name_key 
   CONSTRAINT     ]   ALTER TABLE ONLY public.car_provider
    ADD CONSTRAINT car_provider_name_key UNIQUE (name);
 L   ALTER TABLE ONLY public.car_provider DROP CONSTRAINT car_provider_name_key;
       public            postgres    false    237            N           2606    41443    car_provider car_provider_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.car_provider
    ADD CONSTRAINT car_provider_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.car_provider DROP CONSTRAINT car_provider_pkey;
       public            postgres    false    237            R           2606    41471 (   car_provider_user car_provider_user_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.car_provider_user
    ADD CONSTRAINT car_provider_user_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.car_provider_user DROP CONSTRAINT car_provider_user_pkey;
       public            postgres    false    242            b           2606    41546    car_user car_user_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.car_user
    ADD CONSTRAINT car_user_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.car_user DROP CONSTRAINT car_user_pkey;
       public            postgres    false    250            `           2606    41506    city city_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.city
    ADD CONSTRAINT city_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.city DROP CONSTRAINT city_pkey;
       public            postgres    false    248            Z           2606    41500     country country_calling_code_key 
   CONSTRAINT     c   ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_calling_code_key UNIQUE (calling_code);
 J   ALTER TABLE ONLY public.country DROP CONSTRAINT country_calling_code_key;
       public            postgres    false    246            \           2606    41498    country country_cc_code_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_cc_code_key UNIQUE (cc_code);
 E   ALTER TABLE ONLY public.country DROP CONSTRAINT country_cc_code_key;
       public            postgres    false    246            ^           2606    41496    country country_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.country DROP CONSTRAINT country_pkey;
       public            postgres    false    246            ,           2606    41355    cr_user cr_user_email_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.cr_user
    ADD CONSTRAINT cr_user_email_key UNIQUE (email);
 C   ALTER TABLE ONLY public.cr_user DROP CONSTRAINT cr_user_email_key;
       public            postgres    false    223            .           2606    41357    cr_user cr_user_nin_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.cr_user
    ADD CONSTRAINT cr_user_nin_key UNIQUE (nin);
 A   ALTER TABLE ONLY public.cr_user DROP CONSTRAINT cr_user_nin_key;
       public            postgres    false    223            0           2606    41351    cr_user cr_user_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.cr_user
    ADD CONSTRAINT cr_user_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.cr_user DROP CONSTRAINT cr_user_pkey;
       public            postgres    false    223            2           2606    41353    cr_user cr_user_username_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.cr_user
    ADD CONSTRAINT cr_user_username_key UNIQUE (username);
 F   ALTER TABLE ONLY public.cr_user DROP CONSTRAINT cr_user_username_key;
       public            postgres    false    223            *           2606    41338 0   databasechangeloglock databasechangeloglock_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY public.databasechangeloglock DROP CONSTRAINT databasechangeloglock_pkey;
       public            postgres    false    220            P           2606    41463 "   driver_license driver_license_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.driver_license
    ADD CONSTRAINT driver_license_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.driver_license DROP CONSTRAINT driver_license_pkey;
       public            postgres    false    240            6           2606    41383    lookup_code lookup_code_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.lookup_code
    ADD CONSTRAINT lookup_code_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.lookup_code DROP CONSTRAINT lookup_code_pkey;
       public            postgres    false    227            :           2606    41392    user_address user_address_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.user_address
    ADD CONSTRAINT user_address_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.user_address DROP CONSTRAINT user_address_pkey;
       public            postgres    false    229            d           2606    41559 (   user_notification user_notification_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.user_notification
    ADD CONSTRAINT user_notification_pkey PRIMARY KEY (id);
 R   ALTER TABLE ONLY public.user_notification DROP CONSTRAINT user_notification_pkey;
       public            postgres    false    252            @           2606    41421    brand ux_brand__code 
   CONSTRAINT     O   ALTER TABLE ONLY public.brand
    ADD CONSTRAINT ux_brand__code UNIQUE (code);
 >   ALTER TABLE ONLY public.brand DROP CONSTRAINT ux_brand__code;
       public            postgres    false    233            B           2606    41423    brand ux_brand__name 
   CONSTRAINT     O   ALTER TABLE ONLY public.brand
    ADD CONSTRAINT ux_brand__name UNIQUE (name);
 >   ALTER TABLE ONLY public.brand DROP CONSTRAINT ux_brand__name;
       public            postgres    false    233            F           2606    41433     brand_model ux_brand_model__code 
   CONSTRAINT     [   ALTER TABLE ONLY public.brand_model
    ADD CONSTRAINT ux_brand_model__code UNIQUE (code);
 J   ALTER TABLE ONLY public.brand_model DROP CONSTRAINT ux_brand_model__code;
       public            postgres    false    235            H           2606    41435     brand_model ux_brand_model__name 
   CONSTRAINT     [   ALTER TABLE ONLY public.brand_model
    ADD CONSTRAINT ux_brand_model__name UNIQUE (name);
 J   ALTER TABLE ONLY public.brand_model DROP CONSTRAINT ux_brand_model__name;
       public            postgres    false    235            X           2606    41486 ;   car_provider_coupon ux_car_provider_coupon__car_provider_id 
   CONSTRAINT     }   ALTER TABLE ONLY public.car_provider_coupon
    ADD CONSTRAINT ux_car_provider_coupon__car_provider_id UNIQUE (coupon_code);
 e   ALTER TABLE ONLY public.car_provider_coupon DROP CONSTRAINT ux_car_provider_coupon__car_provider_id;
       public            postgres    false    244            T           2606    41473 -   car_provider_user ux_car_provider_id__user_id 
   CONSTRAINT     k   ALTER TABLE ONLY public.car_provider_user
    ADD CONSTRAINT ux_car_provider_id__user_id UNIQUE (user_id);
 W   ALTER TABLE ONLY public.car_provider_user DROP CONSTRAINT ux_car_provider_id__user_id;
       public            postgres    false    242            8           2606    41385 %   lookup_code ux_lookup_code__key__code 
   CONSTRAINT     l   ALTER TABLE ONLY public.lookup_code
    ADD CONSTRAINT ux_lookup_code__key__code UNIQUE (lookup_key, code);
 O   ALTER TABLE ONLY public.lookup_code DROP CONSTRAINT ux_lookup_code__key__code;
       public            postgres    false    227    227            t           2606    42339 "   order_approval order_approval_pkey 
   CONSTRAINT     d   ALTER TABLE ONLY restaurant.order_approval
    ADD CONSTRAINT order_approval_pkey PRIMARY KEY (id);
 P   ALTER TABLE ONLY restaurant.order_approval DROP CONSTRAINT order_approval_pkey;
    
   restaurant            postgres    false    261            v           2606    42346    products products_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY restaurant.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY restaurant.products DROP CONSTRAINT products_pkey;
    
   restaurant            postgres    false    262            x           2606    42351 ,   restaurant_products restaurant_products_pkey 
   CONSTRAINT     n   ALTER TABLE ONLY restaurant.restaurant_products
    ADD CONSTRAINT restaurant_products_pkey PRIMARY KEY (id);
 Z   ALTER TABLE ONLY restaurant.restaurant_products DROP CONSTRAINT restaurant_products_pkey;
    
   restaurant            postgres    false    263            r           2606    42328    restaurants restaurants_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY restaurant.restaurants
    ADD CONSTRAINT restaurants_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY restaurant.restaurants DROP CONSTRAINT restaurants_pkey;
    
   restaurant            postgres    false    260            �           2620    42416 '   customers refresh_order_customer_m_view    TRIGGER     �   CREATE TRIGGER refresh_order_customer_m_view AFTER INSERT OR DELETE OR UPDATE OR TRUNCATE ON customer.customers FOR EACH STATEMENT EXECUTE FUNCTION customer.refresh_order_customer_m_view();
 B   DROP TRIGGER refresh_order_customer_m_view ON customer.customers;
       customer          postgres    false    281    265            �           2620    42374 3   restaurant_products refresh_order_restaurant_m_view    TRIGGER     �   CREATE TRIGGER refresh_order_restaurant_m_view AFTER INSERT OR DELETE OR UPDATE OR TRUNCATE ON restaurant.restaurant_products FOR EACH STATEMENT EXECUTE FUNCTION restaurant.refresh_order_restaurant_m_view();
 P   DROP TRIGGER refresh_order_restaurant_m_view ON restaurant.restaurant_products;
    
   restaurant          postgres    false    263    280            �           2606    42189    order_items FK_ORDER_ID    FK CONSTRAINT     �   ALTER TABLE ONLY "order".order_items
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id) REFERENCES "order".orders(id) ON DELETE CASCADE NOT VALID;
 D   ALTER TABLE ONLY "order".order_items DROP CONSTRAINT "FK_ORDER_ID";
       order          postgres    false    258    257    3434            �           2606    42203    order_address FK_ORDER_ID    FK CONSTRAINT     �   ALTER TABLE ONLY "order".order_address
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id) REFERENCES "order".orders(id) ON DELETE CASCADE NOT VALID;
 F   ALTER TABLE ONLY "order".order_address DROP CONSTRAINT "FK_ORDER_ID";
       order          postgres    false    3434    257    259            �           2606    41450    car fk_car__car_provider_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car
    ADD CONSTRAINT fk_car__car_provider_id FOREIGN KEY (car_provider_id) REFERENCES public.car_provider(id);
 E   ALTER TABLE ONLY public.car DROP CONSTRAINT fk_car__car_provider_id;
       public          postgres    false    231    237    3406            �           2606    41579 '   car_booking fk_car_booking__car_user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_booking
    ADD CONSTRAINT fk_car_booking__car_user_id FOREIGN KEY (car_user_id) REFERENCES public.car_user(id);
 Q   ALTER TABLE ONLY public.car_booking DROP CONSTRAINT fk_car_booking__car_user_id;
       public          postgres    false    3426    250    254            �           2606    41574 #   car_booking fk_car_booking__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_booking
    ADD CONSTRAINT fk_car_booking__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 M   ALTER TABLE ONLY public.car_booking DROP CONSTRAINT fk_car_booking__user_id;
       public          postgres    false    3376    254    223            �           2606    41592 =   car_provider_address fk_car_provider_address__car_provider_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_provider_address
    ADD CONSTRAINT fk_car_provider_address__car_provider_id FOREIGN KEY (car_provider_id) REFERENCES public.car_provider(id);
 g   ALTER TABLE ONLY public.car_provider_address DROP CONSTRAINT fk_car_provider_address__car_provider_id;
       public          postgres    false    3406    237    256            �           2606    41597 5   car_provider_address fk_car_provider_address__city_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_provider_address
    ADD CONSTRAINT fk_car_provider_address__city_id FOREIGN KEY (city_id) REFERENCES public.city(id);
 _   ALTER TABLE ONLY public.car_provider_address DROP CONSTRAINT fk_car_provider_address__city_id;
       public          postgres    false    248    256    3424            �           2606    41522 ;   car_provider_coupon fk_car_provider_coupon__car_provider_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_provider_coupon
    ADD CONSTRAINT fk_car_provider_coupon__car_provider_id FOREIGN KEY (car_provider_id) REFERENCES public.car_provider(id);
 e   ALTER TABLE ONLY public.car_provider_coupon DROP CONSTRAINT fk_car_provider_coupon__car_provider_id;
       public          postgres    false    244    3406    237            �           2606    41512 7   car_provider_user fk_car_provider_user__car_provider_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_provider_user
    ADD CONSTRAINT fk_car_provider_user__car_provider_id FOREIGN KEY (car_provider_id) REFERENCES public.car_provider(id);
 a   ALTER TABLE ONLY public.car_provider_user DROP CONSTRAINT fk_car_provider_user__car_provider_id;
       public          postgres    false    3406    237    242            �           2606    41517 /   car_provider_user fk_car_provider_user__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.car_provider_user
    ADD CONSTRAINT fk_car_provider_user__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 Y   ALTER TABLE ONLY public.car_provider_user DROP CONSTRAINT fk_car_provider_user__user_id;
       public          postgres    false    242    3376    223            �           2606    41547    car_user fk_car_user__car_id    FK CONSTRAINT     x   ALTER TABLE ONLY public.car_user
    ADD CONSTRAINT fk_car_user__car_id FOREIGN KEY (car_id) REFERENCES public.car(id);
 F   ALTER TABLE ONLY public.car_user DROP CONSTRAINT fk_car_user__car_id;
       public          postgres    false    231    250    3388            �           2606    41527    city fk_city__country_id    FK CONSTRAINT     |   ALTER TABLE ONLY public.city
    ADD CONSTRAINT fk_city__country_id FOREIGN KEY (country_id) REFERENCES public.country(id);
 B   ALTER TABLE ONLY public.city DROP CONSTRAINT fk_city__country_id;
       public          postgres    false    248    3422    246            �           2606    41507 )   driver_license fk_driver_license__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.driver_license
    ADD CONSTRAINT fk_driver_license__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 S   ALTER TABLE ONLY public.driver_license DROP CONSTRAINT fk_driver_license__user_id;
       public          postgres    false    240    3376    223            �           2606    41533 %   user_address fk_user_address__city_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_address
    ADD CONSTRAINT fk_user_address__city_id FOREIGN KEY (city_id) REFERENCES public.city(id);
 O   ALTER TABLE ONLY public.user_address DROP CONSTRAINT fk_user_address__city_id;
       public          postgres    false    229    248    3424            �           2606    41397 %   user_address fk_user_address__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_address
    ADD CONSTRAINT fk_user_address__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 O   ALTER TABLE ONLY public.user_address DROP CONSTRAINT fk_user_address__user_id;
       public          postgres    false    229    223    3376            �           2606    41368 0   user_authority fk_user_authority__authority_name    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_authority
    ADD CONSTRAINT fk_user_authority__authority_name FOREIGN KEY (authority_name) REFERENCES public.authority(name);
 Z   ALTER TABLE ONLY public.user_authority DROP CONSTRAINT fk_user_authority__authority_name;
       public          postgres    false    224    3380    225            �           2606    41373 )   user_authority fk_user_authority__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_authority
    ADD CONSTRAINT fk_user_authority__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 S   ALTER TABLE ONLY public.user_authority DROP CONSTRAINT fk_user_authority__user_id;
       public          postgres    false    225    3376    223            �           2606    41560 /   user_notification fk_user_notification__user_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_notification
    ADD CONSTRAINT fk_user_notification__user_id FOREIGN KEY (user_id) REFERENCES public.cr_user(id);
 Y   ALTER TABLE ONLY public.user_notification DROP CONSTRAINT fk_user_notification__user_id;
       public          postgres    false    223    252    3376            �           2606    42357 !   restaurant_products FK_PRODUCT_ID    FK CONSTRAINT     �   ALTER TABLE ONLY restaurant.restaurant_products
    ADD CONSTRAINT "FK_PRODUCT_ID" FOREIGN KEY (product_id) REFERENCES restaurant.products(id) ON DELETE RESTRICT NOT VALID;
 Q   ALTER TABLE ONLY restaurant.restaurant_products DROP CONSTRAINT "FK_PRODUCT_ID";
    
   restaurant          postgres    false    3446    263    262            �           2606    42352 $   restaurant_products FK_RESTAURANT_ID    FK CONSTRAINT     �   ALTER TABLE ONLY restaurant.restaurant_products
    ADD CONSTRAINT "FK_RESTAURANT_ID" FOREIGN KEY (restaurant_id) REFERENCES restaurant.restaurants(id) ON DELETE RESTRICT NOT VALID;
 T   ALTER TABLE ONLY restaurant.restaurant_products DROP CONSTRAINT "FK_RESTAURANT_ID";
    
   restaurant          postgres    false    263    260    3442            U           0    42404    order_customer_m_view    MATERIALIZED VIEW DATA     :   REFRESH MATERIALIZED VIEW customer.order_customer_m_view;
          customer          postgres    false    266    3674            S           0    42362    order_restaurant_m_view    MATERIALIZED VIEW DATA     >   REFRESH MATERIALIZED VIEW restaurant.order_restaurant_m_view;
       
   restaurant          postgres    false    264    3674            T   S   x�K124M2M��502��5II6յ�L4�55LK14�HNK21�,-N-�7�t�,*.�r�Rk3�h3�NM��K������ 7)�      N   �   x�}ϻm�1���.$��ҙ#@ �:7��G�\߁�Q�sL��P�+O��5}5�k��Hl��씶�n��F��c�^�g���_�D����x���q��T�)8m�"��2�jJu�Խh�	Z6�<d� ic5db1�`�`$k,؁*�1b��$3��+�D������������j�֖�R=7v;c;!PX�٥��Pvܜ�Q�14���\X"����u��� �m      M   �   x���1�1E����10�^�c�	?�#E�G�^�����"րK#�q8��x�9I���%;J�6h�8��� ܉ls[��j�ཱུ/S�;����<.�!�sm0����HY-��t��]�w~���`$%� ��1���t�m���.p��>iOR`�ij�9��t���ci-]n��������-�H      L   E  x����Nd1E����} FNb��Z	͎���q�D�� �����������ޣ��I�,�+`��Gk�9r������F*@��X�i��wB�Ddbs�_s-(3p%/I�`H3����a�����[��� �eT�G}��FP���h�326����ɴQ�SՃ��=r*�gi� T��yr`K\[.����c�Z��$��i@�>,ui���p���aw��J=$�%И/��œ im*����)��j������CN���1EF���������'���������xzڎ~�}cL�����z����>���r]׿d���      W   G   x��̹�0 �v���.ib#��Ep�ӕJ��$V�䵃r�F!]�{�B]��`ƻU�
�>�S(8      X   �   x���=�0๽�+;v�VhV������@%��X������̑W���b�Cʅ�S7�T�:���Hh"�q�޷����+N��p��c�{��vc��?3H`��
���!��`�l�ْ�?ޥj�	�j�B1gP{����� ���U['��I��;��|�؅ϱ���<�/[+�/      V   �   x���-r�0EQ������dI�������;����f������I�M��hi�F���(�d!�:�H�����f��5��}R��w'#�-9Ak�
�E_
�xO@�'��Nu#%F^o���u��a*�à#�y���p!�0���ӹ�f�d��[���r�̼r)E��Dw�b������q�7�{[���'J�      +   H   x���q�wt����tL����
	8;���y��q:')�e��A�}<]�B8�s2S�J�b���� l&      4   7   x�3�4400��urvuq�ur���2�4	:��ss��n�AA�A�\1z\\\ �
n      6   ?   x�3�4�4400�V03Vp�u�2��AD��KR��!bƜ�
f�
>�y)��9�%\1z\\\ �[      2   �   x����j1���S��c�'�ϐ*�΍t�c|g�q OoLj7�Y���Q`h(�%c�A�O�a)-c���$S�D�kB�H�$h�wK?X����:�K�9��N�2�%�����8o����=N��^����^y�:
����Y��|�V�iB�9���`d3���H����.���lLI��:�;�ι'(�Z�      I      x������ � �      8   T   x�3�LN,R((�/�LI-RH��-H̫�4426153��4��,.I��4202�50�54V04�22�21�353651%BEW� j��      K      x������ � �      ?      x������ � �      =   :   x�3��4�,��,.I��4202�50�54V04�22�21�357074$BEW� �      E      x������ � �      C      x������ � �      A      x������ � �      *   �   x����N�@ ���S����l�'-h�m�j�L�Ўe;PxzM��増I��G�hB�߆�Z��b���a�(�	e��!�	�1�wqQ�����T�|�,��$���xݥbV������5��zկZ/`���N(@���p�a�=�1���7Ӽˍ0�����ɼ����g�Ǫ��[�w&ȇ��tH��p��h@�e�6$��f����ޠ���w0a1)&�QY/в��XjL      (   �  x��[]o7}��
��H��凊}h�.�h�(��b
�L�:RV�����;#��yƱ��v"k��s.y�9$��� J̠�a��缺nR�&�������7b���o�ɟ�j�گ������VfB��@/�-�̍NB�m��w�~��}c�x2�4bV�T��s�.���i�6�}���u�ڷ?�����vy��ۦ���F͑;�����$I���`d�^?�7i�YL�l�SBAAkC�Xm	�VA&�:���Ƨ�ns}�i�lDd_:4�j�)��}L�1�т�(��\0i��h 4�f��f���>A�F!h&_�e]���X��%F�wS���i���Pڀ,G�9������%����y��g,�l����n���Z�j� G�t�,�rFU8��Q18� ^wO[F~�xt'7�n֫���Yܬw��_�������S�HIf��6IEfˀ��P�#!���k��w���z��!��[��Z.�ȷ��o��,��Hֻ��6�o?�)I�|��29hJ��M��F~2�W5Y�)��A��(9����������H	�����~�{5���B6��$2s.A�)٠BB���s�P_�$C0����zV�ʏ�.�m��jR�
�Bȅ0s�8ÛƝ�8E��R�S�Q$G޺��-s��y=�B�Ɓ|ovzߨ2����P�q�X�T�� @*YR.\�9���
{��t�V<��N/>�{HY�q��h��[�ya�2Q�Rq�tR:�`q��&�l�����|{�0��B}���dZ�X���<�Z��W�\�6�cNF�3�+�8M�o��_�����O��v��f������@�[H9�h��D��(M����lD��dtҫ��RG?%���J��M��PO�w~;���)j�RS"��Etʋ���H�R�BVyH���(?�ԩ�Y�c"xQt�
h�r0+�8���"�a�mxI��R[�MڲpL��kg���;l�r�E��~��!9��@���H(��e��vY�'�,~ ^݃Ơ�Nj1�=���x�_bc.�vT�����K�G�4K^�YJ���p�� ��qa�<�����e��s���l���]��RAI0�8��Z�Б�ئ���N y���W�fHT�	,92��e?��PVy�(�W�����j��S��r�ݍӓ�3�j���V�����<�cI�R�J�ak�P֚�M_�a����ۆ\h�qڗ�[W�`�i�4��OD�KJ<Kr�$B�l�=�����;�'e�~U�V2��$�r9�e���$�U���OT���Z��RHˢ�(��֭:�c��DN���ȿ����{V�_,��D�z�������uu�`W5ne5�u9(���]�v^�W�mZ��w���*������u�(�oW�P�i3w��n�fa�wB5XI֢�\5]�+ )�����u�tx���=(�A�@z��o�m�<�:;�z̓z)��@�T&�I'�-RI-��; ��ھD9��ف�4�Z��E��1aԷ�p��,�&O�+��)ǜ���-!(�6Ga��އ�v�iU1d�z�Y��C��&
��+�,3��"ː(̊V��Ql�s��s4�4)��p�����z.�H��W�Y�,S���
©(�*�9y��9�Q���sGB�`������sB��=E��6Z�0i�c�\$z�%g�'�?�Ľ�&�Y��.~vs��oo�j�xhc%�y��s`8�+#�N�L�Lh�+��P��	����a)���d���HQ?!�^I!�rkA�D��aYh9�>�wr� I��x����Y] f���5�mL��ݓ����a
g&��J�*b��'�Hɺɘd�k�T�u�Dp}ųW�z˂���,�N�c���E�f� +���L��E�cq��a&���f��Ő��.z#�Rc��u ���J����U�p����ȑ��SxTT�|�(���o�"� �X)�i�	-_ ���_)h����.�y�!���l�:��~��~<I��W�l��p�Ec|q��L���UU�����J�� 9�x�R���}�(�_���z��D?�� �S$�!�'������ =��;k�m���8WE�����)�`�<���eH��/v�� �C:���3��b��'�����]�W Ҷ�FRYi���?D�F�)��b�%L���](X�Ё�vJ$G��v�=1f:�{I0����!�<5�4�$LCeL��u1�2Z�`K���g,R��s���\7�S&B٣�\o����~��V_چ�������z�TzAnn��ld�گ���%��u㖊fy/#��r��)�^r��Sh@���7��g��#	٬���v�K�`����#j���R2�X�s֋�$uzѳO/�z¬%}���?l���BwU�FUF)gD�%E��wh1"�&D(���\'��Б���=�S�юn���X�au�u�r=�Fa������P����{�T�:�~pEc�'�������b� (f��	V&�!D��N)I�����خٱ�]N�?���o��)�n�?��L\R�CӴo �����Oڌ��ݚ�@Z�6����5YEd�bAٜ���B���vM�H��.-�?Hٱ+Bm���2f��.{�b!�B������D�LrI8Q�sދ�l
�Ĕt��F�Ƭ�ec�����"���� šP���=EGR��9�Ȓ����='����Ӌ��(9�w?����S)B�U�yoL��R�����R.�,X�=��_�A}׽�����W��U*EXⲭ;���4)R�*�RfJ����<ß���Gg�<��,�*�>���W?�{���ӵk'�-�\�WU���b�$4�H3�d$������)��;'Ƕ����v`o.|ڤU���v�Z�u��o���n�3]<�Ԃ��zl���Ns��OX?�(�v!*A!7�>��{���s�����}��N�?���pգ���p����t�?R�Zg���	�s⎱�^���/��:��To���F,��d���z�t �M�������>xr�Խ\����~�jvV�8�+�����t~���Փ~����ws�����0�MU6]Ȯ8l�P��sO. -4�������g�\��3X�NvA��ڸ��mQ���`}��BhRAJc3gD5+1�:��`��I�!�c�}�#'�
:�����u����-�ҽ���	���n&���~aN��k����QD	�p�ui����$.B�ߐoW��uD��o�7o����Y      '      x�3�L��"�=... U�      ;   M   x�3�4�4426153���4�4202�50"���,.I�����Y*ZX�ZZ����"�+F��� 
��      .   �   x�m�]�0�wO�4��{Rʆl(�YZbo/�&y��|;
ROR��$P`�=��`���I�X��c�O3죉��Vl�����e%
�\�-�x�~��Dj��qȥW���5�@6��e�DB˾Y�;S9Buܺ�R�c*5��-�����b?&?���lm�      0      x������ � �      ,   +   x���q�w��t��4�
������<]\�8-�b���� �v	�      G      x������ � �      P   �   x���;N1��x�.��~��C�LB�j��H�.��G�G �_�T%�4�E�(�nLq-�b�,m�p���TX��ȣY]��c�ڎLba�UҘN�.��+Z���|��~�3�����5� u��c\�����\�Y��IQu�����/9�9���uޏ�������ɢ>	      Q   ^   x���Q
�  ��ye��]�H��E��#� �gLZ�%�,ًU�)��+5#I�Y��k�X"4g�,Ơ��>��`g7�������{ ��:2      R   R   x����� �s腌(?�^rQ�/!%����]���0.U;�/���.j+&��-Rd�K{�zlg�]˔�ꙝ�ڡ�w�}p�      O   K   x�K124M2M��502��5II6յ�L4�55LK14�HNK21�,J-.I,-J�+�7�,�J!��Y�gW� n$     