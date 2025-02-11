import { useMutation, useQueryClient } from '@tanstack/react-query';
import { loginUser as loginApi } from '../../services/apiAuthentication';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-hot-toast';
import { useAuthContext } from '../../context/AuthContext';

export function useLogin() {
  const queryClient = useQueryClient();
  const {handleLogin} = useAuthContext();
  const navigate = useNavigate();

  const { mutate: login, isLoading } = useMutation({
    mutationFn: ({ username, password }) => loginApi({ username, password }),
    onSuccess: (res) => {
      const {accessToken,refreshToken,userInfor} = res.data;
      handleLogin(accessToken,refreshToken,userInfor)
      navigate('/')
    },
    onError: (err) => {
      console.log('ERROR', err);
      toast.error(err.message);
    },
  });

  return { login, isLoading };
}
