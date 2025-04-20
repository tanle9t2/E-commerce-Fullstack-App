import { useEffect, useState } from "react";
import Button from "../../ui/Button";
import ButtonIcon from "../../ui/ButtonIcon";
import ButtonText from "../../ui/ButtonText";
import ButtonGroup from "../../ui/ButtonGroup";

import Form from "../../ui/Form";
import Input from "../../ui/Input";
import FormRowVertical from "../../ui/FormRowVertical";
import { useLogin } from "./useLogin";
import SpinnerMini from "../../ui/SpinnerMini";
import { MdFacebook } from "react-icons/md";
import styled from "styled-components";
import { FcGoogle } from "react-icons/fc";
import { useForm } from "react-hook-form";
import { useAuthContext } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
const StyledLineBreak = styled.div`
    background-color: #dbdbdb;
    flex: 1;
    height: 1px;
    width: 100%;

`;
const StyledLoginSocical = styled.div`
    display:flex;
    align-items:center;
`
const StyledWord = styled.div`
    color:var(--color-grey-800);
    margin:0 10px;
`;
const CustomForm = styled(Form)`
  position:absolute;
`
const CustomButton = styled(Button)`
  width:100%;
`
function LoginForm() {
  const { login, isLoading } = useLogin();
  const { register, handleSubmit, formState, getValues, reset } = useForm();
  const { errors } = formState;
  const navigate = useNavigate()
  // const { isAuthenticated } = useAuthContext();
  const [loginError, setLoginError] = useState("");
  // useEffect(() => {
  //   if (isAuthenticated()) {
  //     navigate('/');  // Redirect to the home page if the user is authenticated
  //   }
  // }, [isAuthenticated, navigate]);
  function onSubmit() {
    const username = getValues().username;
    const password = getValues().password;
    if (!username || !password) return;
    login(
      { username, password },
      {
        onError: (err) => {
          setLoginError(err.message);
        },
      }
    );
  }

  return (
    <CustomForm onSubmit={handleSubmit(onSubmit)}>
      <FormRowVertical>
        <h1 className="text-4xl">Đăng nhập</h1>
      </FormRowVertical>
      <FormRowVertical error={errors?.username?.message} label="Username">
        <Input
          width="340px"
          id="email"
          // This makes this form better for password managers
          autoComplete="username"
          placeholder="Email/Số điện thoại/Tên đăng nhập"
          disabled={isLoading}
          {...register("username", {
            required: "This field is required",
          })}
        />

      </FormRowVertical>

      <FormRowVertical error={errors?.password?.message} label="Mật khẩu">
        <Input
          width="340px"
          type="password"
          id="password"
          autoComplete="current-password"
          placeholder="Mật khẩu"
          disabled={isLoading}
          {...register("password", {
            required: "This field is required",
          })}
        />
      </FormRowVertical>
      <FormRowVertical error={loginError}>
        <CustomButton variation="primary" disabled={isLoading}>
          {!isLoading ? "Đăng nhập" : <SpinnerMini />}
        </CustomButton>
      </FormRowVertical>
      <FormRowVertical>
        <a className="text-blue-600" href="#">Quên mật khẩu</a>
      </FormRowVertical>
      <FormRowVertical className>
        <StyledLoginSocical>
          <StyledLineBreak />
          <StyledWord className="ml2 mr-2">Hoặc</StyledWord>
          <StyledLineBreak />
        </StyledLoginSocical>
        <ButtonGroup>
          <Button variation="secondary" size="medium">
            <ButtonIcon>
              <MdFacebook />
            </ButtonIcon>
            <ButtonText>
              Facebook
            </ButtonText>
          </Button>
          <Button variation="secondary" size="medium">
            <ButtonIcon>
              <FcGoogle />
            </ButtonIcon>
            <ButtonText>
              Google
            </ButtonText>
          </Button>
        </ButtonGroup>
      </FormRowVertical>
      <FormRowVertical  >
        <a className="text-center" href="#">Đăng ký</a>
      </FormRowVertical>
    </CustomForm>
  );
}

export default LoginForm;
