import { useForm } from "react-hook-form";
import { useException } from "../../../../hooks/ExceptionProvider";
import api from "../../../../api/api";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { Button, Group, Stepper, TextInput } from "@mantine/core";
import useSWR from "swr";
import { getFetcher } from "../../../../api/fetchers";
import { Role } from "../../../../domain/Role";
import { List } from "../../../../components/List/List";
import { useState } from "react";
import { ChangeRoleRequest } from "../../../../http/request/ChangeRoleRequest";
import { useMembership } from "../../../../hooks/MembershipProvider";

interface Props {
  onClose: () => void;
}

const ChangeRoleForm = ({ onClose }: Props): JSX.Element => {
  const [active, setActive] = useState(0);
  const nextStep = () =>
    setActive((current) => (current < 2 ? current + 1 : current));
  const prevStep = () =>
    setActive((current) => (current > 0 ? current - 1 : current));

  const { handleException } = useException();

  const { data: roles } = useSWR<Role[]>("member-roles", getFetcher);
  const { membership } = useMembership();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    setValue,
    clearErrors,
    watch,
  } = useForm<ChangeRoleRequest>();

  const changeRole = handleSubmit(
    async (changeRoleRequest: ChangeRoleRequest) => {
      await api
        .put(`memberships/${membership?.id}/member-role`, changeRoleRequest)
        .then(function () {
          goodNotification("Role was changed successfully!");
          onClose();
        })
        .catch(function (exception) {
          handleException(exception, setError, true);
        });
    }
  );

  return (
    <form className="gap-m flex flex-col p-xs" onSubmit={changeRole}>
      <Stepper
        active={active}
        onStepClick={setActive}
        allowNextStepsSelect={false}
        size="lg"
      >
        <Stepper.Step label="First step" description="Select role">
          <List
            title="Roles"
            list={roles}
            itemsPerPage={5}
            wrapInCard={false}
            onItemClick={(role) => {
              setValue("roleValue", role.value);
              setValue("roleName", role.name);
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

        <Stepper.Step label="Second step" description="Verify and submit">
          <div className="flex flex-col gap-m">
            <TextInput
              defaultValue={membership?.member.username}
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
        </Stepper.Step>
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
        {active === 1 && (
          <Button
            radius="md"
            size="md"
            type="submit"
            className="flex-1 btn-attract text-primary"
          >
            Submit changes
          </Button>
        )}
      </Group>
    </form>
  );
};

export default ChangeRoleForm;
