import { useQuery } from "@tanstack/react-query";
import { getUserChats } from "../../services/apiChat";
import { useChatContext } from "../../context/ChatContext";

export function useUserChats() {
    const {
        isLoading,
        data: userChat,
        error,

    } = useQuery({
        queryKey: ["userChat"],
        queryFn: getUserChats,
    })

    return { isLoading, userChat, error }
}