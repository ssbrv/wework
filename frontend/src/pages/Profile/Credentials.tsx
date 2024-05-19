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
import { useException } from "../../hooks/ExceptionProvider";
import { useForm } from "react-hook-form";
import { useEffect } from "react";
import { goodNotification } from "../../components/Notifications/Notifications";
import { UpdateUserRequest } from "../../http/request/UpdateUserRequest";

const Credentials = (): JSX.Element => {
  const [
    changePasswordOpened,
    { open: openChangePassword, close: closeChangePassword },
  ] = useDisclosure(false);

  const [
    editUsernamePressed,
    { open: pressEditUsername, close: unpressEditUsername },
  ] = useDisclosure(false);

  const { user, editUsername, isItMe } = useUser();

  const { handleException } = useException();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    reset,
  } = useForm<UpdateUserRequest>();

  function resetForm(): void {
    reset({
      username: user?.username,
      firstName: user?.firstName,
      lastName: user?.lastName,
      sex: user?.sex,
    });
  }

  useEffect(() => {
    resetForm();
  }, [user]);

  const handleEditUsername = handleSubmit(
    async (editUsernameRequest: UpdateUserRequest) => {
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
              resetForm();
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
          className={`btn-action ${isItMe ? "visible" : "hidden"}`}
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
