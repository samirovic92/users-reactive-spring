create table users (
    id UUID DEFAULT random_uuid() PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email  varchar(100) UNIQUE ,
    password varchar(255) NOT NULL
);