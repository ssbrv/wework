import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import PreAuth from "../../components/PreAuth/PreAuth";
import { useAuth } from "../../AuthProvider";
import { AuthRequest } from "../../http/request/AuthRequest";
import AuthService from "../../api/service/AuthService";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import ExceptionHandler from "../../http/exceptionHandler/ExceptionHandler";

const LoginPage = (): JSX.Element => {
  const { setToken } = useAuth();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<AuthRequest>();

  const login = handleSubmit(async (data: AuthRequest) => {
    await AuthService.auth(
      { username: data.username, password: data.password },
      setToken
    )
      .then(function () {
        navigate("/profile");
      })
      .catch(function (exception) {
        ExceptionHandler.handleException(setError, exception);
      });
  });

  return (
    <PreAuth>
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
      <Button radius="md" size="md" type="submit" onClick={login}>
        Log in
      </Button>
      <div className="flex justify-between">
        <Text className="italic">Don't have an account yet?</Text>
        <a className="text-action" href="/register">
          Register
        </a>
      </div>
    </PreAuth>
  );
};

export default LoginPage;
