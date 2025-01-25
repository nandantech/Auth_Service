
# User Authentication Service

This is a user authentication service built with **Spring Boot** that includes features such as user registration, login, email verification, password reset functionality, JWT authentication, and more. Additionally, the service supports OAuth2 integration with Google and Facebook for user authentication.

## Key Features

- **User Registration**: Allows users to sign up by providing their details (name, email, password).
  
- **Email Verification**: Sends a verification code to the user’s email upon registration. The verification code expires after a defined period (e.g., 1 hour).

- **Resend Verification Code**: Users can request a new verification code if the original one has expired.

- **JWT Authentication**: Secure user authentication using JWT tokens. Tokens are generated upon successful login and are used for authenticating subsequent requests.

- **Forgot Password**: Users can reset their password by receiving a reset link on their registered email. The link contains a reset token with an expiration time.

- **User Login**: Allows users to log in with their email and password. A JWT token is returned upon successful authentication.

## Upcoming Features

- **OAuth2 Authentication with Google and Facebook**: Integration of OAuth2 for social login using Google and Facebook to allow users to authenticate with their existing accounts.

## Technologies Used

- **Spring Boot**: For building the backend service.
- **Spring Security**: For handling authentication and authorization.
- **JWT (JSON Web Tokens)**: For secure authentication.
- **JavaMailSender**: For sending email verification and password reset emails.
- **H2 Database**: For development and testing purposes. (Can be replaced with any relational database in production).
- **Spring Data JPA**: For interacting with the database.
- **OAuth2**: To integrate with Google and Facebook for user authentication.


## Setup and Installation

### Prerequisites

- **Java 11+**
- **Maven** (or **Gradle**, depending on your setup)
- A valid **Gmail SMTP account** for sending email verification and password reset links.
- A Google and/or Facebook Developer account for OAuth2 integration.

### 1. Clone the Repository

```bash
https://github.com/nandantech/Auth_Service.git
```

### 2 .env file configuration for the User Authentication Service

#### JWT Secret Key for encoding and decoding JWT tokens
JWT_SECRET_KEY=your-jwt-secret-key

#### OAuth2 configuration for Google login
OAUTH2_GOOGLE_CLIENT_ID=your-google-client-id
OAUTH2_GOOGLE_CLIENT_SECRET=your-google-client-secret

#### OAuth2 configuration for Facebook login
OAUTH2_FACEBOOK_CLIENT_ID=your-facebook-client-id
OAUTH2_FACEBOOK_CLIENT_SECRET=your-facebook-client-secret

#### JWT Secret Key for encoding and decoding JWT tokens
##### // You need to generate a string secret for JWT. You can use a secure password generator or an online tool.
JWT_SECRET_KEY=

#### Mail properties for sending verification and password reset emails
##### // Support email and app password can be found/set in your email service settings (e.g., Gmail settings).
SUPPORT_EMAIL=
APP_PASSWORD=



