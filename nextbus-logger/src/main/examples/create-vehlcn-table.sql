-- SQL DDL for NextBus Vehicle Location Recorder (Apache Derby  or DB2)
-- Note is has no Primary Key, or Indices, as it is intended for rapid insert
DROP TABLE NEXTBUS.VEHLCN;
CREATE TABLE NEXTBUS.VEHLCN (
        AGENCY  CHAR(8) NOT NULL,
        ROUTE   CHAR(12) NOT NULL,
        VEHICLE CHAR(12) NOT NULL,
        LONGITUDE DOUBLE,
        LATITUDE DOUBLE,
        HEADING DOUBLE,
        SPEEDMPH DOUBLE,
        LASTREPORT BIGINT,
        TS BIGINT,
        TSKEW BIGINT,
        CREATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );