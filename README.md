# Talkify – Messaging System

**Talkify** is a robust and scalable messaging system built with **Spring Boot 3.4** on the backend and **React 18 with TypeScript and Vite** on the frontend. It integrates real-time messaging, security, localization, and a structured database schema with migrations.

## ✨ Features

### 📌 Backend (Spring Boot 3.4, Java 23)
- **Spring Security** – Authentication and authorization for secure communication.  
- **QueryDSL** – Type-safe query generation for better database interactions.  
- **HATEOAS** – Hypermedia-driven REST API design.  
- **WebSockets** – Real-time messaging.  
- **Mailing System** – Dynamic email templates using **Freemarker**, localized for different languages.  
- **H2 & Flyway** – In-memory database for development and structured database migrations.  
- **Frontend Maven Plugin** – Builds both frontend and backend into a single JAR for easy deployment.  

### 🎨 Frontend (React 18 + TypeScript + Vite)
- **Localization (i18n)** – Supports multiple languages for UI, backend error messages, and emails.  
- **Infinite Scroll** – Smooth scrolling for chat messages, channels, and friend lists.  
- **Responsive UI** – Optimized for different screen sizes.  

### 💬 Messaging System
- **Channels** – Conversations with multiple members.  
- **Private Channels** – Secure chats for friends.  
- **Roles & Permissions** – Manage users as **Owner, Admin, or Guest** within channels.  
- **Real-time Chat** – Instant updates using WebSockets.  

## 🚀 Getting Started

### Prerequisites
- Node.js & yarn  
- Maven  

### Build Full Application
```sh
mvn clean package
```
This generates a single JAR file containing both frontend and backend.  

## 📜 License
MIT License  
