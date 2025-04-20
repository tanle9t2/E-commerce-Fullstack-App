import { useForm } from "react-hook-form";
import Button from "../../ui/Button";
import Form from "../../ui/Form";
import FormRowVertical from "../../ui/FormRowVertical";
import Input from "../../ui/Input";
// import { useSignUp } from "./useSignUp";
import SpinnerMini from "../../ui/SpinnerMini";
import styled from "styled-components";
import ButtonGroup from "../../ui/ButtonGroup";
import FormRow from "../../ui/FormRow"
import ButtonIcon from "../../ui/ButtonIcon";
import { MdFacebook } from "react-icons/md";
import ButtonText from "../../ui/ButtonText";
import { FcGoogle } from "react-icons/fc";
import { useState } from "react";
import { useSendOtp } from "./useSendOtp";
import { validateEmail } from "../../utils/helper";
import EmailStep from "./EmailStep";
import OtpStep from "./OtpStep";
import SignUpDetailStep from "./SignUpDetailStep";
import { useVerifyOtp } from "./useVerifyOtp";
import { useSignUp } from "./useSignUp";
// Email regex: /\S+@\S+\.\S+/


function SignupForm() {
  const { register, formState, handleSubmit, getValues, reset, setError } = useForm();
  const { errors } = formState;

  const [step, setStep] = useState(1);
  const [email, setEmail] = useState("");
  const [isOtpVerified, setIsOtpVerified] = useState(false);

  const { sendOtp, isLoading: loadingSend } = useSendOtp();
  const { verifyOtp, isLoading: loadingVerify } = useVerifyOtp();
  const { isLoading: loadingSignUp, signUp } = useSignUp()

  // Step 1: Send OTP
  function handleEmailSubmit({ toEmail }) {
    sendOtp(
      { toEmail },
      {
        onSuccess: () => {
          setEmail(toEmail);
          setStep(2);
        },
        onError: (error) => setError("toEmail", { type: "manual", message: error.message }),
      }
    );
  }

  // Step 2: Verify OTP
  function handleOtpSubmit({ otp }) {
    verifyOtp(
      { otp },
      {
        onSuccess: () => {
          setIsOtpVerified(true);
          setStep(3);
        },
        onError: (error) => setError("otp", { type: "manual", message: error.message }),
      }
    );
  }

  // Step 3: Final Signup Submission
  function handleSignupSubmit({ firstName, lastName, username, password, phoneNumber }) {
    console.log(firstName, lastName, username, password, phoneNumber)
    if (!isOtpVerified) {
      setError("otp", { type: "manual", message: "Please verify your email first" });
      return;
    }
    signUp({ firstName, lastName, username, password, phoneNumber, email }, {
      onError: (error) => setError("username", { type: "manual", message: error.message }),
    })
  }

  return (
    <>
      {step === 1 && <EmailStep onSubmit={handleSubmit(handleEmailSubmit)}
        errors={errors} loading={loadingSend} register={register} />}
      {step === 2 && <OtpStep onSubmit={handleSubmit(handleOtpSubmit)} errors={errors} loading={loadingVerify} register={register} resendOtp={() => handleEmailSubmit({ toEmail: email })} />}
      {step === 3 && <SignUpDetailStep isLoading={loadingSignUp} onSubmit={handleSubmit(handleSignupSubmit)} errors={errors} register={register} getValues={getValues} reset={reset} />}
    </>
  );
}

export default SignupForm;
