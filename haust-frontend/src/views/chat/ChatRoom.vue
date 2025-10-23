<template>
  <div class="chat-room">
    <el-card>
      <template #header>
        <div class="header">
          <h2>Chat Room</h2>
          <el-button
            v-if="!chatStore.isConnected"
            type="primary"
            @click="showUsernameDialog = true"
          >
            Connect
          </el-button>
          <el-button
            v-else
            type="danger"
            @click="handleDisconnect"
          >
            Disconnect
          </el-button>
        </div>
      </template>
      
      <div class="chat-container">
        <div class="users-panel">
          <h3>Online Users ({{ chatStore.onlineUsers.length }})</h3>
          <el-scrollbar height="500px">
            <div
              v-for="user in chatStore.onlineUsers"
              :key="user"
              class="user-item"
            >
              <el-icon><User /></el-icon>
              <span>{{ user }}</span>
            </div>
          </el-scrollbar>
        </div>
        
        <div class="messages-panel">
          <el-scrollbar ref="messagesScrollbar" height="500px">
            <div
              v-for="(message, index) in chatStore.messages"
              :key="index"
              class="message-item"
              :class="getMessageClass(message)"
            >
              <div v-if="message.type === 'JOIN' || message.type === 'LEAVE'" class="system-message">
                {{ message.content }}
              </div>
              <div v-else class="chat-message">
                <div class="message-header">
                  <span class="sender">{{ message.sender }}</span>
                  <span class="time">{{ formatTime(message.timestamp) }}</span>
                </div>
                <div class="message-content">{{ message.content }}</div>
              </div>
            </div>
          </el-scrollbar>
          
          <div class="message-input">
            <el-input
              v-model="messageText"
              placeholder="Type a message..."
              :disabled="!chatStore.isConnected"
              @keyup.enter="sendMessage"
            >
              <template #append>
                <el-button
                  :icon="Position"
                  :disabled="!chatStore.isConnected"
                  @click="sendMessage"
                >
                  Send
                </el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </el-card>
    
    <!-- Username Dialog -->
    <el-dialog
      v-model="showUsernameDialog"
      title="Enter Your Username"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-input
        v-model="username"
        placeholder="Enter username"
        @keyup.enter="handleConnect"
      />
      <template #footer>
        <el-button @click="showUsernameDialog = false">Cancel</el-button>
        <el-button type="primary" @click="handleConnect">Connect</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { useChatStore } from '@/store'
import { User, Position } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const chatStore = useChatStore()

const showUsernameDialog = ref(false)
const username = ref('')
const messageText = ref('')
const messagesScrollbar = ref()

const handleConnect = () => {
  if (!username.value.trim()) {
    ElMessage.warning('Please enter a username')
    return
  }
  
  chatStore.connect(username.value)
  showUsernameDialog.value = false
  ElMessage.success('Connected to chat room')
}

const handleDisconnect = () => {
  chatStore.disconnect()
  username.value = ''
  ElMessage.info('Disconnected from chat room')
}

const sendMessage = () => {
  if (!messageText.value.trim()) {
    return
  }
  
  chatStore.sendPublicMessage(messageText.value)
  messageText.value = ''
}

const getMessageClass = (message: any) => {
  if (message.type === 'JOIN' || message.type === 'LEAVE') {
    return 'system'
  }
  return message.sender === chatStore.currentUsername ? 'own' : 'other'
}

const formatTime = (timestamp?: string) => {
  if (!timestamp) return ''
  return new Date(timestamp).toLocaleTimeString()
}

// Auto scroll to bottom when new messages arrive
watch(
  () => chatStore.messages.length,
  () => {
    nextTick(() => {
      if (messagesScrollbar.value) {
        messagesScrollbar.value.setScrollTop(messagesScrollbar.value.wrapRef.scrollHeight)
      }
    })
  }
)
</script>

<style scoped>
.chat-room {
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h2 {
  margin: 0;
}

.chat-container {
  display: flex;
  gap: 20px;
}

.users-panel {
  width: 250px;
  border-right: 1px solid #e4e7ed;
  padding-right: 20px;
}

.users-panel h3 {
  margin-top: 0;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  margin-bottom: 5px;
  border-radius: 4px;
  cursor: pointer;
}

.user-item:hover {
  background-color: #f5f7fa;
}

.messages-panel {
  flex: 1;
}

.message-item {
  margin-bottom: 15px;
}

.system-message {
  text-align: center;
  color: #909399;
  font-size: 14px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.chat-message {
  max-width: 70%;
}

.message-item.own .chat-message {
  margin-left: auto;
  background-color: #409EFF;
  color: white;
  padding: 10px;
  border-radius: 8px;
}

.message-item.other .chat-message {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 8px;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 12px;
}

.message-item.own .message-header {
  color: rgba(255, 255, 255, 0.8);
}

.message-item.other .message-header {
  color: #909399;
}

.sender {
  font-weight: bold;
}

.time {
  font-size: 11px;
}

.message-content {
  line-height: 1.5;
}

.message-input {
  margin-top: 20px;
}
</style>
