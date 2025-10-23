import { defineStore } from 'pinia'
import { ref } from 'vue'
import SockJS from 'sockjs-client'
import { Client, type IMessage } from '@stomp/stompjs'
import type { ChatMessage } from '@/types'
import { ElMessage } from 'element-plus'

export const useChatStore = defineStore('chat', () => {
  const messages = ref<ChatMessage[]>([])
  const onlineUsers = ref<string[]>([])
  const isConnected = ref<boolean>(false)
  const currentUsername = ref<string>('')
  
  let stompClient: Client | null = null

  // Connect to WebSocket
  const connect = (username: string) => {
    const socket = new SockJS('http://localhost:8080/im/ws')
    
    stompClient = new Client({
      webSocketFactory: () => socket as any,
      debug: (str) => {
        console.log('STOMP:', str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    })

    stompClient.onConnect = () => {
      console.log('Connected to WebSocket')
      isConnected.value = true
      currentUsername.value = username

      // Subscribe to public messages
      stompClient!.subscribe('/topic/public', (message: IMessage) => {
        const chatMessage: ChatMessage = JSON.parse(message.body)
        messages.value.push(chatMessage)
        
        // Update online users when someone joins
        if (chatMessage.type === 'JOIN' && chatMessage.sender) {
          if (!onlineUsers.value.includes(chatMessage.sender)) {
            onlineUsers.value.push(chatMessage.sender)
          }
        } else if (chatMessage.type === 'LEAVE' && chatMessage.sender) {
          const index = onlineUsers.value.indexOf(chatMessage.sender)
          if (index > -1) {
            onlineUsers.value.splice(index, 1)
          }
        }
      })

      // Subscribe to private messages
      stompClient!.subscribe('/user/queue/private', (message: IMessage) => {
        const chatMessage: ChatMessage = JSON.parse(message.body)
        messages.value.push(chatMessage)
      })

      // Send JOIN message
      sendMessage({
        type: 'JOIN',
        sender: username,
        content: `${username} joined the chat`
      })
    }

    stompClient.onStompError = (frame) => {
      console.error('STOMP error:', frame)
      ElMessage.error('WebSocket connection error')
      isConnected.value = false
    }

    stompClient.activate()
  }

  // Disconnect from WebSocket
  const disconnect = () => {
    if (stompClient && currentUsername.value) {
      sendMessage({
        type: 'LEAVE',
        sender: currentUsername.value,
        content: `${currentUsername.value} left the chat`
      })
      
      stompClient.deactivate()
      isConnected.value = false
      currentUsername.value = ''
      messages.value = []
      onlineUsers.value = []
    }
  }

  // Send message
  const sendMessage = (message: ChatMessage) => {
    if (stompClient && isConnected.value) {
      const destination = message.type === 'PRIVATE' 
        ? '/app/chat.sendPrivate' 
        : message.type === 'JOIN'
        ? '/app/chat.addUser'
        : '/app/chat.sendMessage'
      
      stompClient.publish({
        destination,
        body: JSON.stringify(message)
      })
    }
  }

  // Send public message
  const sendPublicMessage = (content: string) => {
    sendMessage({
      type: 'CHAT',
      sender: currentUsername.value,
      content
    })
  }

  // Send private message
  const sendPrivateMessage = (receiver: string, content: string) => {
    sendMessage({
      type: 'PRIVATE',
      sender: currentUsername.value,
      receiver,
      content
    })
  }

  return {
    messages,
    onlineUsers,
    isConnected,
    currentUsername,
    connect,
    disconnect,
    sendPublicMessage,
    sendPrivateMessage
  }
})
