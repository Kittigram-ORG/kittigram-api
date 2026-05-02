ALTER TABLE users.users
    ADD COLUMN legal_hold_until   TIMESTAMP,
    ADD COLUMN deleted_at         TIMESTAMP,
    ADD COLUMN scheduled_purge_at TIMESTAMP;
