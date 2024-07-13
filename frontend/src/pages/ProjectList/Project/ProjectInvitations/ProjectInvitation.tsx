import { Button, CloseButton, Modal, TextInput, Tooltip } from "@mantine/core";
import { Header } from "../../../../components/Header/Header";
import { useMembership } from "../../../../hooks/MembershipProvider";
import { ButtonBar } from "../../../../components/ButtonBar/ButtonBar";
import { useNavigate } from "react-router-dom";
import { DateInput } from "@mantine/dates";
import { useDisclosure } from "@mantine/hooks";
import ChangeRoleForm from "../ProjectMembers/ChangeRoleForm";
import { useForm } from "react-hook-form";
import { Membership } from "../../../../domain/Membership";
import { useEffect } from "react";
import CancelInvitationForm from "./CancelInvitationForm";

const ProjectInvitation = (): JSX.Element => {
  const navigate = useNavigate();
  const { membership, mutate } = useMembership();
  const [changeRoleOpened, { open: openChangeRole, close: closeChangeRole }] =
    useDisclosure(false);
  const [
    cancelInvitationOpened,
    { open: openCancelInvitation, close: closeCancelInvitation },
  ] = useDisclosure(false);

  const { register: key, reset } = useForm<Membership>();

  function resetForm(): void {
    reset({
      member: membership?.member,
      role: membership?.role,
      inviter: membership?.inviter,
    });
  }

  useEffect(() => {
    resetForm();
  }, [membership]);

  return (
    <div className="flex flex-col gap-m">
      <Header
        name={
          "Invitation - " +
          membership?.member.firstName +
          " " +
          membership?.member.lastName
        }
        controls={[
          <Tooltip
            label="Go to invitations"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <CloseButton onClick={() => navigate("../")} />
          </Tooltip>,
        ]}
      />

      <div className="flex gap-m flex-wrap">
        <div className="flex flex-col gap-m flex-1 card p-m">
          <TextInput
            {...key("member.username")}
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
            {...key("role.name")}
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
          />
          <div className="ml-auto">
            <Button
              size="md"
              radius="md"
              className="btn-action"
              onClick={openChangeRole}
            >
              Change role
            </Button>
          </div>
        </div>

        <div className="flex flex-col gap-m flex-1 card p-m justify-evenly">
          <TextInput
            {...key("inviter.username")}
            size="md"
            radius="md"
            variant="filled"
            readOnly
            label="Inviter"
            styles={{
              input: {
                borderColor: "transparent",
              },
            }}
          />
          <DateInput
            value={membership ? new Date(membership.createdAt) : null}
            label="Invited at"
            size="md"
            radius="md"
            variant="filled"
            readOnly
            styles={{
              input: {
                borderColor: "transparent",
              },
            }}
          />
        </div>
      </div>

      <ButtonBar>
        <Button
          size="md"
          radius="md"
          className="btn-action"
          onClick={() => navigate(`/users/${membership?.member.id}/profile`)}
        >
          Go to his profile
        </Button>
        <Button
          size="md"
          radius="md"
          className="btn-danger"
          onClick={openCancelInvitation}
        >
          Cancel invitation
        </Button>
      </ButtonBar>

      <Modal
        onClose={() => {
          closeChangeRole();
          mutate();
        }}
        opened={changeRoleOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="lg"
      >
        <ChangeRoleForm
          onClose={() => {
            closeChangeRole();
            mutate();
          }}
        />
      </Modal>
      <Modal
        onClose={closeCancelInvitation}
        opened={cancelInvitationOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="sm"
      >
        <CancelInvitationForm onClose={closeCancelInvitation} />
      </Modal>
    </div>
  );
};

export default ProjectInvitation;
