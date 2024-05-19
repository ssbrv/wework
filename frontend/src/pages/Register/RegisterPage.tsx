import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import { useAuth } from "../../hooks/AuthProvider";
import { Link, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { RegisterRequest } from "../../http/request/RegisterRequest";
import { useException } from "../../hooks/ExceptionProvider";
import { goodNotification } from "../../components/Notifications/Notifications";
import PreAuthCard from "../../components/PreAuthCard/PreAuthCard";
import { useEffect } from "react";

const RegisterPage = (): JSX.Element => {
  const { register, myId } = useAuth();
  const { handleException } = useException();
  const navigate = useNavigate();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    getValues,
  } = useForm<RegisterRequest>();

  useEffect(() => {
    if (myId) {
      navigate(`/${myId}/profile`);
    }
  }, [myId, navigate]);

  const registerUser = handleSubmit(
    async (registerRequest: RegisterRequest) => {
      await register(registerRequest)
        .then(function () {
          goodNotification(
            "Successfully registrated!",
            "Welcome aboard, " + registerRequest.username + "!"
          );
        })
        .catch(function (exception) {
          handleException(exception, setError);
        });
    }
  );

  return (
    <PreAuthCard>
      <form className="flex flex-col gap-m" onSubmit={registerUser}>
        <TextInput
          {...key("firstName")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="First Name"
          error={errors.firstName?.message}
        />
        <TextInput
          {...key("lastName")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Last Name"
          error={errors.lastName?.message}
        />
        <TextInput
          {...key("username")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Username"
          error={errors.username?.message}
        />
        <PasswordInput
          {...key("password")}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Password"
          error={errors.password?.message}
        />
        <PasswordInput
          {...key("repeatPassword", {
            validate: (match) => {
              return (
                match === getValues("password") || "Passwords have to match"
              );
            },
          })}
          variant="filled"
          radius="md"
          size="md"
          placeholder="Repeat password"
          error={errors.repeatPassword?.message}
        />
        <Button radius="md" size="md" type="submit">
          Register
        </Button>
        <div className="flex justify-between">
          <Text className="italic">Already have an account?</Text>
          <Link
            className="text-action hover:text-action hover:grayscale-[25%]"
            to="/login"
          >
            Log in
          </Link>
        </div>
      </form>
    </PreAuthCard>
  );
};

export default RegisterPage;
