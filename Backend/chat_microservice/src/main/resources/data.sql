DELETE FROM messages WHERE uuid = 'b7ce4cc8-bd32-40fa-b231-1dad3ea4c08d';
DELETE FROM messages WHERE uuid = 'e9edda03-dedf-48f9-8e0a-ebb8dd022595';
DELETE FROM messages WHERE uuid = '8ff5ed2f-0926-4125-a76d-8c80ce409b88';
DELETE FROM messages WHERE uuid = '40d551e7-3cf1-46ea-801b-b355decf1e75';
DELETE FROM messages WHERE uuid = '213cbdfa-5334-4f9c-a151-3f8eadcd3d3b';
DELETE FROM messages WHERE uuid = '7ac29c00-18cf-40aa-a2cd-fcab4940e443';

INSERT INTO messages (uuid, sender, receiver, timestamp, content) VALUES
    ('b7ce4cc8-bd32-40fa-b231-1dad3ea4c08d', 'adminBroadcast', 'userBroadcast', '2024-01-17 12:03:43', 'Test broadcast')
    ,('e9edda03-dedf-48f9-8e0a-ebb8dd022595', 'userBroadcast', 'adminBroadcast', '2024-01-18 02:04:45', 'Second test broadcast')
    ,('8ff5ed2f-0926-4125-a76d-8c80ce409b88', 'carol8', 'maria', '2024-01-17 12:04:45', 'Ping')
    ,('40d551e7-3cf1-46ea-801b-b355decf1e75', 'maria', 'carol8', '2024-01-18 02:04:45', 'Pong')
    ,('213cbdfa-5334-4f9c-a151-3f8eadcd3d3b', 'carol8', 'carol8', '2024-01-18 02:04:45', 'Hmmmm')
    ,('7ac29c00-18cf-40aa-a2cd-fcab4940e443', 'maria', 'maria', '2024-01-18 02:04:45', 'Mda')
;