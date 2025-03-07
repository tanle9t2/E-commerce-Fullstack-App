import { useMutation, useQueryClient } from '@tanstack/react-query';
import { loginUser as loginApi } from '../../services/apiAuthentication';
import { useNavigate } from 'react-router-dom';

import { setAccessToken } from '../../services/api';
import { setAuth } from '../../utils/helper';
import { useAuthContext } from '../../context/AuthContext';


export function useLogin() {
  const { handleLogin } = useAuthContext()
  const navigate = useNavigate();
  const { mutate: login, isLoading } = useMutation({
    mutationFn: ({ username, password }) => loginApi({ username, password }),
    onSuccess: (res) => {
      const { accessToken, refreshToken, userInfor } = res.data;
      setAccessToken(accessToken)
      handleLogin({ user: userInfor })
      setAuth({ accessToken, refreshToken })
      navigate('/')

    },
  });

  return { login, isLoading };
}
