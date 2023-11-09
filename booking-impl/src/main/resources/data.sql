INSERT INTO CONFERENCE_ROOM(id, capacity, name)
VALUES (1, 3, 'Amaze');
INSERT INTO CONFERENCE_ROOM(id, capacity, name)
VALUES (2, 7, 'Beauty');
INSERT INTO CONFERENCE_ROOM(id, capacity, name)
VALUES (3, 12, 'Inspire');
INSERT INTO CONFERENCE_ROOM(id, capacity, name)
VALUES (4, 20, 'Strive');
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (1, 1, 15, '2023-11-09T09:00', '2023-11-09T09:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (2, 1, 15, '2023-11-09T13:00', '2023-11-09T13:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (3, 1, 15, '2023-11-08T17:00', '2023-11-08T17:15', true);

INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (4, 2, 15, '2023-11-09T09:00', '2023-11-09T09:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (5, 2, 15, '2023-11-09T13:00', '2023-11-09T13:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (6, 2, 15, '2023-11-08T17:00', '2023-11-08T17:15', true);

INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (7, 3, 15, '2023-11-09T09:00', '2023-11-09T09:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (8, 3, 15, '2023-11-09T13:00', '2023-11-09T13:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (9, 3, 15, '2023-11-08T17:00', '2023-11-08T17:15', true);

INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (10, 4, 15, '2023-11-09T09:00', '2023-11-09T09:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (11, 4, 15, '2023-11-09T13:00', '2023-11-09T13:15', true);
INSERT INTO BOOKING_SLOT(id, room_id, duration, start_time, end_time, maintenance_slot)
VALUES (12, 4, 15, '2023-11-08T17:00', '2023-11-08T17:15', true);