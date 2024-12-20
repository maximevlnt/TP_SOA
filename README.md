# Room Management Automation System (TP SOA)

## Project Overview
This project is a Proof-of-Concept (PoC) web application for managing rooms at INSA. The application automates room management tasks such as turning on/off lights, opening/closing windows and doors, and managing heating systems based on data retrieved from sensors and analyzed by microservices. The architecture leverages **Spring Boot** and the **Java** programming language. Sensors and actuators are simulated for the purpose of this project.

## Features
- **Automated Light Control**: Turns lights on/off based on the combination of ambient light levels and room occupancy.
- **Microservice-Based Architecture**: Independent services handle sensor data, decision-making logic, and actuator operations.
- **Web Interface**: Displays the history of actions and provides insights into the system's behavior.

## Scenarios Implemented
### Scenario: Light Management
- **Description**: This scenario automates the control of room lights based on the following criteria:
  1. Lights are turned on if the room is occupied and the external light level is below a specified threshold.
  2. Lights are turned off if the room is unoccupied or if external light exceeds the threshold.

## Architecture
The application is structured as a **microservice-based system**. It consists of the following components:

1. **Luminosity Sensor Service**: Manages simulated data for external light levels.
2. **Presence Sensor Service**: Manages simulated data for room occupancy.
3. **Decision-Making Service**: Implements system intelligence by applying thresholds and rules to determine actuator actions.
4. **Actuator Service**: Handles light control by turning lights on or off based on decisions made by the system.

### Data Flow
1. The **Luminosity Sensor Service** retrieves external light data.
2. The **Presence Sensor Service** retrieves room occupancy data.
3. The **Decision-Making Service** collects data from the two sensor services, applies logic (e.g., thresholds, presence checks), and determines appropriate actions.
4. The **Actuator Service** executes the actions (e.g., turning lights on/off).

## Technologies Used
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Frontend**: HTML, CSS, JavaScript (for the web interface)
- **Build Tool**: Maven
- **Database**: In-memory database for storing the history of actions

## Installation and Setup
### Prerequisites
- Java 23
- Maven

### Steps to Run the Project
1. Clone the repository:
   ```bash
   git clone <repository_url>
   ```
2. Navigate to the project directory:
   ```bash
   cd room-management-automation
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the web interface at:
   ```
   http://localhost:8080
   ```

## Web Interface
The web interface provides:
- A dashboard to visualize sensor data.
- A history view to see past actions performed by the actuators.

## Future Enhancements
- Integration with real sensors and actuators using IoT protocols.
- Additional scenarios (e.g., temperature-based heating management, CO2-based ventilation control).
- Enhanced security features for web application access.
