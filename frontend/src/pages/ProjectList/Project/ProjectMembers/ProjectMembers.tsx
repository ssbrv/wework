import useSWR from "swr";
import { useProject } from "../../../../hooks/ProjectProvider";
import { Membership } from "../../../../domain/Membership";
import { getFetcher } from "../../../../api/fetchers";
import { useException } from "../../../../hooks/ExceptionProvider";
import { List } from "../../../../components/List/List";
import { SrcollUpAffix } from "../../../../components/Affix/ScrollUpAffix";
import { Button, Modal } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { useDisclosure } from "@mantine/hooks";
import InviteUserForm from "./InviteUserForm";

const ProjectMembers = (): JSX.Element => {
  const { handleException } = useException();
  const { project } = useProject();
  const navigate = useNavigate();
  const [inviteUserOpened, { open: openInviteUser, close: closeInviteUser }] =
    useDisclosure(false);

  const { data: memberships, error } = useSWR<Membership[]>(
    `projects/${project?.id}/memberships`,
    getFetcher
  );

  if (error) {
    console.log(
      "The exception was caught while fetching data from projects/projectId/memberships"
    );
    handleException(error, undefined, true);
  }

  const transformedMemberships = memberships?.map((membership) => ({
    id: membership.id,
    name: membership.member.username,
    fullName: membership.member.firstName + " " + membership.member.lastName,
    roleName: membership.role.name,
  }));

  return (
    <div className="flex flex-col gap-m">
      <List
        title="Members"
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
        list={transformedMemberships}
        onItemClick={(member) => {
          navigate(`${member.id}`);
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
        <InviteUserForm onClose={closeInviteUser} />
      </Modal>
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectMembers;
