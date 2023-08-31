CREATE TABLE courses (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255),
  teacher_id BIGINT
);

CREATE TABLE teachers (
  id SERIAL PRIMARY KEY,
  fullname VARCHAR(255),
  faculty VARCHAR(255)
);

CREATE TABLE students (
  id SERIAL PRIMARY KEY,
  fullname VARCHAR(255)
);

CREATE TABLE course_student (
  course_id INT,
  student_id INT,
  FOREIGN KEY (course_id) REFERENCES courses (id),
  FOREIGN KEY (student_id) REFERENCES students (id)
);

INSERT INTO students (fullname) VALUES ('Ivan Ivanov'), ('Fedor Petrov'), ('Alexey Popov');

INSERT INTO teachers (fullname, faculty) VALUES ('Pavel Pavlov', 'Technical'), ('Dmitriy Sergeev', 'Business');

INSERT INTO courses (title, teacher_id) VALUES ('Programming', 1), ('Mathematics', 1), ('Marketing', 2);

INSERT INTO course_student (student_id, course_id) VALUES (1, 1), (1, 2), (2, 3), (3, 1), (3, 2), (3, 3);