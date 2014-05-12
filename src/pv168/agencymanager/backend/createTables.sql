DROP TABLE agent;
DROP TABLE mission;

CREATE TABLE mission (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    codename VARCHAR(255) NOT NULL UNIQUE,
    datecreated DATE NOT NULL,
    inprogress BOOLEAN,
    maxnumberofagents INTEGER NOT NULL,
    notes VARCHAR(255));

CREATE TABLE agent (
    id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    missionId BIGINT REFERENCES mission (id),
    number INT UNIQUE,
    name VARCHAR(20),
    isdead BOOLEAN,
    enrollment DATE);
                
INSERT INTO mission (codename, datecreated, inprogress, maxnumberofagents, notes)
    VALUES ('Smatlaci v akcii', '2006-09-10-00.00.00', true, 4, 'aj tak je to uplne zbytocne *facepalm*');
INSERT INTO mission (codename, datecreated, inprogress, maxnumberofagents, notes)
    VALUES ('Okradanie malych deti', '2001-04-01-00.00.00', true, 3, 'chudak deti *facepalm*');
    
INSERT INTO agent (missionId, number, name, isdead, enrollment) 
    VALUES (1, 777, 'Jozin z Bazin', false, '2009-01-02-00.00.00');
INSERT INTO agent (missionId, number, name, isdead, enrollment) 
    VALUES (1, 123, 'Ferko Trefulka', true, '2000-08-02-00.00.00');
INSERT INTO agent (missionId, number, name, isdead, enrollment) 
    VALUES (1, 999, 'Istvan Istenem', false, '1999-11-20-00.00.00');
INSERT INTO agent (missionId, number, name, isdead, enrollment) 
    VALUES (2, 865, 'Noname Annonymous', true, '1950-01-01-00.00.00');
INSERT INTO agent (missionId, number, name, isdead, enrollment) 
    VALUES (2, 1, 'Erzebet Fekete', false, '2005-06-22-00.00.00');