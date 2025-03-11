import { createContext, useContext, useState } from "react";
import { useGetMessage } from "../features/chat/useGetMessage";
import SockJS from "sockjs-client/dist/sockjs";
import { Client } from "@stomp/stompjs";
import { useAuthContext } from "./AuthContext";
import { getAuth } from "../utils/helper";
const ChatContex = createContext()
const WS_URL = `http://localhost:8080/ecommerce-server/api/v1/ws`
function ChatContextProvider({ children }) {
    const [isChatOpen, setIsChatOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const { auth } = useAuthContext()
    const { accessToken } = getAuth()
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const connectWebSocket = () => {
        const socket = new SockJS(`${WS_URL}?token=${accessToken}`, null, {
            transports: ["websocket", "xhr-streaming", "eventsource", "xhr-polling"], // ✅ Allow fallbacks
            withCredentials: true // ✅ Ensure CORS credentials are allowed
        });
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                // Subscribe to user's private chat
                client.subscribe(`/user/${auth.id}/queue/messages`, onMessageReceived);
                client.subscribe(`/user/public`, onMessageReceived);
            },
        });

        client.activate();
        setStompClient(client);
    };
    const onMessageReceived = (message) => {
        const receivedMessage = JSON.parse(message.body);
        setMessages((prev) => [...prev, receivedMessage]);
    };

    function handleSelectedUser(recipientId, fullName) {
        if (recipientId !== selectedUser?.recipientId) {
            connectWebSocket(recipientId)
            setSelectedUser({ fullName, recipientId })
        }

        else
            setSelectedUser(null)
    }
    return (
        <ChatContex.Provider value={{ isChatOpen, setIsChatOpen, stompClient, messages, setMessages, selectedUser, handleSelectedUser }}>
            {children}
        </ChatContex.Provider>
    );
}
function useChatContext() {
    const context = useContext(ChatContex);
    if (context === undefined)
        throw new Error("AuthContext was used outside of AuthContextProvider");
    return context;
}
export { ChatContextProvider, useChatContext }