-- DROP TABLE customers cascade;
-- DROP TABLE animals cascade;
-- DROP TABLE vets cascade;
-- DROP TABLE visits cascade;
-- DROP TABLE surgeries cascade;
-- DROP TABLE flyway_schema_history cascade;

create TABLE IF NOT EXISTS USERS
(
    id            serial primary key not null,
    password      varchar(255)       not null,
    role          varchar(255)       not null,
    account_state varchar(255)       not null,
    username      varchar(255)       not null unique
);

create TABLE IF NOT EXISTS customers
(
    id      serial primary key not null,
    name    varchar(255)       not null,
    surname varchar(255)       not null,
    FOREIGN KEY (id) references USERS (id)
);

create TABLE IF NOT EXISTS animals
(
    id            serial primary key not null,
    date_of_birth date,
    name          varchar(255)       not null,
    species       varchar(255)       not null,
    owner_id      integer            not null,
    FOREIGN KEY (owner_id) REFERENCES customers (id)
);
create TABLE IF NOT EXISTS vets
(
    id                serial primary key not null,
    image             oid,
    name              varchar(255)       not null,
    surname           varchar(255)       not null,
    availability_from time,
    availability_to   time,
    FOREIGN KEY (id) references USERS (id)
);
create TABLE IF NOT EXISTS surgeries
(
    id   serial primary key not null,
    name varchar(255)       not null
);
create TABLE IF NOT EXISTS visits
(
    id          serial primary key not null,
    description varchar(255),
    price       numeric(19, 2)     not null,
    start_time  timestamp          not null,
    status      varchar(255)       not null,
    animal_id   integer            not null,
    customer_id integer            not null,
    vet_id      integer            not null,
    surgery_id  integer            not null,
    FOREIGN KEY (surgery_id) REFERENCES surgeries (id),
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (animal_id) REFERENCES animals (id),
    FOREIGN KEY (vet_id) REFERENCES vets (id)
)