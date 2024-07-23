import { Button, Group } from "@mantine/core";
import { useMembership } from "../../../../hooks/MembershipProvider";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { useNavigate } from "react-router-dom";
import api from "../../../../api/api";
import { displayError } from "../../../../utils/displayError";

interface Props {
  onClose: () => void;
}

const KickMemberForm = ({ onClose }: Props): JSX.Element => {
  const { membership } = useMembership();
  const navigate = useNavigate();

  async function kickMember(): Promise<void> {
    await api
      .put(`/memberships/${membership?.id}/kick`)
      .then(function () {
        goodNotification("User was kicked successfully!");
        onClose();
        navigate("../");
      })
      .catch(function (exception) {
        displayError(exception, undefined, true);
        onClose();
      });
  }

  return (
    <div className="gap-m flex flex-col p-xs">
      <div className="text-md text-center">
        Are you sure you want to kick{" "}
        <span className="font-bold italic">{membership?.member.username}</span>{" "}
        from this project?
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

export default KickMemberForm;
