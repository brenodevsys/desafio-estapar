create table sectors
(
    id           bigserial primary key,
    code         varchar(20)    not null,
    base_price   numeric(12, 2) not null,
    max_capacity int            not null
);

create table spots
(
    id            bigserial primary key,
    sector_id     bigint           not null references sectors (id),
    lat           double precision not null,
    lng           double precision not null,
    status        varchar(20)      not null,
    current_plate varchar(16)
);

create table parking_sessions
(
    id                   bigserial primary key,
    plate                varchar(16)              not null,
    sector_id            bigint references sectors (id),
    spot_id              bigint references spots (id),
    entry_time           timestamp with time zone not null,
    parked_time          timestamp with time zone,
    exit_time            timestamp with time zone,
    price_factor         numeric(6, 3)            not null,
    effective_price_hour numeric(12, 2),
    charged_amount       numeric(12, 2),
    status               varchar(20)              not null
);

create index idx_sessions_open_by_plate on parking_sessions (plate, status);
create index idx_sessions_exit_time on parking_sessions (exit_time);
