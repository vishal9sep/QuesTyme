
INSERT INTO Availability (id, avail_day, start_time, end_time)
VALUES
(10, 'Monday', '08:00:00', '12:00:00'),
(11, 'Monday', '09:00:00', '12:00:00');



INSERT INTO interviews (interview_id, interviewer_id, interviewee_id, start_time, end_time, interview_date, students_notes, admin_feedback, category, instructions, title, meeting_link, meeting_status, batch, reminder_sent)
VALUES (20, 1, 2, '10:00:00', '11:00:00', '2023-04-15', 'Student did well', 'Good job', 'Technical', 'Be on time', 'Java Developer', 'https://meet.google.com/abc-xyz', 'P', 'Batch 01', false);



INSERT INTO recurring_meeting (recurring_id, admin_id, duration, instruction, meeting_link, title)
VALUES (40,1, 60, 'Please come prepared with your CV and a list of questions.', 'https://meet.google.com/abc123', 'Job Interview');


INSERT INTO role (`id`, `name`)
VALUES
    (1, 'Admin'),
    (2, 'Manager'),
    (3, 'Employee');


INSERT INTO slot (slot_id, title, instruction, admin_id, meeting_link, slot_date, start_time, end_time, type, status, user_id)
VALUES (1, 'Interview with John', 'Please bring your CV and references', 1234, 'https://meet.google.com/abc123', '2023-04-12', '10:00:00', '11:00:00', 'Interview', 'A', 101);



--INSERT INTO users (id, user_name, email,  password, type)
--VALUES (101, 'John Doe', 'testuser1@test.com',  'password123', 'JAVA'),
--       (102, 'Jane Doe', 'jane.doe@example.com',  'password456', 'JAVA'),
--       (103, 'Bob Smith', 'bob.smith@example.com', 'password789', 'DSA');

INSERT INTO users (id, user_name, email,  password)
VALUES (101, 'John Doe', 'testuser1@test.com',  'password123'),
       (102, 'Jane Doe', 'jane.doe@example.com',  'password456'),
       (103, 'Bob Smith', 'bob.smith@example.com', 'password789');



INSERT INTO hibernate_sequence (next_val) VALUES (1);

