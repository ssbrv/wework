import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import PreAuth from "../../components/PreAuth/PreAuth";

const LoginPage = (): JSX.Element => {
  return (
    <PreAuth>
      <TextInput
        variant="filled"
        radius="md"
        size="md"
        placeholder="Username"
      />
      <PasswordInput
        variant="filled"
        radius="md"
        size="md"
        placeholder="Password"
      />
      <Button radius="md" size="md">
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
