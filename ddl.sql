CREATE TABLE shoes (
                       id varchar(40) NOT NULL,
                       brand varchar(256),
                       categories varchar(1023),
                       colors varchar(255),
                       image_url varchar(2047),
                       name varchar(511),
                       price float(4),
                       quantity integer NOT NULL,
                       lati varchar(63),
                       longti varchar(63),
                       PRIMARY KEY(id)
);
