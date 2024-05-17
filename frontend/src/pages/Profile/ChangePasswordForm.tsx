import { Button, Group, PasswordInput } from "@mantine/core";
import { useAuth } from "../../hooks/AuthProvider";
import { useExceptionHandler } from "../../hooks/ExceptionHandler";
import { ChangePasswordRequest } from "../../http/request/ChangePasswordRequest";
import { useForm } from "react-hook-form";
import { goodNotification } from "../../components/Notifications/Notifications";

interface Props {
  onClose: () => void;
}

const ChangePasswordForm = ({ onClose }: Props): JSX.Element => {
  const { changePassword } = useAuth();
  const { handleException } = useExceptionHandler();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    getValues,
  } = useForm<ChangePasswordRequest>();

  const handleChangePassword = handleSubmit(
    async (changePasswordRequest: ChangePasswordRequest) => {
      await changePassword(changePasswordRequest)
        .then(function () {
          goodNotification(
            "Password was changed successfully!",
            "And all other devices were logged out successfully too"
          );
        })
        .catch(function (exception) {
          handleException(exception, setError, true);
        });
    }
  );

  return (
    <form className="gap-m flex flex-col p-xs" onSubmit={handleChangePassword}>
      <div className="fnt-md font-bold">Change password form</div>
      <PasswordInput
        {...key("password")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Current password"
        error={errors.password?.message}
      />
      <PasswordInput
        {...key("newPassword")}
        variant="filled"
        radius="md"
        size="md"
        placeholder="New password"
        error={errors.newPassword?.message}
      />
      <PasswordInput
        {...key("repeatNewPassword", {
          validate: (match) => {
            return (
              match === getValues("newPassword") || "Passwords have to match"
            );
          },
        })}
        variant="filled"
        radius="md"
        size="md"
        placeholder="Repeat new password"
        error={errors.repeatNewPassword?.message}
      />

      <Group className="gap-m">
        <Button
          variant="outline"
          size="md"
          radius="md"
          className="flex-1"
          onClick={onClose}
        >
          Cancel
        </Button>
        <Button
          radius="md"
          size="md"
          type="submit"
          className="flex-1 btn-attract"
        >
          Save
        </Button>
      </Group>
    </form>
  );
};

export default ChangePasswordForm;
