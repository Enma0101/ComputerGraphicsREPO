# Formula 1 Simulator - Computer Graphics

This project is a Formula 1 simulator developed as part of a computer graphics study. It implements a comprehensive system ranging from user management to race dynamics and pit stop simulations.

## Key Features

The system is structured into several modules that provide a complete simulation experience:

- **Session Management**: Authentication and user registration system using a local database.
- **Vehicle Visualization**: Dedicated rendering engines for the Formula 1 cars and the circuit environment.
- **Race Simulation**: Competition logic including real-time position and timing tracking.
- **Strategic Interaction**: Detailed views for the Garage and Pit Stop, allowing for technical management during the race.
- **Customization**: Configuration modules to adjust the visual and auditory experience of the simulator.

## Technical Architecture

The project uses a modern technology stack focused on performance and portability:

- **Language**: Java 23 (JDK 23).
- **Interface and Graphics**: JavaFX for rendering the user interface and graphic components.
- **Persistence**: SQLite for persistent storage of user profiles and historical records.
- **Dependency Management**: Maven for building and automation.

## Setup and Execution

To run the project locally, ensure you have Java 23 and Maven installed. Follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project's root directory.
3. Run the following command to compile and launch the application:

```bash
mvn clean javafx:run
```

This command will download the necessary dependencies, compile the source code, and start the simulator from the main entry point.

Enmanuel Fuenmayor 2024
