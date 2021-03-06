CREATE TABLE IF NOT EXISTS subjects
(
    subjectId   SERIAL PRIMARY KEY,
    code        varchar(50),
    subjectName varchar(50)
);

CREATE TABLE IF NOT EXISTS courses
(
    courseId  SERIAL PRIMARY KEY,
    subjectId INTEGER,
    quarter   INTEGER,
    board     varchar(50),
    year      INTEGER,
    UNIQUE (subjectId, quarter, board, year),
    FOREIGN KEY (subjectId) REFERENCES subjects ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    userId     SERIAL PRIMARY KEY,
    fileNumber INTEGER UNIQUE,
    name       TEXT,
    surname    TEXT,
    username   TEXT NOT NULL UNIQUE,
    email      TEXT NOT NULL UNIQUE,
    password   TEXT,
    isAdmin    BOOLEAN
);

CREATE TABLE IF NOT EXISTS roles
(
    roleId   SERIAL NOT NULL PRIMARY KEY,
    roleName VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS announcements
(
    announcementId SERIAL PRIMARY KEY,
    userId         INTEGER,
    courseId       INTEGER,
    title          varchar(50),
    content        TEXT,
    date           TIMESTAMP,
    FOREIGN KEY (userId) references users ON DELETE CASCADE,
    FOREIGN KEY (courseId) references courses ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS timetables
(
    courseId  INTEGER,
    dayOfWeek INTEGER,
    startTime TIME,
    endTime   TIME,
    FOREIGN KEY (courseId) REFERENCES courses ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_to_course
(
    courseId INTEGER NOT NULL,
    userId   INTEGER NOT NULL,
    roleId   INTEGER NOT NULL,
    UNIQUE(courseId, userId),
    FOREIGN KEY (userId) REFERENCES users ON DELETE CASCADE,
    FOREIGN KEY (courseId) references courses ON DELETE CASCADE,
    FOREIGN KEY (roleId) references roles ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS file_extensions
(
    fileExtensionId SERIAL PRIMARY KEY,
    fileExtension   varchar(5),
    UNIQUE (fileExtension)
);

CREATE TABLE IF NOT EXISTS files
(
    fileId          SERIAL PRIMARY KEY,
    fileSize        INTEGER,
    fileName        varchar(50),
    fileDate        TIMESTAMP,
    file            BYTEA,
    fileExtensionId INTEGER,
    courseId        INTEGER,
    downloads       INTEGER,
    FOREIGN KEY (fileExtensionId) references file_extensions ON DELETE CASCADE,
    FOREIGN KEY (courseId) references courses ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS file_categories
(
    categoryId   SERIAL PRIMARY KEY,
    categoryName varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS category_file_relationship
(
    categoryId INTEGER,
    fileId     INTEGER,
    FOREIGN KEY (categoryId) references file_categories ON DELETE SET NULL,
    FOREIGN KEY (fileId) references files ON DELETE CASCADE,
    UNIQUE (categoryId, fileId)
);

CREATE TABLE IF NOT EXISTS profile_images
(
    image  BYTEA,
    userId INTEGER PRIMARY KEY,
    FOREIGN KEY (userId) REFERENCES users ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS exams
(
    examId SERIAL PRIMARY KEY ,
    courseId INTEGER,
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    title text,
    description text,
    file_id INTEGER,
    FOREIGN KEY (courseId) REFERENCES courses,
    FOREIGN KEY (file_id) REFERENCES files
)
