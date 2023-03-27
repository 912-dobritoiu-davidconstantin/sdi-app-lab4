--
-- PostgreSQL database dump
--

-- Dumped from database version 14.4
-- Dumped by pg_dump version 14.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: library; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE library WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';


ALTER DATABASE library OWNER TO postgres;

\connect library

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    author_id bigint NOT NULL,
    bio character varying(255),
    country character varying(255),
    email character varying(255),
    name character varying(255)
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_author_id_seq OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_author_id_seq OWNED BY public.author.author_id;


--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    book_id bigint NOT NULL,
    price double precision NOT NULL,
    rating integer NOT NULL,
    title character varying(255),
    year integer NOT NULL,
    author_id bigint NOT NULL
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_book_id_seq OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_book_id_seq OWNED BY public.book.book_id;


--
-- Name: library; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.library (
    library_id bigint NOT NULL,
    description character varying(255),
    location character varying(255),
    name character varying(255),
    owner character varying(255),
    rating integer NOT NULL
);


ALTER TABLE public.library OWNER TO postgres;

--
-- Name: library_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.library_book (
    library_book_id bigint NOT NULL,
    borrow_date timestamp(6) without time zone NOT NULL,
    return_date timestamp(6) without time zone,
    book_id bigint NOT NULL,
    library_id bigint NOT NULL
);


ALTER TABLE public.library_book OWNER TO postgres;

--
-- Name: library_book_library_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.library_book_library_book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.library_book_library_book_id_seq OWNER TO postgres;

--
-- Name: library_book_library_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.library_book_library_book_id_seq OWNED BY public.library_book.library_book_id;


--
-- Name: library_library_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.library_library_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.library_library_id_seq OWNER TO postgres;

--
-- Name: library_library_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.library_library_id_seq OWNED BY public.library.library_id;


--
-- Name: author author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN author_id SET DEFAULT nextval('public.author_author_id_seq'::regclass);


--
-- Name: book book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN book_id SET DEFAULT nextval('public.book_book_id_seq'::regclass);


--
-- Name: library library_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library ALTER COLUMN library_id SET DEFAULT nextval('public.library_library_id_seq'::regclass);


--
-- Name: library_book library_book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library_book ALTER COLUMN library_book_id SET DEFAULT nextval('public.library_book_library_book_id_seq'::regclass);


--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (author_id, bio, country, email, name) FROM stdin;
1	Harper Lee was an American novelist widely known for her novel 'To Kill a Mockingbird', which won the Pulitzer Prize in 1961.	United States	harperlee@example.com	Harper Lee
3	F. Scott Fitzgerald was an American novelist and short story writer, whose works are the paradigmatic writings of the Jazz Age. He is best known for his novel 'The Great Gatsby'.	United States	fscottfitzgerald@example.com	F. Scott Fitzgerald
4	J.D. Salinger was an American novelist, best known for his novel 'The Catcher in the Rye', a controversial book that has become an icon of teenage rebellion.	United States	jdsalinger@example.com	J.D. Salinger
5	Aldous Huxley was an English writer and philosopher, best known for his novels 'Brave New World' and 'Island'.	United Kingdom	aldoushuxley@example.com	Aldous Huxley
6	William Golding was a British novelist and playwright, best known for his novel 'Lord of the Flies', which is considered a classic of modern literature.	United Kingdom	williamgolding@example.com	William Golding
7	Jane Austen was an English novelist known primarily for her six major novels, which interpret, critique and comment upon the British landed gentry at the end of the 18th century.	United Kingdom	janeausten@example.com	Jane Austen
8	Oscar Wilde was an Irish poet and playwright, best known for his novel 'The Picture of Dorian Gray'.	Ireland	oscarwilde@example.com	Oscar Wilde
9	Gabriel Garcia Marquez was a Colombian novelist, short-story writer, screenwriter and journalist, known for his novels 'One Hundred Years of Solitude' and 'Love in the Time of Cholera'. He was awarded the Nobel Prize in Literature in 1982.	Colombia	gabrielgarciamarquez@example.com	Gabriel Garcia Marquez
2	George Orwell was an English novelist, essayist, journalist and critic. He is best known for his novels 'Nineteen Eighty-Four' and 'Animal Farm'.	United Kingdom	georgeorwell@example.com	George Orwell
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (book_id, price, rating, title, year, author_id) FROM stdin;
1	8.99	4	To Kill a Mockingbird	1960	1
2	9.99	4	1984	1949	2
3	12.99	3	The Great Gatsby	1925	3
4	7.99	3	The Catcher in the Rye	1951	4
5	6.99	3	Animal Farm	1945	2
6	11.99	3	Brave New World	1932	5
7	9.99	3	Lord of the Flies	1954	6
8	5.99	4	Pride and Prejudice	1813	7
9	8.99	4	The Picture of Dorian Gray	1890	8
10	10.99	4	One Hundred Years of Solitude	1967	9
\.


--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.library (library_id, description, location, name, owner, rating) FROM stdin;
1	The New York Public Library is a public library system in New York City. With nearly 53 million items and 92 locations, the New York Public Library is the second largest public library in the United States, and the fourth largest in the world.	New York City, NY	New York Public Library	New York City Department of Education	4
2	The Library of Congress is the research library that officially serves the United States Congress and is the de facto national library of the United States. It is the oldest federal cultural institution in the United States.	Washington, D.C.	Library of Congress	United States Congress	4
3	The British Library is the national library of the United Kingdom and is one of the largest libraries in the world. The library's collections include around 170 million items from many countries and in many languages.	London, England	British Library	Department for Digital, Culture, Media and Sport	4
4	Library and Archives Canada is the federal institution responsible for acquiring, preserving, and making Canada's documentary heritage accessible. It is the fourth largest library in the world, and the largest in North America.	Ottawa, Canada	Library and Archives Canada	Government of Canada	4
5	The Bibliothèque nationale de France is the national library of France and is located in Paris. It is the repository of all that is published in France and also holds extensive historical collections.	Paris, France	Bibliothèque nationale de France	French government	4
6	The State Library Victoria is the central library of the state of Victoria, Australia, located in Melbourne. It is the oldest public library in Australia and one of the first free libraries in the world.	Melbourne, Australia	State Library Victoria	State Government of Victoria	4
7	The National Library of Australia is the largest reference library in Australia, responsible under the terms of the National Library Act.	Canberra, Australia	National Library of Australia	Government of Australia	4
8	The Tokyo Metropolitan Library is a library in Tokyo, Japan. It is one of the largest libraries in Japan and has a collection of about 3.8 million books.	Tokyo, Japan	Tokyo Metropolitan Library	Tokyo Metropolitan Government	4
9	The Biblioteca Nacional de España is the national library of Spain, located in Madrid. It is one of the largest libraries in Spain and holds more than 26 million items.	Madrid, Spain	Biblioteca Nacional de España	Spanish Ministry of Culture and Sport	4
10	The National Library of China is the largest library in Asia and one of the largest in the world. It is located in Beijing and has a collection of over 37 million items.	Beijing, China	National Library of China	Ministry of Culture and Tourism of the People's Republic of China	4
\.


--
-- Data for Name: library_book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.library_book (library_book_id, borrow_date, return_date, book_id, library_id) FROM stdin;
5	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	1	2
6	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	2	2
7	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	4	6
8	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	3	8
9	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	7	8
10	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	7	7
11	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	7	10
12	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	4	3
13	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	5	3
14	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	8	3
15	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	9	3
16	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	10	2
17	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	10	4
18	2022-03-25 20:56:22.465	2022-03-25 20:56:22.465	10	6
4	2022-03-25 20:56:22.465	2022-03-26 20:56:22.465	1	1
\.


--
-- Name: author_author_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_author_id_seq', 10, true);


--
-- Name: book_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_book_id_seq', 10, true);


--
-- Name: library_book_library_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.library_book_library_book_id_seq', 18, true);


--
-- Name: library_library_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.library_library_id_seq', 10, true);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (book_id);


--
-- Name: library_book library_book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library_book
    ADD CONSTRAINT library_book_pkey PRIMARY KEY (library_book_id);


--
-- Name: library library_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library
    ADD CONSTRAINT library_pkey PRIMARY KEY (library_id);


--
-- Name: library_book fkerhvjx4mc7sf21kx85tjcnjlx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library_book
    ADD CONSTRAINT fkerhvjx4mc7sf21kx85tjcnjlx FOREIGN KEY (book_id) REFERENCES public.book(book_id);


--
-- Name: book fkklnrv3weler2ftkweewlky958; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fkklnrv3weler2ftkweewlky958 FOREIGN KEY (author_id) REFERENCES public.author(author_id);


--
-- Name: library_book fklohkf9ywj855nc5nsd0mxss2f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.library_book
    ADD CONSTRAINT fklohkf9ywj855nc5nsd0mxss2f FOREIGN KEY (library_id) REFERENCES public.library(library_id);


--
-- PostgreSQL database dump complete
--

