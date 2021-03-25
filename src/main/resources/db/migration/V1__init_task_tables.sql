CREATE TABLE IF NOT EXISTS customers(

    id integer primary key serial not null,
    name varchar(255),
    surname varchar(255)
);

CREATE TABLE IF NOT EXISTS animals(
    id integer primary key serial not null,
    date_of_birth date,
    name varchar (255),
    species varchar(255),
    owner_id integer not null,
    CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES customers(id)
);
CREATE TABLE IF NOT EXISTS vets(
    id integer primary key serial not null ,
    availability varchar(255),
    image oid, -- ?
    name varchar (255),
    surname varchar (255)
);
CREATE TABLE IF NOT EXISTS visits(
    id integer primary key serial not null,
    descriptions varchar(255),
    price  numeric(19, 2) not null,
    start_time timestamp not null,
    status varchar(255) not null,
    animal_id integer not null,
    customer_id integer not null,
    vet_id integer not null,
    CONSTRAINT fk_owner_visit FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_animal_visit FOREIGN KEY (animal_id) REFERENCES animals(id)
)