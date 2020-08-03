-- event_types schema

-- !Ups

CREATE TABLE event_types (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP,
    PRIMARY KEY (id)
);

-- !Downs

DROP TABLE event_types;
