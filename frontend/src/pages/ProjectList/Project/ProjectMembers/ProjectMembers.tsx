import useSWR from "swr";
import { useProject } from "../../../../providers/ProjectProvider";
import { Membership } from "../../../../domain/Membership";
import { getFetcher } from "../../../../api/fetchers";
import { List } from "../../../../components/List/List";
import { SrcollUpAffix } from "../../../../components/Affix/ScrollUpAffix";
import { Button, Modal, Tooltip } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { useDisclosure } from "@mantine/hooks";
import InviteUserForm from "./InviteUserForm";
import { Users } from "tabler-icons-react";
import { displayError } from "../../../../utils/displayError";

const ProjectMembers = (): JSX.Element => {
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
    displayError(error, undefined, true);
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
          <Tooltip
            label="Number of members"
            position="bottom"
            offset={{ mainAxis: 15, crossAxis: -40 }}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div className="flex gap-xs">
              <div>{project?.memberCount}</div>
              <Users />
            </div>
          </Tooltip>,
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
