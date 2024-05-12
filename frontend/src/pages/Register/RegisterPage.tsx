import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import LgCardWithLogoOnGradientBackground from "../../components/LgCardWithLogoOnGradientBackground/LgCardWithLogoOnGradientBackground";
import { useAuth } from "../../AuthProvider";
import AuthService from "../../api/service/AuthService";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import ExceptionHandler from "../../http/exceptionHandler/ExceptionHandler";
import { RegisterRequest } from "../../http/request/RegisterRequest";

const RegisterPage = (): JSX.Element => {
  const { setToken } = useAuth();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
    getValues,
  } = useForm<RegisterRequest>();

  const registerUser = handleSubmit(async (data: RegisterRequest) => {
    await AuthService.register(data, setToken)
      .then(function () {
        navigate("/profile");
      })
      .catch(function (exception) {
        ExceptionHandler.handleException(setError, exception);
      });
  });

  return (
    <LgCardWithLogoOnGradientBackground>
      <TextInput
        {...register("firstName")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="First Name"
        error={errors.firstName?.message}
      />
      <TextInput
        {...register("lastName")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Last Name"
        error={errors.lastName?.message}
      />
      <TextInput
        {...register("username")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Username"
        error={errors.username?.message}
      />
      <PasswordInput
        {...register("password")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Password"
        error={errors.password?.message}
      />
      <PasswordInput
        {...register("repeatPassword", {
          validate: (match) => {
            return match === getValues("password") || "Passwords have to match";
          },
        })}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Repeat password"
        error={errors.repeatPassword?.message}
      />
      <Button radius="md" size="md" type="submit" onClick={registerUser}>
        Register
      </Button>
      <div className="flex justify-between">
        <Text className="italic">Already have an account?</Text>
        <a className="text-action" href="/login">
          Log in
        </a>
      </div>
    </LgCardWithLogoOnGradientBackground>
  );
};

export default RegisterPage;
