
--DROP TABLE IF EXISTS `availability`;

CREATE TABLE Availability (
    id INT NOT NULL,
    avail_day VARCHAR(255) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
--    PRIMARY KEY (id)
);


CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
);


 CREATE TABLE `interviews` (
   `interview_id` INT,
   `interviewer_id` INT ,
   `interviewee_id` INT DEFAULT NULL,
    `start_time` time DEFAULT NULL,
    `end_time` time DEFAULT NULL,
    `interview_date` date DEFAULT NULL,
    `students_notes` varchar(255) DEFAULT NULL,
    `admin_feedback` varchar(255) DEFAULT NULL,
   `category` varchar(255) DEFAULT NULL,
   `instructions` varchar(255) DEFAULT NULL,
   `title` varchar(255) DEFAULT NULL,
   `meeting_link` varchar(255) DEFAULT NULL,
   `meeting_status` varchar(255) DEFAULT NULL,
   `batch` varchar(255) DEFAULT NULL,
   `reminder_sent` BOOLEAN NOT NULL
  );


CREATE TABLE recurring_meeting (
  recurring_id INT NOT NULL,
  admin_id INT NOT NULL,
  duration INT NOT NULL,
  instruction VARCHAR(255),
  meeting_link VARCHAR(255),
  title VARCHAR(255)
--  category VARCHAR(255)
);

CREATE TABLE `role` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL
--  PRIMARY KEY (`id`)
);



CREATE TABLE slot (
  slot_id INT ,
  title VARCHAR(255),
  instruction VARCHAR(255),
  admin_id INT,
  meeting_link VARCHAR(255),
  slot_date DATE,
  start_time TIME,
  end_time TIME,
  type VARCHAR(255),
  status CHAR(1),
  user_id INT
);


CREATE TABLE `users` (
  `id` int NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
);


CREATE TABLE user_role (
  user_id INT NOT NULL,
  role_id INT NOT NULL
);











