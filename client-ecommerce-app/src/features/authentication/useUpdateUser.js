import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateUser as updateUserAPI } from "../../services/apiAuthentication";
import toast from "react-hot-toast";
import { useAuthContext } from "../../context/AuthContext";

export function useUpdateUser() {
    const { updateCurrentUser } = useAuthContext();
    const { isLoading, mutate: updateUser } = useMutation({
        mutationFn: ({ userData, image }) => updateUserAPI(userData, image),
        onSuccess: (response) => {
            const { avatar, firstName, lastName, email } = response.data;
            const newData = {};
            if (avatar)
                newData.avatar = avatar;
            if (firstName && lastName)
                newData.fullName = `${firstName} ${lastName}`
            if (email)
                newData.email = email;
            updateCurrentUser(newData)
            toast.success("Cập nhật thông tin thành công")
        },
        onError: (error) => toast.error(error.message)
    })

    return { isLoading, updateUser }
}