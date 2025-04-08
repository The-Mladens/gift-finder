# Gift Finder

Gift Finder is a cloud-based application designed to help users find the perfect gift using AI-powered recommendations. The system is built with a modern architecture, leveraging Azure services, a PostgreSQL database, and the OpenAI API for intelligent responses.

---

## System Architecture

The system consists of the following main components:

### 1. **Frontend**
- **Technology**: React (hosted on Azure Static Web Apps)
- **Description**: 
  - The frontend provides a user-friendly interface for interacting with the application.
  - It communicates with the backend via HTTP requests to fetch data and display AI-generated recommendations.
  - Hosted as a static web app on Azure for scalability and performance.

### 2. **Backend**
- **Technology**: Azure Function App (Java-based)
- **Description**:
  - The backend is implemented as serverless functions using Azure Function App.
  - It handles requests from the frontend, processes data, and interacts with the database and OpenAI API.
  - The backend is designed to scale automatically based on demand.

### 3. **Database**
- **Technology**: PostgreSQL (hosted on Azure Database for PostgreSQL)
- **Description**:
  - The database stores user data, chat completions, and other application-related information.
  - Stored procedures are used for secure and efficient data operations.
  - Connection details are securely managed using Azure App Settings.

### 4. **OpenAI API**
- **Technology**: OpenAI GPT API
- **Description**:
  - The OpenAI API is used to generate intelligent responses and recommendations based on user input.
  - The backend invokes the API to process user queries and return AI-powered results.

---

## Architecture Diagram

```plaintext
+-------------------+       +-----------------------+
|   User/Client     |       |  Azure Resource Group |
|-------------------|       |-----------------------|
|                   |       | +-------------------+ |
| Azure Static Web  +------->| Azure Function App  | |
|       App         |       | +-------------------+ |
|                   |       |         |             |
+-------------------+       |         v             |
                             | +-------------------+ |
                             | | PostgreSQL DB     | |
                             | +-------------------+ |
                             |         |             |
                             |         v             |
                             | +-------------------+ |
                             | | OpenAI API        | |
                             | +-------------------+ |
                             +-----------------------+