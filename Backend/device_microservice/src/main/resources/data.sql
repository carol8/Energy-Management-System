DELETE FROM users WHERE uuid = '8b4ec6a9-0b7f-4ee0-a57c-7739ae649bff';
DELETE FROM users WHERE uuid = 'ab7c50af-e146-4052-a736-2ba4dff9bd37';

DELETE FROM devices WHERE user_uuid = '8b4ec6a9-0b7f-4ee0-a57c-7739ae649bff';
DELETE FROM devices WHERE user_uuid = 'ab7c50af-e146-4052-a736-2ba4dff9bd37';

INSERT INTO users(uuid) VALUES
    ('8b4ec6a9-0b7f-4ee0-a57c-7739ae649bff' ),
    ('ab7c50af-e146-4052-a736-2ba4dff9bd37');

INSERT INTO devices(uuid, user_uuid, description, address, max_wh) VALUES
    ('733cf39c-cc83-4183-8d0d-1a69929cb344', '8b4ec6a9-0b7f-4ee0-a57c-7739ae649bff', 'A device that does stuff', 'Str. Somewhere, Nr. 69', 150),
    ('f7a7bb46-611f-4fdc-9688-7695ffad65c3', '8b4ec6a9-0b7f-4ee0-a57c-7739ae649bff', 'Another device that does stuff', 'Str. Somewhere, Nr. 169', 121),
    ('3d7e4ae2-caee-4143-a881-e9dbc0cb7ff7', 'ab7c50af-e146-4052-a736-2ba4dff9bd37', 'A third device that does stuff', 'Str. Somewhere, Nr. 269', 145),
    ('2a60da93-902a-4f44-842d-496e2f11704d', 'ab7c50af-e146-4052-a736-2ba4dff9bd37', 'Yet another device that does stuff', 'Str. Somewhere, Nr. 369', 10);