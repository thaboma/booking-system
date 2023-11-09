CREATE SCHEMA IF NOT EXISTS booking;

CREATE TABLE IF NOT EXISTS booking.conference_room
(
    id
    bigint
    NOT
    NULL,
    capacity
    int,
    name
    character
    varying
(
    50
) ,
    CONSTRAINT room_pkey PRIMARY KEY
(
    id
)
    );


CREATE TABLE IF NOT EXISTS booking.booking_slot
(
    id
    bigint
    NOT
    NULL,
    room_id
    bigint
    NOT
    NULL,
    duration
    int,
    created_time
    timestamp
    without
    time
    zone,
    start_time
    timestamp
    without
    time
    zone,
    end_time
    timestamp
    without
    time
    zone,
    maintenance_slot
    bit,
    CONSTRAINT
    booking_slot_pkey
    PRIMARY
    KEY
(
    id
)
    );


SET SCHEMA booking;