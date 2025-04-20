import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Header from "../../ui/Header";
import Heading from "../../ui/Heading";
import Form from "../../ui/Form";
import FormRowVertical from "../../ui/FormRowVertical";
import Button from "../../ui/Button";
import { useForm } from "react-hook-form";
import { useChangePassword } from "./useChangePassword";
import SpinnerMini from "../../ui/SpinnerMini"
const Container = styled.div`
    width:100%;
  padding: 20px;
  background-color:var(--color-white);
`;

const Title = styled.h2`
  font-size: 2.5rem;
  margin-bottom: 10px;
  text-decoration:bold;
`;

const Subtitle = styled.p`
  font-size: 1.6rem;
  color: #666;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  
  margin: 10px 0;
`;

const Label = styled.label`
  text-align: left;
  font-weight: bold;
  margin-bottom: 5px;
`;

const InputContainer = styled.div`
  display: flex;
  align-items: center;
  position: relative;
`;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const EyeIcon = styled.span`
  position: absolute;
  right: 10px;
  cursor: pointer;
`;

const CustomButton = styled(Button)`
  width: 100%;
  border: none !important;

  &:disabled {
    background: #f5c6c6;
    cursor: not-allowed;
  }
`;
const CustomForm = styled(Form)`
    position:relative;
    border:none;
    width:50%;
    font-size:1.8rem;
`
const strongPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+=[\]{};:'",.<>?/|\\-])[A-Za-z\d!@#$%^&*()_+=[\]{};:'",.<>?/|\\-]{8,16}$/;
function PasswordChange() {

  const [showPassword, setShowPassword] = useState(false);
  const { register, handleSubmit, formState, getValues, reset, watch } = useForm();
  const [inputErrors, setInputErrors] = useState({});
  const togglePasswordVisibility = () => setShowPassword(!showPassword);
  const { isLoading, changePassword } = useChangePassword()

  const password = watch("password");
  const confirmPassword = watch("confirmPassword");
  // Capture errors on every input change
  useEffect(() => {
    // Clear previous timeout
    const timeout = setTimeout(() => {
      if (!password) return;
      if (!strongPasswordRegex.test(password)) {
        const passwordError = {
          message: "Mật khẩu phải dài 8-16 ký tự, chứa ít nhất một ký tự viết hoa và một ký tự viết thường và chỉ bao gồm các chữ cái, số hoặc dấu câu thông thường"
        };
        setInputErrors(current => ({ ...current, passwordError }));
      } else {
        setInputErrors(current => {
          const updatedErrors = { ...current };
          delete updatedErrors.passwordError; // Remove the error if password is valid
          return updatedErrors;
        });
      }
      if (confirmPassword !== password && confirmPassword) {
        const confirmPasswordError = {
          message: "Mật khẩu và Mật khẩu xác nhận không giống nhau"
        };
        setInputErrors(current => ({ ...current, confirmPasswordError }));
      } else {
        setInputErrors(current => {
          const updatedErrors = { ...current };
          delete updatedErrors.confirmPasswordError; // Remove the error if password is valid
          return updatedErrors;
        });
      }
    }, 500); // Delay in milliseconds (500ms)
    return () => clearTimeout(timeout);
  }, [password, confirmPassword]); // Dependency array
  function onSubmit({ oldPassword, password, confirmPassword }) {
    changePassword({ oldPassword, newPassword: password, confirmPassword }, {
      onSuccess: () => reset()
    })
  }
  return (
    <Container>
      <div>
        <Title>Đổi mật khẩu</Title>
        <Subtitle >Để bảo mật tài khoản, vui lòng không chia sẻ mật khẩu cho người khác</Subtitle>
      </div>

      <div className="flex justify-center">
        <CustomForm onSubmit={handleSubmit(onSubmit)}>
          <FormRowVertical label={"Mật khẩu cũ"}>
            <InputContainer>
              <Input
                id="oldPassword"
                type={showPassword ? "text" : "password"}
                disabled={isLoading}
                {...register("oldPassword", {
                  required: "This field is required",
                })}
              />
              <EyeIcon onClick={togglePasswordVisibility}>👁️</EyeIcon>
            </InputContainer>
          </FormRowVertical>
          <FormRowVertical label={"Mật khẩu mới"} error={inputErrors?.passwordError?.message}>
            <InputContainer>
              <Input
                id="password"
                type={showPassword ? "text" : "password"}
                disabled={isLoading}
                {...register("password", {
                  required: "This field is required",
                  minLength: {
                    value: 8,
                    message: "Password needs a minimum of 8 characters",
                  },
                })}
              />
              <EyeIcon onClick={togglePasswordVisibility}>👁️</EyeIcon>
            </InputContainer>
          </FormRowVertical>

          <FormRowVertical label={"Xác nhận mật khẩu"} error={inputErrors?.confirmPasswordError?.message}>
            <InputContainer>
              <Input
                id="confirmPassword"
                type={showPassword ? "text" : "password"}
                disabled={isLoading}
                {...register("confirmPassword", {
                  required: "This field is required",
                 
                })}
              />
              <EyeIcon onClick={togglePasswordVisibility}>👁️</EyeIcon>
            </InputContainer>
          </FormRowVertical>
          <CustomButton disabled={isLoading || !getValues().oldPassword || !password || password !== confirmPassword}>{isLoading ? <SpinnerMini /> : " Xác Nhận"}</CustomButton>
        </CustomForm>
      </div>
    </Container>
  );
};

export default PasswordChange;
