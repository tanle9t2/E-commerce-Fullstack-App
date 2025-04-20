import React, { useEffect, useRef, useState } from 'react';
import styled, { keyframes } from 'styled-components';
import Input from '../../ui/Input';
import Avatar from '../../ui/Avatar';
import TruncateText from '../../ui/TruncateText';
import SockJS from "sockjs-client/dist/sockjs";
import { Client } from "@stomp/stompjs";
import { useUserChats } from './useUserChats';
import { getAuth } from '../../utils/helper';
import { useAuthContext } from '../../context/AuthContext';
import Message from './Message';
import { useGetMessage } from './useGetMessage';
import { useChatContext } from '../../context/ChatContext';
const StyledChat = styled.div`
    position:fixed;
    bottom:0;
    right:0;
`// Define the scaling animation
const scaleIn = keyframes`
  from {
    transform: scale(0);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
`;

const scaleOut = keyframes`
  from {
    transform: scale(1);
    opacity: 1;
  }
  to {
    transform: scale(0);
    opacity: 0;
  }
`;

const ChatButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  font-size: 16px;
  color: #ff4444;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  outline: none;

  &:hover {
    background-color: #f5f5f5;
  }
`;

const ChatIcon = styled.span`
  background-color: #ff4444;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  margin-right: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
`;

const ChatContainer = styled.div`
  position: fixed;
  right: 0;
  bottom: 0;
  width: 642px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transform-origin: bottom right; 
  animation: ${({ isOpen }) => (isOpen ? scaleIn : scaleOut)} 0.3s ease-in-out forwards;
  display: ${({ isOpen }) => (isOpen ? 'block' : 'none')}; /* Control visibility */
  z-index: 1000;
`;

const ChatHeader = styled.div`
  background-color: #ff4444;
  color: white;
  padding: 10px 30px;
  text-align: center;
  display:flex;
  justify-content:space-between;
`;

const ChatSearch = styled.div`
  padding: 10px;
  border-bottom: 1px solid #ddd;
`;

const ChatMessages = styled.div`
  height:400px;
  overflow-y: auto;
`;

const Recipient = styled.div`
  display: flex;
  padding:5px;
  align-items: center;
  margin-bottom: 10px;
  &:hover {
    background-color: rgba(0, 0, 0, 0.08);
    transform: translateY(-1px); /* Slight lift effect */
  }
  background-color:${(props) => props.isActive ? ' rgba(0, 0, 0, 0.08)' : "none"};
`;



const MessageText = styled.div`
  font-size: 14px;
`;

const ChatInputArea = styled.div`
  position:relative;
  display:flex;
  flex-direction:column;
  justify-content:space-between;
  border-top: 1px solid #ddd;
  gap: 10px;
`;


const SendButton = styled.button`
  padding: 5px 10px;
  background-color: #ff4444;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #ff6655;
  }
`;
const CloseButton = styled.button`
  background: none;
  border: none;
  color: white;
  font-size: 16px;
  cursor: pointer;
  padding: 0 10px;

  &:hover {
    color: #ccc;
  }
`;
const ChatMain = styled.div`
    display:grid;
    grid-template-columns:0.3fr 1fr;
`
const ChatContent = styled.div`
    flex: 1;
    max-height:400px;
    position:relative;
    overflow-y:auto;
`
const ChatOperator = styled.div`
    display:flex;
    width:100%;
`
const ChatSelect = styled.div`
   background-color: var(--color-grey-50);
   height:100%;
   display:flex;
   flex-direction:column;
   align-items:center;
   justify-content:center;
`
const ChatLeft = styled.div`
  border-right:1px solid var(--color-grey-50);
`
const HeaderChatContent = styled.div`

  width: 100%;
  padding:10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`
const WS_URL = `http://localhost:8080/ecommerce-server/api/v1/ws`
// Main component
function Chat() {
  const { auth } = useAuthContext();
  const { isChatOpen, setIsChatOpen, selectedUser, messages, setMessages, stompClient, handleSelectedUser } = useChatContext();
  const { isLoading, userChat } = useUserChats()
  const { loadingMessages, getMessage } = useGetMessage()
  const [newMessage, setNewMessage] = useState('');
  const messagesEndRef = useRef(null);
  useEffect(() => {
    console.log(messagesEndRef.current)
    messagesEndRef.current?.scrollTo({
      top: messagesEndRef.current.scrollHeight,
      behavior: "smooth"
    });
  }, [messages]);
  if (isLoading) return
  const sendMessage = (content) => {
    if (stompClient && selectedUser) {
      stompClient.publish({
        destination: `/app/chat/${auth.id}/${selectedUser.recipientId}`,
        body: JSON.stringify({ content }),
      });
    }
  };

  const toggleChat = () => {
    setIsChatOpen(!isChatOpen);
  };

  const closeChat = () => {
    setIsChatOpen(false);
  };
  const handleSendMessage = () => {
    if (newMessage.trim()) {
      sendMessage(newMessage)
      setMessages([...messages, { id: Date.now(), senderId: auth.id, content: newMessage, timestamp: Date.now() }]);
      setNewMessage('');
    }
  };
  function hanleSelected(userId, fullName) {
    getMessage(userId, {
      onSuccess: (data) => {
        handleSelectedUser(userId, fullName)
        setMessages(data)
      }
    })

  }
  return (
    <StyledChat>
      <ChatButton onClick={toggleChat}>
        <ChatIcon>🙂</ChatIcon> Chat
      </ChatButton>

      <ChatContainer isOpen={isChatOpen}>
        <ChatHeader>
          Chat
          <CloseButton onClick={closeChat}>×</CloseButton>
        </ChatHeader>

        <ChatMain>
          <ChatLeft>
            <ChatSearch>
              <input type="text" placeholder="Tìm theo tên khách hàng" />
            </ChatSearch>
            <ChatMessages>
              {!userChat.length ? null : userChat.map(({ userId, firstName, lastName, avtUrl, username }) => (
                <Recipient isActive={userId === selectedUser?.recipientId} onClick={() => hanleSelected(userId, `${firstName} ${lastName}`)} key={userId}>
                  <Avatar url={avtUrl} width={30} height={30} />
                  <MessageText>
                    <div className='flex'>
                      <TruncateText width={"90px"}>{`${firstName} ${lastName}`}</TruncateText>
                      {/* <span className='ml-5' />{msg.date} */}
                    </div>
                    {/* <TruncateText width={"80px"}>{msg.text}</TruncateText> */}

                  </MessageText>
                </Recipient>
              ))}
            </ChatMessages>
          </ChatLeft>

          <ChatInputArea >
            {selectedUser ? <>
              {!loadingMessages && <ChatContent ref={messagesEndRef}>
                <HeaderChatContent>{selectedUser.fullName}</HeaderChatContent>
                {messages
                  .map(({ id, senderId, timestamp, content }) => (
                    <Message key={id} content={content} timestamp={timestamp} isSender={senderId === auth.id} />
                  ))}
              </ChatContent>}
              <ChatOperator>
                <Input
                  value={newMessage}
                  onChange={(e) => setNewMessage(e.target.value)}
                  placeholder="Type your message..."
                />
                <SendButton onClick={() => handleSendMessage()}>Gửi</SendButton>
              </ChatOperator>
            </>
              :
              <ChatSelect>
                <i class="GHUxSkxNuJ t6aX8KMaL1"><svg width="301" height="180" viewBox="0 0 301 180" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M4.5 162C4.5 160.895 5.39543 160 6.5 160H282.5C283.605 160 284.5 160.895 284.5 162C284.5 163.105 283.605 164 282.5 164H6.5C5.39543 164 4.5 163.105 4.5 162Z" fill="#666666"></path><path d="M69.6355 28.0653C70.1235 21.8195 75.3341 17 81.5991 17H239.627C246.585 17 252.085 22.9 251.597 29.8417L243.5 145H60.5L69.6355 28.0653Z" fill="#B7B7B7"></path><path d="M78.2114 33.6879C78.3743 31.6062 80.1111 30 82.1992 30H237.212C239.531 30 241.363 31.9648 241.202 34.2776L233.5 145H69.5L78.2114 33.6879Z" fill="white"></path><path d="M56.5 148H243.5L243.171 149.973C242.207 155.759 237.201 160 231.334 160H56.5V148Z" fill="#666666"></path><path d="M27.5 150.4C27.5 149.075 28.5745 148 29.9 148H221.5C221.5 154.627 216.127 160 209.5 160H37.1C31.7981 160 27.5 155.702 27.5 150.4Z" fill="#B7B7B7"></path><path d="M96.5 148H152.5C152.5 151.866 149.366 155 145.5 155H103.5C99.634 155 96.5 151.866 96.5 148Z" fill="#666666"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M98.0769 44C94.933 44 92.3223 46.4267 92.0929 49.5621L89.9709 78.5621C89.7165 82.039 92.4687 85 95.9549 85H176.923C180.067 85 182.677 82.5733 182.907 79.4379L185.029 50.4379C185.283 46.961 182.531 44 179.045 44H98.0769ZM103.5 59.5C103.5 58.6716 104.171 58 105 58H166C166.828 58 167.5 58.6716 167.5 59.5C167.5 60.3284 166.828 61 166 61H105C104.171 61 103.5 60.3284 103.5 59.5ZM102.5 69.5C102.5 68.6716 103.171 68 104 68H141C141.828 68 142.5 68.6716 142.5 69.5C142.5 70.3284 141.828 71 141 71H104C103.171 71 102.5 70.3284 102.5 69.5Z" fill="#2673DD"></path><path d="M90.5 98.5C90.5 97.6716 91.1716 97 92 97H167C167.828 97 168.5 97.6716 168.5 98.5C168.5 99.3284 167.828 100 167 100H92C91.1716 100 90.5 99.3284 90.5 98.5Z" fill="#B7B7B7"></path><path d="M89.5 108.5C89.5 107.672 90.1716 107 91 107H152C152.828 107 153.5 107.672 153.5 108.5C153.5 109.328 152.828 110 152 110H91C90.1716 110 89.5 109.328 89.5 108.5Z" fill="#B7B7B7"></path><path d="M90 117C89.1716 117 88.5 117.672 88.5 118.5C88.5 119.328 89.1716 120 90 120H118C118.828 120 119.5 119.328 119.5 118.5C119.5 117.672 118.828 117 118 117H90Z" fill="#B7B7B7"></path><path d="M202.239 80C198.129 80 194.688 83.1144 194.279 87.204L193.266 97.3377L184.954 100.455C184.273 100.71 184.084 101.584 184.598 102.098L192.045 109.545L190.879 121.204C190.408 125.913 194.107 130 198.839 130H264.614C268.785 130 272.256 126.796 272.589 122.638L275.309 88.638C275.681 83.983 272.004 80 267.334 80H202.239Z" fill="#EE4D2D"></path><path d="M218 104C218 106.209 216.209 108 214 108C211.791 108 210 106.209 210 104C210 101.791 211.791 100 214 100C216.209 100 218 101.791 218 104Z" fill="white"></path><path d="M235 104C235 106.209 233.209 108 231 108C228.791 108 227 106.209 227 104C227 101.791 228.791 100 231 100C233.209 100 235 101.791 235 104Z" fill="white"></path><path d="M249 108C251.209 108 253 106.209 253 104C253 101.791 251.209 100 249 100C246.791 100 245 101.791 245 104C245 106.209 246.791 108 249 108Z" fill="white"></path></svg></i>
                <div class="D_bmite0fe YN8sHjfMB4">Chào mừng bạn đến với Shopee Chat</div>
                <div>Bắt đầu trả lời người mua!</div>
              </ChatSelect>
            }
          </ChatInputArea>
        </ChatMain>
      </ChatContainer>
    </StyledChat>
  );
};

export default Chat;