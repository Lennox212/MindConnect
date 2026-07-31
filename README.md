# MindConnect Backend

MindConnect is a RESTful social media platform focused on mental wellness and community support. This repository contains the backend API built with Spring Boot, providing secure authentication, user management, post interactions, comments, likes, refresh tokens, email verification, and AWS S3 integration for profile images.

The backend follows a layered architecture using Controllers, Services, Repositories, DTOs, and Entity classes while implementing JWT authentication and role-based authorization.

---

## Features

### Authentication
- User registration
- User login
- JWT Authentication
- Refresh Token Authentication
- Email verification
- OTP password reset
- BCrypt password encryption

### User Management
- View current authenticated user
- Update user information
- Upload profile picture
- Private profile image storage using AWS S3
- Presigned URL generation for secure image access
- Account status validation
    - Active
    - Inactive
    - Suspended
    - Frozen
    - Deactivated

### Posts
- Create posts
- Update posts
- Delete posts
- View public posts
- View posts by visibility
- User-specific posts

### Comments
- Create comments
- Update comments
- Delete comments
- Retrieve comments for posts

### Likes
- Like posts
- Unlike posts
- Prevent duplicate likes

### Security
- Spring Security
- JWT Access Tokens
- Refresh Tokens
- Authentication Filter
- Authorization Rules
- Password Encryption

### Exception Handling
Global exception handling provides consistent API responses for:

- Authentication errors
- Invalid credentials
- Invalid OTP
- Unauthorized access
- Missing resources
- Validation failures
- Account restrictions

---

## Technologies

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

### Database

- MySQL

### Authentication

- JWT
- Refresh Tokens
- BCrypt

### Cloud

- AWS S3
- Presigned URLs

### Email

- Java Mail Sender
- Gmail SMTP

---

## Project Structure

```
src
├── config
├── controller
├── customexceptions
├── entity
├── exceptionhandler
├── payload
│   ├── request
│   └── response
├── repository
├── service
└── utils
```

---

## Security

Sensitive configuration values are stored using environment variables.

Examples include:

```
DB_PASSWORD
JWT_SECRET
MAIL_USERNAME
MAIL_PASSWORD
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
AWS_S3_BUCKET_NAME
```

---

## API Features

- RESTful architecture
- DTO-based request and response models
- Layered architecture
- Repository pattern
- Dependency Injection
- Global exception handling
- Secure authentication and authorization

---

## Future Improvements

- Direct Messaging
- Friend System
- Groups
- Notifications
- Content Moderation
- Search
- Pagination
- Admin Dashboard

---

## Frontend

The React frontend for this project is maintained in a separate repository.

```
MindConnect Frontend
```

---

## Author

Lennox Rivera

Computer Engineering Graduate

GitHub:
https://github.com/Lennox212
