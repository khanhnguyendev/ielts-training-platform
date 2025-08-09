# IELTS Speaking Platform – Project Description

## Overview

The IELTS Speaking Platform is an **AI-powered learning and assessment system** designed to help users practice, improve, and track their IELTS Speaking performance. It combines **real-time speech recognition, AI-based scoring, error correction, and content generation** with a modern, scalable architecture.

The platform integrates **Next.js** for the frontend, **Java (Spring Boot)** for core business services, and **Python (FastAPI)** for AI-heavy tasks such as speech-to-text, scoring, and feedback.

---

## Core Features

### 1. **Automated Scoring**
- AI evaluates speaking answers based on **IELTS band descriptors**:
  - Pronunciation
  - Fluency & Coherence
  - Grammar
  - Vocabulary
- Uses **Speech-to-Text (ASR)** followed by NLP-based scoring models.

### 2. **Error Correction**
- Detects and highlights **grammar, vocabulary, and pronunciation mistakes**.
- Suggests **corrected sentences** side-by-side with user input.
- Helps avoid repeating mistakes.

### 3. **Mock Test Simulation**
- Simulates **IELTS Speaking test format**:
  - Part 1: Introduction & Interview
  - Part 2: Long Turn
  - Part 3: Discussion
- Includes **timers, sequential questions, and recording tools**.

### 4. **Forecast Question Bank**
- Regularly updated **database of recent IELTS topics**.
- Categorized by **Speaking Part** and topic theme.
- Allows targeted practice.

### 5. **Sample Answers & Outlines**
- High-quality sample answers for each question.
- Structured into:
  - Direct Answer
  - Additional Details
  - Example
- Teaches structure and depth in responses.

### 6. **Self-Answer Enhancement**
- AI rewrites user’s answers:
  - Improves **vocabulary richness**
  - Increases **coherence**
  - Keeps user’s original meaning.

### 7. **Sentence Structuring Improvement**
- Suggests **linking words** and **connectors**.
- Reorders sentences for better flow and coherence.

### 8. **Practical Grammar Lessons**
- Extracts **grammar rules** from user mistakes.
- Provides **examples in IELTS context**.

### 9. **Vocabulary Learning**
- Extracts useful vocabulary from **questions and user answers**.
- Allows bookmarking and review of words.
- Provides **definitions, translations, and usage examples**.

---

## Supporting Features

### 10. **User & Auth**
- User registration and login.
- Tracks practice history and progress.

### 11. **Admin Dashboard**
- Manage **questions, vocabulary, and AI configurations**.
- Upload and tag forecast questions.

### 12. **Analytics & Reports**
- Track usage patterns, improvement over time, and most common mistakes.

### 13. **Observability**
- **Metrics** for system health.
- **Tracing** for AI calls.
- **Dashboards** for user engagement.

---

## Technology Stack

### Frontend
- **Next.js** (TypeScript, App Router)
- API SDK generated from **OpenAPI** specs
- WebSockets/SSE for live timers and feedback

### Backend – Core Services (Java, Spring Boot)
- `gateway`: Public REST API, routes to AI services via gRPC
- `auth-service`: JWT-based authentication
- `cms-service`: Content management (questions, topics)
- `exam-orchestrator`: Test flow & state management
- `billing-service`: Plans and usage limits
- `analytics-service`: Tracks usage and improvement

### AI Services (Python, FastAPI)
- `asr-service`: Speech-to-text using Whisper/Azure/GCP
- `scoring-service`: Band scoring for Pronunciation, Fluency, Grammar, Vocabulary
- `correction-service`: Grammar/vocabulary/pronunciation fixes
- `content-gen-service`: Forecast questions, sample answers, vocabulary extraction

### Async Jobs
- Queue-based (Redis/RabbitMQ)
- Handles long-running AI jobs asynchronously

### Infra & DevOps
- Docker + Docker Compose for local dev
- Kubernetes (Kustomize overlays for dev/staging/prod)
- Terraform stubs for cloud provisioning
- OpenTelemetry for tracing
- Prometheus/Grafana for metrics

---

## Future Roadmap
- **Peer review**: Users give feedback on each other’s answers.
- **Live speaking partner matching**.
- **Gamification**: Badges, streaks, leaderboards.
- **Personalized learning paths**.
- **Multi-language UI**.

---

## Target Users
- IELTS candidates
- English learners aiming for professional communication improvement
- Language schools offering online IELTS preparation

---

