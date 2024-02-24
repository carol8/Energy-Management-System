## Build and Execution Considerations
To build and run the Energy Management System, follow these steps:
### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Docker desktop
### Building and Configuring Microservices
1.	Clone the backend repository.
2.	Build and deploy each microservice using the provided docker files / docker compose.
### Building and Configuring the Frontend
1. Clone the React frontend application repository.
2. Build and deploy the frontend using the provided docker files.
### Database Configuration
There is no need to do additional configuration, as the default configuration is included in the microservice compose.yaml file. However, if another configuration is needed, further configuration is required.
### Running the System
1. Deploy the microservices, the database and the React frontend application.
2. Access the system in your web browser, and perform the following actions:
    - Administrators: Log in with your admin credentials to perform CRUD operations on users, devices, and user-device mapping.
    - Clients: Log in with your client credentials to view your associated smart energy metering devices.
### User Credentials
- Administrator:
    - Username: carol8
    - Password: secret
- Client:
    - Username: maria
    - Password: user
