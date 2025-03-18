[![Java CI with Maven](https://github.com/jamilxt/DailyConnect/actions/workflows/maven.yml/badge.svg)](https://github.com/jamilxt/DailyConnect/actions/workflows/maven.yml)
# DailyConnect

DailyConnect is a Spring Boot web application designed to facilitate real-time communication, task tracking, and daily inspiration for users. It features a responsive UI, secure authentication, and WebSocket-based chat functionality, making it ideal for teams or individuals looking to stay connected and organized.

## Live Demo
The application is deployed and accessible at:  
[**https://dailyconnect.onrender.com**](https://dailyconnect.onrender.com)

## Features
- **Real-Time Chat**: Private messaging with online user tracking and chat history sorted by last message time.
- **Dashboard**: Displays a daily word and user count (currently hardcoded).
- **Notifications**: Placeholder page for future notification features.
- **Daily Task Tracker**: Placeholder page for task management (to be expanded).
- **Responsive UI**: Adapts to mobile, tablet, and desktop screens using Tailwind CSS.
- **Authentication**: Secure login/logout with Spring Security, showing users as online immediately upon login.

## Tech Stack
- **Backend**: Spring Boot, Spring Security, Spring WebSocket (STOMP over SockJS)
- **Frontend**: Thymeleaf, Tailwind CSS, JavaScript
- **Dependencies**: 
  - `spring-boot-starter-web`
  - `spring-boot-starter-security`
  - `spring-boot-starter-websocket`
  - `spring-boot-starter-thymeleaf`
  - `sockjs-client`, `stomp-websocket` (WebJars)
  - `lombok` (optional, if used)

## Prerequisites
- **Java**: 17 or later
- **Maven**: 3.6.0 or later
- **IDE**: IntelliJ IDEA, Eclipse, or similar (optional)

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/dailyconnect.git
   cd dailyconnect

2. **Build the Project**:
   ```bash
   mvn clean package
   ```

3. **Run the Application**:
   ```bash
   java -jar target/dailyconnect-0.0.1-SNAPSHOT.jar
   ```

4. **Access the App**:
   - Open a browser and navigate to `http://localhost:8080/`.
   - Login with any of the following credentials:
     - Username: `admin`, Password: `password`
     - Username: `alice`, Password: `password`
     - Username: `bob`, Password: `password`
     - Username: `charlie`, Password: `password`

## Project Structure
```
dailyconnect/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── config/       # Spring configuration (Security, WebSocket)
│   │   │       ├── controller/   # Controllers (Chat, Dashboard)
│   │   │       ├── model/        # Data models (ChatMessage)
│   │   │       └── service/      # Business logic (ChatService, WordService)
│   │   └── resources/
│   │       ├── templates/        # Thymeleaf templates (chat.html, sidebar.html, etc.)
│   │       ├── static/           # Static resources (if any)
│   │       └── application.properties  # App configuration
│   └── test/                     # Unit tests (to be added)
├── pom.xml                       # Maven dependencies
└── README.md                     # This file
```

## Usage
- **Login**: Use any listed username/password to access the app.
- **Dashboard**: View the daily word and user count at `/`.
- **Chat**: Navigate to `/chat` to message users, see online status, and chat history.
- **Logout**: Click "Logout" in the sidebar to sign out and update online status.

## Responsive Design
- **Mobile**: Sidebar collapses with a hamburger menu; chat sections stack vertically.
- **Tablet/Desktop**: Sidebar is fixed; chat history, private chat, and online users display side-by-side.

## Future Enhancements
- Add database persistence (e.g., MySQL) for chat history and tasks.
- Implement real user count via a service instead of hardcoding.
- Expand the Daily Task Tracker with CRUD functionality.
- Enhance notifications with real-time alerts.

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details (create one if desired).

## Contact
For questions or suggestions, reach out to [your-email@example.com](mailto:your-email@example.com).
