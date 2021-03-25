-- DROP TABLE customers IF EXISTS cascade;
-- DROP TABLE animals IF EXISTS cascade;
-- DROP TABLE vets IF EXISTS cascade;
-- DROP TABLE visits IF EXISTS cascade;
-- DROP TABLE flyway_schema_history IF EXISTS cascade;

CREATE TABLE IF NOT EXISTS customers
(
    id      serial primary key not null,
    name    varchar(255)       not null,
    surname varchar(255)       not null
);

CREATE TABLE IF NOT EXISTS animals
(
    id            serial primary key not null,
    date_of_birth date,
    name          varchar(255)       not null,
    species       varchar(255)       not null,
    owner_id      integer            not null,
    FOREIGN KEY (owner_id) REFERENCES customers (id)
);
CREATE TABLE IF NOT EXISTS vets
(
    id           serial primary key not null,
    availability varchar(255)       not null,
    image        oid,
    name         varchar(255)       not null,
    surname      varchar(255)       not null
);
CREATE TABLE IF NOT EXISTS visits
(
    id          serial primary key not null,
    description varchar(255),
    price       numeric(19, 2)     not null,
    start_time  timestamp          not null,
    status      varchar(255)       not null,
    animal_id   integer            not null,
    customer_id integer            not null,
    vet_id      integer            not null,
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (animal_id) REFERENCES animals (id),
    FOREIGN KEY (vet_id) REFERENCES vets (id)
)