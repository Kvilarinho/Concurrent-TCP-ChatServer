


# Concurrent TCP Chat — Server & Client

A concurrent TCP chat application built in Java, featuring a multi-client server, private messaging (whispers), admin mode with server shutdown, and a simple interactive client.
The system demonstrates concurrency, socket programming, command parsing, clean architecture, and unit testing with JUnit + Mockito.

![Server running](/images/tcp_chatserver_running.png)

---

## Overview

This repository contains **two separate Java applications**:

- **ChatServer** — a concurrent TCP server that accepts multiple clients, broadcasts public messages, handles commands, and supports admin-level operations.
- **Client** — a terminal-based TCP client that connects to the server, reads keyboard input, and displays messages in real time.

Both applications run independently and communicate over TCP sockets.

---

## Features

### Server
- Accepts multiple clients concurrently
- Broadcasts messages
- Private messaging (/whisper)
- Admin mode (/admin)
- Server shutdown (/shutdown)
- Thread-safe client management

### Client
- Real-time messaging over TCP
- Graceful exit (/quit)
- Separate threads for input/output

---

## Project Structure

```
Concurrent-TCP-Chat/
├── ChatServer/
├── Client/
├── images/
│   └── tcp_chatserver_running.png
└── README.md
```

---

## Commands

### Client Commands
| Command | Description |
|---------|-------------|
| /help | List commands |
| /list | Show connected users |
| /name <newName> | Change username |
| /whisper <user> <msg> | Private message |
| /admin <password> | Admin login |
| /quit | Leave chat |

### Admin Only
| Command | Description |
|---------|-------------|
| /shutdown | Shut down server |

---

## How to Run

### Start the Server

```
cd ChatServer
mvn clean package
java -jar target/ChatServer-1.0-SNAPSHOT.jar
```

### Start a Client

```
cd Client
mvn clean package
java -jar target/Client-1.0-SNAPSHOT.jar
```

---

## Unit Tests

Run tests:

```
mvn test
```

Includes tests for:

- Client
- ReaderThread
- KeyboardThread
- ChatServer
- ClientHandler

---

## Javadoc

Generate documentation:

```
mvn javadoc:javadoc
```

Output:

```
target/site/apidocs/
```

---

## Author

Developed by Katia Vilarinho.