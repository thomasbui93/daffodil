-- events schema

-- !Ups

CREATE TABLE events (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    description varchar(255),
    eventTypeId BIGINT NOT NUll,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deletedAt TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT `fk_event_type`
        FOREIGN KEY (eventTypeId)
        REFERENCES events(id)
);

-- !Downs

DROP TABLE events;
