-- Campus Connect Database Schema
-- PostgreSQL Database Design

-- Drop existing tables if they exist (for development)
DROP TABLE IF EXISTS event_registrations CASCADE;
DROP TABLE IF EXISTS project_collaborators CASCADE;
DROP TABLE IF EXISTS project_likes CASCADE;
DROP TABLE IF EXISTS project_comments CASCADE;
DROP TABLE IF EXISTS user_badges CASCADE;
DROP TABLE IF EXISTS announcements CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Users table (both students and admins)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('STUDENT', 'ADMIN')),
    department VARCHAR(100),
    academic_year VARCHAR(50),
    bio TEXT,
    avatar_url VARCHAR(500),
    github_username VARCHAR(100),
    linkedin_username VARCHAR(100),
    website_url VARCHAR(500),
    location VARCHAR(200),
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Events table
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    long_description TEXT,
    category VARCHAR(50) NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    max_attendees INTEGER NOT NULL DEFAULT 100,
    current_attendees INTEGER DEFAULT 0,
    organizer_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    requirements TEXT,
    tags TEXT[], -- PostgreSQL array for tags
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    registration_deadline TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event registrations (many-to-many relationship)
CREATE TABLE event_registrations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attendance_status VARCHAR(20) DEFAULT 'REGISTERED' CHECK (attendance_status IN ('REGISTERED', 'ATTENDED', 'NO_SHOW', 'CANCELLED')),
    UNIQUE(user_id, event_id)
);

-- Projects table
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL CHECK (difficulty_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    expected_duration VARCHAR(50),
    team_size VARCHAR(50),
    required_skills TEXT[], -- PostgreSQL array for skills
    requirements TEXT,
    status VARCHAR(30) DEFAULT 'SEEKING_COLLABORATORS' CHECK (status IN ('SEEKING_COLLABORATORS', 'IN_DEVELOPMENT', 'COMPLETED', 'ON_HOLD')),
    owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tags TEXT[], -- PostgreSQL array for tags
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Project collaborators (many-to-many relationship)
CREATE TABLE project_collaborators (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(50) DEFAULT 'COLLABORATOR',
    joined_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(project_id, user_id)
);

-- Project likes
CREATE TABLE project_likes (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(project_id, user_id)
);

-- Project comments
CREATE TABLE project_comments (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    parent_comment_id BIGINT REFERENCES project_comments(id) ON DELETE CASCADE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User badges/achievements
CREATE TABLE user_badges (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    badge_name VARCHAR(100) NOT NULL,
    badge_description TEXT,
    badge_icon VARCHAR(100),
    progress INTEGER DEFAULT 0,
    max_progress INTEGER DEFAULT 100,
    is_earned BOOLEAN DEFAULT FALSE,
    earned_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, badge_name)
);

-- Announcements table
CREATE TABLE announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    priority VARCHAR(20) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    target_audience VARCHAR(20) DEFAULT 'ALL' CHECK (target_audience IN ('ALL', 'STUDENTS', 'ADMINS')),
    is_active BOOLEAN DEFAULT TRUE,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_department ON users(department);
CREATE INDEX idx_events_date ON events(event_date);
CREATE INDEX idx_events_category ON events(category);
CREATE INDEX idx_events_organizer ON events(organizer_id);
CREATE INDEX idx_events_featured ON events(is_featured);
CREATE INDEX idx_event_registrations_user ON event_registrations(user_id);
CREATE INDEX idx_event_registrations_event ON event_registrations(event_id);
CREATE INDEX idx_projects_owner ON projects(owner_id);
CREATE INDEX idx_projects_category ON projects(category);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_featured ON projects(is_featured);
CREATE INDEX idx_project_likes_project ON project_likes(project_id);
CREATE INDEX idx_project_likes_user ON project_likes(user_id);
CREATE INDEX idx_project_comments_project ON project_comments(project_id);
CREATE INDEX idx_user_badges_user ON user_badges(user_id);
CREATE INDEX idx_announcements_active ON announcements(is_active);

-- Create triggers for updating timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_events_updated_at BEFORE UPDATE ON events FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_projects_updated_at BEFORE UPDATE ON projects FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_project_comments_updated_at BEFORE UPDATE ON project_comments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_announcements_updated_at BEFORE UPDATE ON announcements FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data
-- Sample admin user
INSERT INTO users (email, password_hash, first_name, last_name, role, is_active, email_verified) VALUES
('admin@campus.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', 'ADMIN', true, true);

-- Sample students
INSERT INTO users (email, password_hash, first_name, last_name, role, department, academic_year, bio, is_active, email_verified) VALUES
('john.doe@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Doe', 'STUDENT', 'Computer Science', '3rd Year', 'Passionate about AI and web development', true, true),
('sarah.chen@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Sarah', 'Chen', 'STUDENT', 'Computer Science', '4th Year', 'AI researcher and tech enthusiast', true, true),
('mike.johnson@university.edu', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Mike', 'Johnson', 'STUDENT', 'Environmental Engineering', '2nd Year', 'Sustainability advocate and app developer', true, true);

-- Sample events
INSERT INTO events (title, description, long_description, category, event_date, start_time, end_time, location, max_attendees, organizer_id, tags, is_featured) VALUES
('AI & Machine Learning Symposium', 'Join industry experts discussing the future of artificial intelligence', 'Comprehensive symposium covering AI research, applications, and career opportunities', 'TECHNOLOGY', '2024-11-25', '14:00', '17:00', 'Engineering Building, Hall A', 200, 1, ARRAY['AI', 'Machine Learning', 'Technology'], true),
('Startup Pitch Competition', 'Present your innovative startup ideas to investors', 'Annual competition for student entrepreneurs to showcase their business ideas', 'CAREER', '2024-11-28', '10:00', '16:00', 'Business School Auditorium', 150, 1, ARRAY['Entrepreneurship', 'Pitch', 'Startup'], false),
('Career Fair - Tech Companies', 'Meet recruiters from top technology companies', 'Networking event with leading tech companies offering internships and jobs', 'CAREER', '2024-12-05', '11:00', '18:00', 'Student Center Main Hall', 500, 1, ARRAY['Career', 'Internship', 'Technology'], true);

-- Sample projects
INSERT INTO projects (title, description, category, difficulty_level, expected_duration, team_size, required_skills, owner_id, tags, is_featured, status) VALUES
('AI-Powered Study Assistant', 'An intelligent chatbot that helps students with coursework', 'AI_ML', 'ADVANCED', '3+ months', '4-5 people', ARRAY['Python', 'Machine Learning', 'NLP', 'React'], 2, ARRAY['AI', 'Education', 'Chatbot'], true, 'SEEKING_COLLABORATORS'),
('Campus Sustainability Tracker', 'Mobile app tracking sustainable practices on campus', 'MOBILE', 'INTERMEDIATE', '1-2 months', '2-3 people', ARRAY['React Native', 'Node.js', 'MongoDB'], 3, ARRAY['Mobile App', 'Sustainability', 'Gamification'], false, 'IN_DEVELOPMENT'),
('Virtual Reality Lab Experience', 'VR experiences for chemistry and physics labs', 'OTHER', 'ADVANCED', '3+ months', '4-5 people', ARRAY['Unity', 'C#', '3D Modeling', 'VR'], 2, ARRAY['VR', 'Education', '3D Modeling'], true, 'COMPLETED');

-- Sample event registrations
INSERT INTO event_registrations (user_id, event_id) VALUES
(2, 1), (3, 1), (2, 3), (3, 2);

-- Sample project likes
INSERT INTO project_likes (project_id, user_id) VALUES
(1, 3), (2, 2), (3, 3);

-- Sample user badges
INSERT INTO user_badges (user_id, badge_name, badge_description, progress, max_progress, is_earned, earned_date) VALUES
(2, 'Event Explorer', 'Attended 5+ campus events', 100, 100, true, CURRENT_TIMESTAMP),
(2, 'Idea Generator', 'Posted 3+ project ideas', 100, 100, true, CURRENT_TIMESTAMP),
(3, 'Community Builder', 'Connected with 10+ students', 80, 100, false, null);

-- Sample announcements
INSERT INTO announcements (title, content, author_id, priority, target_audience) VALUES
('Welcome to Campus Connect!', 'We are excited to launch our new student community platform. Join events, share project ideas, and connect with fellow students.', 1, 'HIGH', 'ALL'),
('Tech Innovation Summit Registration Open', 'Registration is now open for the annual Tech Innovation Summit. Limited seats available!', 1, 'NORMAL', 'STUDENTS');

-- Update counters
UPDATE projects SET 
    likes_count = (SELECT COUNT(*) FROM project_likes WHERE project_id = projects.id),
    comments_count = (SELECT COUNT(*) FROM project_comments WHERE project_id = projects.id);

UPDATE events SET 
    current_attendees = (SELECT COUNT(*) FROM event_registrations WHERE event_id = events.id AND attendance_status = 'REGISTERED');