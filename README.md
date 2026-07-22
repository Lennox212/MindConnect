# Student Curriculum AI

Student Curriculum AI is an AI-powered academic planning platform built with Django that helps students plan their degree path more efficiently. By analyzing a student's completed coursework and curriculum requirements, the application provides personalized course recommendations and academic guidance to support informed semester planning.

The platform combines full-stack web development, Firebase Authentication, and OpenAI integration to deliver an intelligent advising experience that simplifies curriculum navigation and helps students stay on track toward graduation.

---

## Project Status

This project is actively under development as part of my software engineering portfolio. The current implementation includes secure authentication, curriculum management, AI-assisted academic advising, and personalized course recommendations. Additional features and enhancements will continue to be added as the project evolves.

---

## Features

### AI-Powered Academic Advising

- Personalized course recommendations based on completed coursework
- AI-generated academic guidance for semester planning
- Intelligent curriculum navigation
- Degree progression assistance using OpenAI
- Personalized recommendations tailored to each student's academic history

### Student Management

- Student Registration
- Secure Login
- Firebase Authentication
- Student Profile Management
- Personalized Dashboard

### Curriculum Planning

- Track completed courses
- Plan future semesters
- Monitor degree progress
- View curriculum requirements
- Organize academic plans

### Backend Functionality

- Django MVC Architecture
- Database Integration
- Form Validation
- Secure Authentication
- Modular Application Design

---

## Technology Stack

| Category | Technologies |
|----------|--------------|
| Language | Python |
| Framework | Django |
| Authentication | Firebase Authentication |
| Artificial Intelligence | OpenAI API |
| Database | SQLite (Development) |
| Frontend | HTML, CSS, JavaScript |
| Additional Services | Firebase |

---

## Project Structure

```text
StudentCurriculumAI
│
├── StudentCurriculum/
├── accounts/
│   ├── management/
│   ├── migrations/
│   ├── templates/
│   ├── static/
│   ├── models.py
│   ├── views.py
│   ├── forms.py
│   ├── urls.py
│   └── firebase_auth_service.py
│
├── templates/
├── static/
├── manage.py
└── requirements.txt
```

---

## How It Works

1. Students create an account using Firebase Authentication.
2. They record completed coursework and academic information.
3. The application compares completed courses against degree requirements.
4. OpenAI analyzes the student's academic progress.
5. Personalized course recommendations and academic guidance are generated.
6. Students use these recommendations to plan future semesters and monitor progress toward graduation.

---

## Installation

### Prerequisites

- Python 3.11+
- Django
- Firebase Project
- OpenAI API Key

### Clone the Repository

```bash
git clone https://github.com/Lennox212/Student-Curriculum-AI.git
```

### Install Dependencies

```bash
pip install -r requirements.txt
```

### Configure Environment Variables

Create a `.env` file containing your credentials:

```env
OPENAI_API_KEY=your_openai_api_key
FIREBASE_CREDENTIALS=your_firebase_credentials
```

### Run the Application

```bash
python manage.py migrate
```

```bash
python manage.py runserver
```

The application will be available at:

```
http://127.0.0.1:8000
```

---

## Skills Demonstrated

This project demonstrates experience with:

- Python
- Django
- Firebase Authentication
- OpenAI API Integration
- Full-Stack Web Development
- Database Design
- Authentication & Authorization
- AI-Assisted Decision Support
- Software Architecture
- REST API Integration

---

## Future Enhancements

Planned improvements include:

- Interactive degree progress visualization
- Support for multiple universities
- Faculty and advisor portal
- AI chat assistant for academic advising
- Automatic prerequisite validation
- Exportable academic plans
- Docker deployment
- Comprehensive unit and integration testing

---

## About

Student Curriculum AI was developed as part of my software engineering portfolio to demonstrate the integration of artificial intelligence into a practical educational application. By combining Django, Firebase, and OpenAI, the project showcases how AI can be used to provide personalized academic recommendations and improve the student planning experience.

---

## Author

**Lennox Rivera**

Computer Engineering Graduate

Python • Django • Firebase • OpenAI • Full-Stack Development
