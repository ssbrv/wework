import {
  Button,
  LoadingOverlay,
  Modal,
  PasswordInput,
  TextInput,
} from "@mantine/core";
import { useUser } from "../../hooks/UserProvider";
import { useDisclosure } from "@mantine/hooks";
import ChangePasswordForm from "./ChangePasswordForm";
import { useExceptionHandler } from "../../hooks/ExceptionHandler";
import { useForm } from "react-hook-form";
import { EditUsernameRequest } from "../../http/request/EditUsernameRequest";
import { useEffect } from "react";
import { goodNotification } from "../../components/Notifications/Notifications";

const Credentials = (): JSX.Element => {
  const [
    changePasswordOpened,
    { open: openChangePassword, close: closeChangePassword },
  ] = useDisclosure(false);

  const [
    editUsernamePressed,
    { open: pressEditUsername, close: unpressEditUsername },
  ] = useDisclosure(false);

  const { user, editUsername } = useUser();

  const { handleException } = useExceptionHandler();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    reset,
  } = useForm<EditUsernameRequest>();

  useEffect(() => {
    reset({ username: user?.username });
  }, [reset, user?.username]);

  const handleEditUsername = handleSubmit(
    async (editUsernameRequest: EditUsernameRequest) => {
      await editUsername(editUsernameRequest)
        .then(function () {
          goodNotification("Username was edited successfully!");
          unpressEditUsername();
        })
        .catch(function (exception) {
          handleException(exception, setError, true);
        });
    }
  );

  return (
    <div className="flex-1 card p-m relative gap-s flex flex-col justify-between">
      <LoadingOverlay visible={!user} className="rounded z-10" />
      <div className="fnt-md font-bold">Credentials</div>

      <TextInput
        {...key("username")}
        size="md"
        radius="md"
        variant="filled"
        readOnly={!editUsernamePressed}
        label="Username"
        error={errors.username?.message}
        styles={{
          input: {
            borderColor: "transparent",
          },
        }}
      />
      {!editUsernamePressed ? (
        <div className="flex justify-end">
          <Button
            radius="md"
            size="md"
            className="btn-action"
            onClick={pressEditUsername}
          >
            Edit username
          </Button>
        </div>
      ) : (
        <div className="flex justify-end gap-m">
          <Button
            radius="md"
            size="md"
            variant="outline"
            onClick={() => {
              reset({ username: user?.username });
              unpressEditUsername();
            }}
          >
            Cancel
          </Button>
          <Button
            radius="md"
            size="md"
            className="btn-attract"
            onClick={handleEditUsername}
          >
            Save
          </Button>
        </div>
      )}
      <PasswordInput
        size="md"
        radius="md"
        variant="filled"
        disabled
        label="Password"
        defaultValue="blablablabla"
        styles={{
          input: {
            borderColor: "transparent",
          },
        }}
      />
      <div className="flex justify-end">
        <Button
          radius="md"
          size="md"
          className="btn-action"
          onClick={openChangePassword}
        >
          Change password
        </Button>
      </div>

      <Modal
        opened={changePasswordOpened}
        onClose={closeChangePassword}
        centered
        radius="md"
        withCloseButton={false}
      >
        <ChangePasswordForm onClose={closeChangePassword} />
      </Modal>
    </div>
  );
};

export default Credentials;
