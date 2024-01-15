# URL Shortening Service Design Document

## Overview
The goal of this project is to create a URL shortening service that allows users to submit long URLs and receive shortened versions for easier use. The service consists of a backend REST API responsible for shortening, redirecting, and deleting URLs. A frontend to provide a user interface for URL submission.

## Demo 
Incase the URL is not working, here is a recorded demo.



## Project Setup
- **Project Structure**: The project is organized in two separate directories -- one for backend and, other one for frontend. Their name are self-explanatory. 
- **Programming Language**: For backend, Java is used as the programming language using Dropwizrd while the frontend is implemented using React.
- **Setup**: 

```
git clone https://github.com/tanmesh/urlshortener.git

## running the backend
cd backend 

mvn clean install

java -jar ./target/backend-1.0-SNAPSHOT.jar server ./src/main/resources/tinyurl.config.prod.yaml

## running the frontend

npm install .

npm start
```


## REST API for URL Shortening

### Database
For the project, I opted for using NoSql database, MongoDB. 

### API Endpoint

- **Create Shortened URL (POST):**
    - Endpoint: `/tinyurl/`
    - Description: Create a shortened URL by submitting a long URL.
    - Request Headers:
        ```
            Content-Type    application/json
        ```
    - Request Body:
        ```json
        {
            "longUrl": "codingchallenges.fyi/"
        }
        ```
    - Response:
      ```
        http://localhost:8080/tinyurl/YPBqS+
      ```
    - Status Codes:
      - 201 Created: Successfully created a shortened URL.
      - 400 Bad Request: Invalid request, missing the 'url' field.
      - 409 Conflict: A shortened URL already exists for the provided long URL.

- **Retrieve Original URL (GET):**
    - Endpoint: `/tinyurl/{key}`
    - Description: Retrieve the original long URL by providing the shortened key.
    - Request Headers:
        ```
            Content-Type    application/json
        ```
    - Response:
      - Redirects to the original long URL if the key is valid.
    - Status Codes:
      - 302 Found: Successfully found the original URL.
      - 404 Not Found: Shortened URL key not found.

- **Delete Shortened URL (DELETE):**
    - Endpoint: `/tinyurl/{key}`
    - Description: Delete the shortened URL associated with the provided key.
    - Request Headers:
        ```
            Content-Type    application/json
        ```
    - Response:
      - Empty response body.
    - Status Codes:
      - 200 OK: Successfully deleted the shortened URL.
      - 404 Not Found: Shortened URL key not found.

- **Idempotency**: The process of ensuring that creating a shortened URL is an idempotent operation involves checking whether the requested long URL has been previously shortened. If it has, the saved short URL is returned; otherwise, a new short URL is generated.

## Build GUI for URL Submission
- Design a simple web using React for users to submit long URLs.
- Use the backend REST API to handle form submissions and update the UI with the result.

## CI/CD Pipeline
- Set up an automated integration and deployment pipeline using GitHub Actions.
- Ensured that commits to the master branch trigger build processes.
- Deploy the application automatically if tests pass.

## Future tasks
- **User Authentication**: Integrate user authentication for enhanced security. 
- **Create custom TinyUrl**: Creating custom api for the user.
- **Payment Solution**: Explore integrating payment solutions like Stripe for additional features.