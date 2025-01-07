import { useState } from "react";
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
function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
//   const { login , isLoading } = useLogin();

  function handleSubmit(e) {
    // e.preventDefault();
    // if (!email || !password) return;
    // login(
    //   { email, password },
    //   {
    //     onSettled: () => {
    //       setEmail("");
    //       setPassword("");
    //     },
    //   }
    // );
  }

  return (
    <Form onSubmit={handleSubmit}>
        <FormRowVertical>
            <h1 className="text-4xl">Đăng nhập</h1>
        </FormRowVertical>
      <FormRowVertical label="Email">
        <Input
            width="340px"
          type="email"
          id="email"
          // This makes this form better for password managers
          autoComplete="username"
          value={email}
          placeholder="Email"
          onChange={(e) => setEmail(e.target.value)}
        //   disabled={isLoading}
        />
      </FormRowVertical>

      <FormRowVertical label="Mật khẩu">
        <Input
            width="340px"
          type="password"
          id="password"
          autoComplete="current-password"
          placeholder="Mật khẩu"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        //   disabled={isLoading}
        />
      </FormRowVertical>
      <FormRowVertical>
        {/* <Button size="large" disabled={isLoading}> 
          {!isLoading ? "Log in" : <SpinnerMini />}
        </Button> */}
          <Button size="large"> 
           Đăng nhập
        </Button>
      </FormRowVertical>
      <FormRowVertical>
        <a className="text-blue-600" href="#">Quên mật khẩu</a>
      </FormRowVertical>
      <FormRowVertical className>
        <StyledLoginSocical>
            <StyledLineBreak/>
            <StyledWord className="ml2 mr-2">Hoặc</StyledWord>
            <StyledLineBreak/>
        </StyledLoginSocical>
        <ButtonGroup>
        <Button variation = "secondary" size="medium"> 
          <ButtonIcon>
            <MdFacebook/>
        </ButtonIcon>
        <ButtonText>
            Facebook
        </ButtonText>
        </Button>
        <Button variation = "secondary" size="medium"> 
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
    </Form>
  );
}

export default LoginForm;
