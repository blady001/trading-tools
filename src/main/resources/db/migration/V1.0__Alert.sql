CREATE TABLE alert (
    id IDENTITY PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    exchange VARCHAR(50) NOT NULL,
    exchange_time_offset TIME WITH TIME ZONE NOT NULL,
    timeframe VARCHAR(3) NOT NULL,
    trigger VARCHAR(50) NOT NULL,
    notification_method INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);