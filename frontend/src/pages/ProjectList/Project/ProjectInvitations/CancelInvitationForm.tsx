import { Button, Group } from "@mantine/core";
import { useException } from "../../../../hooks/ExceptionProvider";
import { useMembership } from "../../../../hooks/MembershipProvider";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { useNavigate } from "react-router-dom";

interface Props {
  onClose: () => void;
}

const CancelInvitationForm = ({ onClose }: Props): JSX.Element => {
  const { handleException } = useException();
  const { membership, changeMembershipStatus } = useMembership();
  const navigate = useNavigate();

  async function kickMember(): Promise<void> {
    await changeMembershipStatus({ status: "KICKED" })
      .then(function () {
        goodNotification("User was kicked successfully!");
        onClose();
        navigate("../");
      })
      .catch(function (exception) {
        handleException(exception, undefined, true);
        onClose();
      });
  }

  return (
    <div className="gap-m flex flex-col p-xs">
      <div className="text-md text-center">
        Are you sure you want to cancel invitation of{" "}
        <span className="font-bold italic">{membership?.member.username}</span>{" "}
        ?
      </div>
      <Group className="gap-m">
        <Button
          size="md"
          radius="md"
          className="btn-attract flex-1"
          onClick={onClose}
        >
          No!
        </Button>
        <Button
          size="md"
          radius="md"
          className="btn-danger flex-1"
          onClick={kickMember}
        >
          Yes!
        </Button>
      </Group>
    </div>
  );
};

export default CancelInvitationForm;
