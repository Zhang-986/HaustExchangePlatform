# HAUST Exchange Platform - Frontend Development Guide

## Quick Start

### Prerequisites
- Node.js 16+ and npm
- Backend services running on `http://localhost:8080`

### Installation & Running

```bash
# Navigate to frontend directory
cd haust-frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

The application will be available at `http://localhost:5173`

## Project Architecture

### Directory Structure

```
haust-frontend/
├── src/
│   ├── api/                    # API service layer
│   │   ├── auth.ts            # Authentication APIs
│   │   ├── forum.ts           # Forum post and reply APIs
│   │   ├── referral.ts        # Referral APIs
│   │   └── request.ts         # Axios instance configuration
│   │
│   ├── store/                  # Pinia stores
│   │   ├── auth.ts            # Authentication state
│   │   └── chat.ts            # WebSocket chat state
│   │
│   ├── router/                 # Vue Router
│   │   └── index.ts           # Route definitions
│   │
│   ├── types/                  # TypeScript definitions
│   │   └── index.ts           # All type definitions
│   │
│   ├── views/                  # Page components
│   │   ├── auth/              # Login & Register
│   │   ├── forum/             # Forum pages
│   │   ├── referral/          # Referral pages
│   │   ├── chat/              # Chat room
│   │   └── admin/             # Admin dashboard
│   │
│   ├── components/             # Reusable components
│   │   └── layout/            # Layout components
│   │
│   ├── App.vue                 # Root component
│   └── main.ts                 # Application entry
│
├── .env                        # Environment variables
├── vite.config.ts              # Vite configuration
└── tsconfig.app.json           # TypeScript config
```

## API Integration

### Authentication Flow

1. User logs in via `/login` page
2. Backend returns JWT token and role
3. Token is stored in `localStorage` and Pinia store
4. Token is automatically added to all subsequent API requests via Axios interceptor
5. On 401 response, user is redirected to login page

### API Request Example

```typescript
import { authApi } from '@/api'

// Login
const response = await authApi.login({
  account: 'user123',
  password: 'password'
})
// response: { token: string, role: number }

// Token is automatically added to headers
```

### Available APIs

#### Authentication (`auth.ts`)
- `register(data: AccountDTO): Promise<void>` - User registration
- `login(data: AccountDTO): Promise<RoleVo>` - User login
- `adminLogin(data: AccountDTO): Promise<RoleVo>` - Admin login
- `askQuestion(text: string): Promise<string>` - AI Q&A

#### Forum (`forum.ts`)
- `createPost(data: CreatePostDTO): Promise<void>`
- `updatePost(id, data): Promise<void>`
- `getPosts(params: PageDTO): Promise<PageVO<Post>>`
- `deletePost(id): Promise<void>`
- `likePost(id, flag): Promise<number>`
- `getPostDetail(id): Promise<PostVO>`
- `getMyPosts(params): Promise<PageVO<Post>>`

#### Reply (`forum.ts`)
- `addReply(data: CreateReplyDTO): Promise<void>`
- `getReplies(postId, params): Promise<PageVO<ReplyVO>>`
- `likeReply(id, flag): Promise<number>`
- `getHotReplies(postId): Promise<HotReplyVo[]>`
- `adminDeleteReply(id): Promise<void>`

#### Referral (`referral.ts`)
- `addInfo(data: CodingSharingDTO): Promise<void>`
- `modifyInfo(data): Promise<void>`
- `getPage(params: PageDTO): Promise<PageVO<CodingSharingVo>>`
- `deleteInfo(id): Promise<void>`
- `getMyInfo(params): Promise<PageVO<CodingSharing>>`
- `adminGetPage(params): Promise<PageVO<CodingSharingVo>>`
- `adminPermit(id, status): Promise<void>`

## State Management with Pinia

### Auth Store

```typescript
import { useAuthStore } from '@/store'

const authStore = useAuthStore()

// State
authStore.token       // JWT token
authStore.role        // User role (0=user, 1=admin)
authStore.isLoggedIn  // Boolean login status

// Actions
await authStore.login(credentials)
await authStore.adminLogin(credentials)
await authStore.register(credentials)
authStore.logout()
authStore.isAdmin()   // Check if current user is admin
```

### Chat Store

```typescript
import { useChatStore } from '@/store'

const chatStore = useChatStore()

// State
chatStore.messages         // All chat messages
chatStore.onlineUsers      // List of online usernames
chatStore.isConnected      // WebSocket connection status
chatStore.currentUsername  // Current user's username

// Actions
chatStore.connect(username)
chatStore.disconnect()
chatStore.sendPublicMessage(content)
chatStore.sendPrivateMessage(receiver, content)
```

## Routing

### Protected Routes

All routes except `/login` and `/register` require authentication. Admin routes also require admin role.

```typescript
// Protected route example
{
  path: '/forum',
  component: ForumList,
  meta: { requiresAuth: true }
}

// Admin route example
{
  path: '/admin',
  component: AdminDashboard,
  meta: { requiresAuth: true, requiresAdmin: true }
}
```

### Navigation Guard

The router automatically checks authentication status before each navigation:

```typescript
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')  // Redirect to login
    return
  }
  
  if (to.meta.requiresAdmin && !authStore.isAdmin()) {
    next('/forum')  // Redirect to forum
    return
  }
  
  next()
})
```

## WebSocket Chat

### Connection Flow

1. User enters username in chat room
2. `chatStore.connect(username)` is called
3. SockJS connection established to `http://localhost:8080/im/ws`
4. STOMP client subscribes to:
   - `/topic/public` - Public messages
   - `/user/queue/private` - Private messages
5. JOIN message is sent to announce user entry

### Message Types

```typescript
type MessageType = 'CHAT' | 'JOIN' | 'LEAVE' | 'PRIVATE'

interface ChatMessage {
  type: MessageType
  sender: string
  receiver?: string    // Only for PRIVATE messages
  content: string
  timestamp?: string
}
```

### Sending Messages

```typescript
// Public message
chatStore.sendPublicMessage('Hello everyone!')

// Private message
chatStore.sendPrivateMessage('user123', 'Hi there!')
```

## TypeScript Types

All types are defined in `src/types/index.ts`. Here are the main ones:

### Authentication Types
```typescript
interface AccountDTO {
  account: string
  password: string
}

interface RoleVo {
  token: string
  role: number
}
```

### Forum Types
```typescript
interface Post {
  id: number
  title: string
  description: string
  anonymity: boolean
  userId: number
  username?: string
  createTime: string
  updateTime: string
  likedTimes: number
  isLiked?: boolean
}

interface ReplyVO {
  id: number
  postId: number
  userId: number
  username: string
  content: string
  parentId?: number
  createTime: string
  likedTimes: number
  isLiked?: boolean
  children?: ReplyVO[]
}
```

### Pagination Types
```typescript
interface PageDTO {
  pageNum: number
  pageSize: number
}

interface PageVO<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
  pages: number
}
```

## Configuration

### Environment Variables

Create `.env.development` and `.env.production`:

```bash
# Development
VITE_API_BASE_URL=http://localhost:8080

# Production
VITE_API_BASE_URL=https://your-api-domain.com
```

Access in code:
```typescript
const apiUrl = import.meta.env.VITE_API_BASE_URL
```

### Vite Configuration

The `vite.config.ts` includes:
- Path alias: `@` -> `./src`
- API proxy for development
- Port configuration (5173)

```typescript
export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

## Development Tips

### Hot Module Replacement (HMR)

Vite provides instant HMR. Changes to `.vue`, `.ts`, and `.css` files are reflected immediately without full page reload.

### TypeScript Strictness

The project uses strict TypeScript settings. Always:
- Define types for function parameters and return values
- Use interfaces from `src/types/index.ts`
- Avoid `any` type

### Component Best Practices

1. **Use Composition API with `<script setup>`**
```vue
<script setup lang="ts">
import { ref } from 'vue'
const count = ref(0)
</script>
```

2. **Type your props and emits**
```typescript
interface Props {
  title: string
  count?: number
}
const props = defineProps<Props>()
```

3. **Use Element Plus components**
```vue
<el-button type="primary" @click="handleClick">
  Click Me
</el-button>
```

### Error Handling

API errors are automatically handled by Axios interceptors:
- Network errors show "Network error" toast
- 401 errors redirect to login
- 403 errors show "Access denied"
- Other errors display server message

Manual error handling:
```typescript
try {
  await postApi.createPost(data)
  ElMessage.success('Post created')
} catch (error) {
  // Error already shown by interceptor
  console.error('Failed to create post:', error)
}
```

## Testing

### Manual Testing Checklist

1. **Authentication**
   - [ ] Register new account
   - [ ] Login as user
   - [ ] Login as admin
   - [ ] Logout

2. **Forum**
   - [ ] List posts
   - [ ] Create post
   - [ ] View post detail
   - [ ] Add reply
   - [ ] Like/unlike post
   - [ ] Delete own post

3. **Referral**
   - [ ] Browse referrals
   - [ ] Submit referral
   - [ ] View my referrals
   - [ ] Delete referral

4. **Chat**
   - [ ] Connect to chat
   - [ ] Send messages
   - [ ] View online users
   - [ ] Disconnect

5. **Admin**
   - [ ] View pending referrals
   - [ ] Approve referral
   - [ ] Reject referral

## Troubleshooting

### Build Errors

**Problem**: TypeScript errors during build
```
Solution: Run `npm run build` to see detailed errors
Ensure all types are properly imported and defined
```

**Problem**: Module not found
```
Solution: Check path aliases in tsconfig.app.json
Verify imports use '@/' for src directory
```

### Runtime Errors

**Problem**: API calls fail with CORS error
```
Solution: Ensure backend allows CORS from http://localhost:5173
Or use Vite proxy configuration
```

**Problem**: WebSocket connection fails
```
Solution: Check backend WebSocket endpoint is available
Verify URL in chat.ts store (http://localhost:8080/im/ws)
```

**Problem**: "Not authenticated" errors
```
Solution: Clear localStorage and login again
Check token is being sent in Authorization header
```

## Performance Optimization

### Lazy Loading Routes

Routes are already configured with lazy loading:
```typescript
{
  path: '/forum',
  component: () => import('@/views/forum/ForumList.vue')
}
```

### Component Code Splitting

Large components can be lazy loaded:
```typescript
const HeavyComponent = defineAsyncComponent(
  () => import('./HeavyComponent.vue')
)
```

### Image Optimization

Place images in `public/` for direct access or `src/assets/` to be processed by Vite.

## Deployment

### Production Build

```bash
npm run build
```

Output is in `dist/` directory.

### Deployment Options

1. **Static Hosting** (Netlify, Vercel, GitHub Pages)
   - Build the project
   - Upload `dist/` directory
   - Configure environment variables

2. **Docker**
```dockerfile
FROM node:16 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

3. **Nginx Configuration**
```nginx
server {
  listen 80;
  server_name your-domain.com;
  
  root /var/www/html;
  index index.html;
  
  location / {
    try_files $uri $uri/ /index.html;
  }
  
  location /api {
    proxy_pass http://localhost:8080;
  }
}
```

## Contributing

1. Create a feature branch
2. Make changes following the code style
3. Test thoroughly
4. Build to ensure no errors
5. Submit pull request

## License

MIT License
