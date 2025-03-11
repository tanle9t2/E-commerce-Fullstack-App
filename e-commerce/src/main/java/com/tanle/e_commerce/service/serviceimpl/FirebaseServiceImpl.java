package com.tanle.e_commerce.service.serviceimpl;

import com.google.firebase.database.*;
import com.tanle.e_commerce.dto.ChatMessageDTO;
import com.tanle.e_commerce.entities.ChatMessage;
import com.tanle.e_commerce.entities.ChatRoom;
import com.tanle.e_commerce.mapper.ChatMessageMapper;
import com.tanle.e_commerce.service.FirebaseService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    private final DatabaseReference messagesRef;
    private final DatabaseReference roomsRef;


    @Autowired
    public FirebaseServiceImpl(FirebaseDatabase firebaseDatabase) {
        this.messagesRef = firebaseDatabase.getReference("chat_messages");
        this.roomsRef = firebaseDatabase.getReference("chat_rooms");
    }

    @Override
    public String findChatRoomIdByRecipientAndSender(int senderId, int recipientId) {
        String chatRoomId = generateChatRoomId(senderId, recipientId);
        CompletableFuture<String> future = new CompletableFuture<>();
        roomsRef.orderByChild("chatRoomName")
                .equalTo(chatRoomId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Chat room exists, return its ID
                            future.complete(chatRoomId);
                        } else {
                            // Chat room does not exist, create a new one
                            String newChatRoomId = createChatRoom(senderId, recipientId);
                            future.complete(newChatRoomId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Error fetching chat room: " + error.getMessage());
                        future.completeExceptionally(new RuntimeException("Firebase error"));
                    }
                });

        try {
            return future.get(); // Blocks and waits for the Firebase result
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createChatRoom(int senderId, int recipientId) {
        String chatRoomId = generateChatRoomId(senderId, recipientId);

        ChatRoom senderChatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomName(chatRoomId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientChatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .chatRoomName(chatRoomId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        roomsRef.child(senderChatRoom.getId()).setValueAsync(senderChatRoom);
        roomsRef.child(recipientChatRoom.getId()).setValueAsync(recipientChatRoom);
        return chatRoomId;
    }

    private String generateChatRoomId(int senderId, int recipientId) {
        return senderId < recipientId ? senderId + "_" + recipientId : recipientId + "_" + senderId;
    }

    @Override
    public List<ChatMessage> getChatMessageByRoom(int senderId, int recipientId) {
        String chatId = findChatRoomIdByRecipientAndSender(senderId, recipientId);
        CompletableFuture<List<ChatMessage>> future = new CompletableFuture<>();
        messagesRef.orderByChild("chatId")
                .equalTo(chatId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<ChatMessage> chatMessages = new ArrayList<>();
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                ChatMessage message = snap.getValue(ChatMessage.class);
                                if (message != null) {
                                    chatMessages.add(message);
                                }
                            }
                        }
                        future.complete(chatMessages); // ✅ Complete future with the full lis
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Firebase error: " + error.getMessage());

                    }
                });
        try {
            return future.get()
                    .stream()
                    .sorted(Comparator.comparing(ChatMessage::getTimestamp))
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage, int senderId, int recipientId) {
        String roomName = findChatRoomIdByRecipientAndSender(senderId,recipientId);
        updateChatRoomStatus(roomName, recipientId, true);
        chatMessage.setId(UUID.randomUUID().toString());
        chatMessage.setChatId(roomName);
        chatMessage.setTimestamp(System.currentTimeMillis());
        chatMessage.setSenderId(senderId);
        chatMessage.setRecipientId(recipientId);


        messagesRef.child(chatMessage.getId()).setValueAsync(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatRoom> getAllChatRoomSender(int senderId) {
        CompletableFuture<List<ChatRoom>> future = new CompletableFuture<>();
        roomsRef.orderByChild("senderId").equalTo(senderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<ChatRoom> chatRooms = new ArrayList<>();
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                ChatRoom message = snap.getValue(ChatRoom.class);
                                if (message != null) {
                                    chatRooms.add(message);
                                }
                            }
                        }
                        future.complete(chatRooms); // ✅ Complete future with the full lis
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Firebase error: " + error.getMessage());
                    }
                });
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateChatRoomStatus(String roomName, int userId, boolean isRead) {
        roomsRef.orderByChild("chatId")
                .equalTo(roomName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                ChatRoom chatRoom = snap.getValue(ChatRoom.class);
                                Map<String, Object> updates = new HashMap<>();
                                updates.put("isRead", isRead);
                                if (chatRoom.getRecipientId() == userId) {
                                    roomsRef.child(snap.getKey()).updateChildrenAsync(updates);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Firebase error: " + error.getMessage());

                    }
                });
    }
}
