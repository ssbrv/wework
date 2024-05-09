import { Button, PasswordInput, Text, TextInput } from "@mantine/core";
import PreAuth from "../../components/PreAuth/PreAuth";

const RegisterPage = (): JSX.Element => {
  return (
    <PreAuth>
      <TextInput
        variant="filled"
        radius="md"
        size="md"
        placeholder="First Name"
      />
      <TextInput
        variant="filled"
        radius="md"
        size="md"
        placeholder="Last Name"
      />
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
      <PasswordInput
        variant="filled"
        radius="md"
        size="md"
        placeholder="Repeat password"
      />
      <Button radius="md" size="md">
        Register
      </Button>
      <div className="flex justify-between">
        <Text className="italic">Already have an account?</Text>
        <a className="text-action" href="/login">
          Log in
        </a>
      </div>
    </PreAuth>
  );
};

export default RegisterPage;
