import useSWR from "swr";
import { useProject } from "../../../../hooks/ProjectProvider";
import { Membership } from "../../../../domain/Membership";
import { getFetcher } from "../../../../api/fetchers";
import { List } from "../../../../components/List/List";
import { SrcollUpAffix } from "../../../../components/Affix/ScrollUpAffix";
import { Button, Modal } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { useDisclosure } from "@mantine/hooks";
import InviteUserForm from "../ProjectMembers/InviteUserForm";
import { displayError } from "../../../../utils/displayError";

const ProjectInvitations = (): JSX.Element => {
  const { project } = useProject();
  const navigate = useNavigate();
  const [inviteUserOpened, { open: openInviteUser, close: closeInviteUser }] =
    useDisclosure(false);

  const {
    data: invitations,
    error,
    mutate,
  } = useSWR<Membership[]>(`projects/${project?.id}/invitations`, getFetcher);

  if (error) {
    console.log(
      "The exception was caught while fetching data from projects/projectId/invitations"
    );
    displayError(error, undefined, true);
  }

  const transformedInvitations = invitations?.map((invitation) => ({
    id: invitation.id,
    name: invitation.member.username,
    fullName: invitation.member.firstName + " " + invitation.member.lastName,
    roleName: invitation.role.name,
  }));

  return (
    <div className="flex flex-col gap-m">
      <List
        title="Invitated users"
        controls={[
          <Button
            size="md"
            radius="md"
            className="btn-action"
            onClick={openInviteUser}
          >
            Invite user
          </Button>,
        ]}
        list={transformedInvitations}
        onItemClick={(invitation) => {
          navigate(`./${invitation.id}`);
        }}
        displayAttributes={[
          {
            index: 0,
            attribute: "fullName",
          },
          {
            index: 1,
            attribute: "roleName",
          },
        ]}
      />
      <Modal
        onClose={closeInviteUser}
        opened={inviteUserOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="lg"
      >
        <InviteUserForm
          onClose={() => {
            closeInviteUser();
            mutate();
          }}
        />
      </Modal>
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectInvitations;
