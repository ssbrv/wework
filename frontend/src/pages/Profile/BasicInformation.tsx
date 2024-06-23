import { Button, LoadingOverlay, NativeSelect, TextInput } from "@mantine/core";
import { useUser } from "../../hooks/UserProvider";
import { useDisclosure } from "@mantine/hooks";
import { useException } from "../../hooks/ExceptionProvider";
import { useForm } from "react-hook-form";
import { UpdateUserRequest } from "../../http/request/UpdateUserRequest";
import { useEffect } from "react";
import { goodNotification } from "../../components/Notifications/Notifications";

const BasicInformation = (): JSX.Element => {
  const { user, editBasic } = useUser();

  const [editPressed, { open: pressEdit, close: unpressEdit }] =
    useDisclosure(false);

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
    reset(user);
  }, [user]);

  const handleEdit = handleSubmit(
    async (editBasicRequest: UpdateUserRequest) => {
      await editBasic(editBasicRequest)
        .then(function () {
          goodNotification("Basic information was edited successfully!");
          unpressEdit();
        })
        .catch(function (exception) {
          handleException(exception, setError, true);
        });
    }
  );

  return (
    <div className="flex-1 card p-m relative gap-s flex flex-col">
      <LoadingOverlay visible={!user} className="rounded z-10" />
      <div className="fnt-md font-bold">Basic information</div>
      <TextInput
        {...key("firstName")}
        size="md"
        radius="md"
        variant="filled"
        readOnly={!editPressed}
        label="First Name"
        styles={{
          input: {
            borderColor: "transparent",
          },
        }}
        error={errors.firstName?.message}
      />
      <TextInput
        {...key("lastName")}
        size="md"
        radius="md"
        variant="filled"
        readOnly={!editPressed}
        label="Last Name"
        styles={{
          input: {
            borderColor: "transparent",
          },
        }}
        error={errors.lastName?.message}
      />
      <NativeSelect
        {...key("sex")}
        size="md"
        radius="md"
        variant="filled"
        disabled={!editPressed}
        data={[
          { label: "Male", value: "MALE" },
          { label: "Female", value: "FEMALE" },
          { label: "-", value: "UNSPECIFIED" },
        ]}
        label="Sex"
        styles={{
          input: {
            borderColor: "transparent",
          },
        }}
        error={errors.sex?.message}
      />
      {!editPressed ? (
        <div className="flex justify-end">
          <Button
            radius="md"
            size="md"
            className="btn-action"
            onClick={pressEdit}
          >
            Edit basic information
          </Button>
        </div>
      ) : (
        <div className="flex justify-end gap-m">
          <Button
            radius="md"
            size="md"
            variant="outline"
            onClick={() => {
              reset(user);
              unpressEdit();
            }}
          >
            Cancel
          </Button>
          <Button
            radius="md"
            size="md"
            className="btn-attract"
            onClick={handleEdit}
          >
            Save
          </Button>
        </div>
      )}
    </div>
  );
};

export default BasicInformation;
