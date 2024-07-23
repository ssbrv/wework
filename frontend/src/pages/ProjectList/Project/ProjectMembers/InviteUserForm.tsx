import { useForm } from "react-hook-form";
import api from "../../../../api/api";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { Button, Group, Stepper, TextInput } from "@mantine/core";
import { InviteRequest } from "../../../../http/request/InviteRequest";
import { useProject } from "../../../../hooks/ProjectProvider";
import useSWR from "swr";
import { User } from "../../../../domain/User";
import { getFetcher } from "../../../../api/fetchers";
import { Role } from "../../../../domain/Role";
import { List } from "../../../../components/List/List";
import { useState } from "react";
import { displayError } from "../../../../utils/displayError";

interface Props {
  onClose: () => void;
}

const InviteUserForm = ({ onClose }: Props): JSX.Element => {
  const [active, setActive] = useState(0);
  const nextStep = () =>
    setActive((current) => (current < 2 ? current + 1 : current));
  const prevStep = () =>
    setActive((current) => (current > 0 ? current - 1 : current));

  const { project } = useProject();

  const { data: users } = useSWR<User[]>("users", getFetcher);
  const { data: roles } = useSWR<Role[]>("member-roles", getFetcher);

  const transformedUsers = users?.map((user) => ({
    id: user.id,
    name: user.username,
    fullName: user.firstName + " " + user.lastName,
  }));

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    setValue,
    clearErrors,
  } = useForm<InviteRequest>();

  const inviteUser = handleSubmit(async (inviteRequest: InviteRequest) => {
    if (!project) return;

    inviteRequest.projectId = project.id;
    await api
      .post("memberships", inviteRequest)
      .then(function () {
        goodNotification("User was invited successfully!");
        onClose();
      })
      .catch(function (exception) {
        displayError(exception, setError, true);
      });
  });

  return (
    <form className="gap-m flex flex-col p-xs" onSubmit={inviteUser}>
      <Stepper
        active={active}
        onStepClick={setActive}
        allowNextStepsSelect={false}
        size="lg"
      >
        <Stepper.Step label="First step" description="Select user">
          <List
            title="Users"
            list={transformedUsers}
            itemsPerPage={5}
            wrapInCard={false}
            onItemClick={(user) => {
              setValue("username", user.name);
              clearErrors();
              nextStep();
            }}
            displayAttributes={[
              {
                index: 0,
                attribute: "fullName",
              },
            ]}
          />
        </Stepper.Step>

        <Stepper.Step label="Second step" description="Select role">
          <List
            title="Roles"
            list={roles}
            itemsPerPage={5}
            wrapInCard={false}
            onItemClick={(role) => {
              setValue("roleName", role.name);
              setValue("roleValue", role.value);
              clearErrors();
              nextStep();
            }}
            displayAttributes={[
              {
                index: 0,
                attribute: "description",
              },
            ]}
          />
        </Stepper.Step>

        <Stepper.Completed>
          <div className="flex flex-col gap-m">
            <TextInput
              {...key("username")}
              size="md"
              radius="md"
              variant="filled"
              readOnly
              label="Username"
              styles={{
                input: {
                  borderColor: "transparent",
                },
              }}
              error={errors.username?.message}
            />
            <TextInput
              {...key("roleName")}
              size="md"
              radius="md"
              variant="filled"
              readOnly
              label="Role"
              styles={{
                input: {
                  borderColor: "transparent",
                },
              }}
              error={errors.roleValue?.message}
            />
          </div>
        </Stepper.Completed>
      </Stepper>

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
        {active !== 0 && (
          <Button
            variant="outline"
            size="md"
            radius="md"
            className="flex-1"
            onClick={() => {
              prevStep();
              clearErrors();
            }}
          >
            Go back
          </Button>
        )}
        {active === 2 && (
          <Button
            radius="md"
            size="md"
            type="submit"
            className="flex-1 btn-attract text-primary"
          >
            Invite
          </Button>
        )}
      </Group>
    </form>
  );
};

export default InviteUserForm;
