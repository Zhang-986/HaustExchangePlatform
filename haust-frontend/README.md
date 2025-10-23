# HAUST Exchange Platform - Frontend

A Vue 3 + TypeScript + Vite frontend application for the HAUST Exchange Platform.

## Features

- **User Authentication**: Login and registration with JWT token-based authentication
- **Forum System**: Create, view, edit, and delete posts with commenting and like functionality
- **Referral System**: Submit and manage job referrals with approval workflow
- **Real-time Chat**: WebSocket-based instant messaging with public chat rooms
- **Admin Dashboard**: Manage pending referrals and view platform statistics
- **Responsive Design**: Built with Element Plus UI framework

## Tech Stack

- **Vue 3**: Progressive JavaScript framework
- **TypeScript**: Type-safe development
- **Vite**: Next-generation frontend tooling
- **Vue Router**: Official router for Vue.js
- **Pinia**: State management for Vue 3
- **Axios**: Promise-based HTTP client
- **Element Plus**: Vue 3 UI library
- **SockJS + STOMP**: WebSocket communication

## Project Structure

```
haust-frontend/
├── src/
│   ├── api/              # API service layer
│   │   ├── auth.ts       # Authentication APIs
│   │   ├── forum.ts      # Forum APIs
│   │   ├── referral.ts   # Referral APIs
│   │   └── request.ts    # Axios configuration
│   ├── components/       # Reusable components
│   │   └── layout/       # Layout components
│   ├── router/           # Vue Router configuration
│   ├── store/            # Pinia stores
│   │   ├── auth.ts       # Authentication store
│   │   └── chat.ts       # Chat store
│   ├── types/            # TypeScript type definitions
│   ├── views/            # Page components
│   │   ├── auth/         # Login and registration
│   │   ├── forum/        # Forum pages
│   │   ├── referral/     # Referral pages
│   │   ├── chat/         # Chat room
│   │   └── admin/        # Admin dashboard
│   ├── App.vue           # Root component
│   └── main.ts           # Application entry point
├── .env                  # Environment variables
├── .env.development      # Development environment
├── .env.production       # Production environment
├── vite.config.ts        # Vite configuration
├── tsconfig.json         # TypeScript configuration
└── package.json          # Dependencies
```

## Getting Started

### Prerequisites

- Node.js 16+ and npm
- Backend services running (see main README)

### Installation

1. Navigate to the frontend directory:
```bash
cd haust-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Configure environment variables:
Edit `.env.development` to set your backend API URL:
```
VITE_API_BASE_URL=http://localhost:8080
```

### Development

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173`

### Build for Production

```bash
npm run build
```

The built files will be in the `dist/` directory.

### Preview Production Build

```bash
npm run preview
```

## API Integration

The frontend integrates with the following backend services:

### Authentication APIs
- `POST /user/register` - User registration
- `POST /user/login` - User login
- `POST /admin/login` - Admin login
- `GET /user/info` - AI Q&A assistant

### Forum APIs
- `POST /user/posts` - Create post
- `GET /user/posts` - Get posts (paginated)
- `GET /user/posts/:id` - Get post details
- `PUT /user/posts/:id` - Update post
- `DELETE /user/posts/:id` - Delete post
- `POST /user/posts/:id/like` - Like/unlike post
- `POST /user/replies` - Add reply
- `GET /user/replies` - Get replies
- `POST /user/replies/:id/like` - Like/unlike reply

### Referral APIs
- `POST /user/addInfo` - Submit referral
- `GET /user/page` - Get referrals (paginated)
- `PUT /user/modify` - Update referral
- `DELETE /user/delete/:id` - Delete referral
- `GET /user/myInfo` - Get user's referrals
- `GET /admin/page` - Admin get pending referrals
- `PUT /admin/permit` - Approve/reject referral

### WebSocket
- `ws://localhost:8080/im/ws` - WebSocket endpoint for real-time chat

## Features by Module

### Authentication Module
- User registration with validation
- User and admin login
- JWT token management
- Auto-redirect on authentication failure

### Forum Module
- Create posts with optional anonymity
- View all posts with pagination
- Post details with replies
- Like/unlike posts and replies
- My posts management
- Delete posts

### Referral Module
- Submit job referrals with company info
- View all approved referrals
- Manage personal referrals
- Delete referrals
- Rating system

### Chat Module
- Connect to chat room with username
- Public messaging
- View online users
- Real-time message updates
- Auto-scroll to latest messages

### Admin Module
- Review pending referrals
- Approve or reject referrals
- View platform statistics

## Environment Variables

- `VITE_API_BASE_URL`: Backend API base URL (default: http://localhost:8080)

## Development Notes

- The frontend uses TypeScript for type safety
- All API calls are typed with interfaces defined in `src/types/index.ts`
- State management is handled by Pinia stores
- Axios interceptors handle authentication tokens and error responses
- WebSocket connection is managed through the chat store

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## License

This project is licensed under the MIT License.
