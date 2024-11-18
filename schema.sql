create database CustomerSupport default character set 'utf8'
	default collate 'utf8_unicode_ci';

create table UserPrincipal (
	UserId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(30) NOT NULL,
    HashedPassword BINARY(60) NOT NULL,
    Admin BOOL DEFAULT FALSE,
    UNIQUE KEY UserPrincipal_Username (Username)
) ENGINE = InnoDB;

CREATE TABLE Ticket (
	TicketId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    UserId BIGINT UNSIGNED NOT NULL,
    Subject VARCHAR(255) NOT NULL,
    Body TEXT,
    DateCreated DATETIME NOT NULL,
    CONSTRAINT Ticket_UserId FOREIGN KEY (UserId)
		REFERENCES UserPrincipal (UserId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE Attachment (
	AttachmentId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TicketId BIGINT UNSIGNED NOT NULL,
    AttachmentName VARCHAR(255) NULL,
    MimeContentType VARCHAR(255) NOT NULL,
    Contents BLOB NOT NULL,
    CONSTRAINT Attachment_TicketId FOREIGN KEY (TicketId)
		REFERENCES Ticket (TicketId) ON DELETE CASCADE
) ENGINE = InnoDB;