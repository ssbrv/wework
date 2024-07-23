import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import { AuthRequest } from "../../http/request/AuthRequest";
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { goodNotification } from "../../components/Notifications/Notifications";
import PreAuthCard from "../../components/PreAuthCard/PreAuthCard";
import { login, useMyId } from "../../api/auth/authApi";
import { displayError } from "../../utils/displayError";
import { useEffect } from "react";

const LoginPage = (): JSX.Element => {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<AuthRequest>();

  const { myId } = useMyId();
  const navigate = useNavigate();

  useEffect(() => {
    if (myId) {
      navigate(`/users/${myId}/profile`);
    }
  }, [myId]);

  const loginUser = handleSubmit(async (authRequest: AuthRequest) => {
    await login(authRequest)
      .then(function () {
        goodNotification(
          "Successfully logged in!",
          "Welcome back, " + authRequest.username + "!"
        );
      })
      .catch(function (exception) {
        displayError(exception, setError);
      });
  });

  return (
    <PreAuthCard>
      <form className="flex flex-col gap-m" onSubmit={loginUser}>
        <TextInput
          {...register("username")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Username"
          error={errors.username?.message}
          color="blue"
        />
        <PasswordInput
          {...register("password")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Password"
          error={errors.password?.message}
        />
        <Button radius="md" size="md" type="submit" className="btn-action">
          Log in
        </Button>
        <div className="flex justify-between">
          <Text className="italic">Don't have an account yet?</Text>
          <Link
            className="text-action hover:text-action hover:grayscale-[25%]"
            to="/register"
          >
            Register
          </Link>
        </div>
      </form>
    </PreAuthCard>
  );
};

export default LoginPage;
