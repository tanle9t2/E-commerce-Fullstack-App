import { axiosPrivate } from "./api";

export async function getUserChats() {
    try {
        const res = await axiosPrivate.get("/user/chat");

        return res.data;
    } catch (error) {
        console.error("Failed user chat:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getMessages(recipientId) {
    try {
        const res = await axiosPrivate.get(`/messages/${recipientId}`);

        return res.data;
    } catch (error) {
        console.error("Failed user chat:", error);
        throw new Error("Failed getting cart");
    }
}