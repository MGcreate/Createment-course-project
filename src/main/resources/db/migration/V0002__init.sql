CREATE SEQUENCE HIBERNATE_SEQUENCE;

CREATE TABLE public.channel
(
    oid integer NOT NULL PRIMARY KEY,
    name character varying(255) NOT NULL UNIQUE
);

CREATE TABLE public.messenger
(
    oid integer NOT NULL PRIMARY KEY,
    username character varying(255) UNIQUE,
    password character varying(255)
);

CREATE TABLE IF NOT EXISTS public.message
(
    oid integer NOT NULL PRIMARY KEY,
    content character varying(1000) NOT NULL,
    sent_at timestamp without time zone NOT NULL,
    channel_oid integer NOT NULL references channel(oid),
    user_oid integer NOT NULL references messenger(oid)
);
