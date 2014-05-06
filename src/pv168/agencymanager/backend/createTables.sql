CREATE TABLE mission (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    codename VARCHAR(255),
    datecreated DATE NOT NULL,
    inprogress BOOLEAN,
    maxnumberofagents INTEGER NOT NULL,
    notes VARCHAR(255));

CREATE TABLE agent (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    missionId BIGINT REFERENCES mission (id),
    number INT,
    name VARCHAR(20),
    isdead BOOLEAN,
    enrollment DATE);
                    

