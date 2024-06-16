import { Text, Tooltip } from "@mantine/core";
import { Membership } from "../../domain/Membership";
import {
  CalendarTime,
  Check,
  User,
  UserCircle,
  Users,
  X,
} from "tabler-icons-react";
import { KeyedMutator } from "swr";
import { ChangeMembershipStatusRequest } from "../../http/request/ChangeMembershipStatusRequest";
import api from "../../api/api";
import { goodNotification } from "../../components/Notifications/Notifications";
import { useException } from "../../hooks/ExceptionProvider";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { formatDate } from "../../utils/utils";

interface Props {
  invitation: Membership;
  mutate: KeyedMutator<Membership[]>;
}

export const InvitationCard = ({ invitation, mutate }: Props): JSX.Element => {
  const { handleException } = useException();
  const { setValue, handleSubmit } = useForm<ChangeMembershipStatusRequest>();
  const navigate = useNavigate();

  const changeMembershipStatusRequest = handleSubmit(
    async (
      changeMembershipStatusRequest: ChangeMembershipStatusRequest
    ): Promise<void> => {
      try {
        await api.put(
          `/memberships/${invitation.id}`,
          changeMembershipStatusRequest
        );
        goodNotification(
          `The invitation was ${
            changeMembershipStatusRequest.status === "ENABLED"
              ? "accepted successfully!"
              : "rejected successfully!"
          }`
        );
        mutate();
      } catch (exception) {
        handleException(exception, undefined, true);
      }
    }
  );

  return (
    <div className="card p-m h-[452px] flex flex-col gap-s">
      <div className="flex justify-between gap-xs">
        <Tooltip
          label="Project name"
          position="right"
          offset={10}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="font-bold fnt-md">{invitation.project.name}</div>
        </Tooltip>
        <Tooltip
          label="Number of members"
          position="bottom"
          offset={{ mainAxis: 10, crossAxis: -40 }}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="flex gap-xs">
            <div>{invitation.project.memberCount}</div>
            <Users />
          </div>
        </Tooltip>
      </div>

      <Tooltip
        label="Your entry role"
        position="top"
        offset={{ mainAxis: 10, crossAxis: 40 }}
        withArrow
        arrowSize={8}
        arrowRadius={4}
      >
        <div className="flex gap-s items-center hover:bg-hover rounded hover:cursor-pointer p-xs transition-all duration-100 ease-linear">
          <UserCircle className="size-l" />
          <div>{invitation.role.name}</div>
        </div>
      </Tooltip>

      <Tooltip
        label="Go to inviter's profile"
        position="top"
        offset={{ mainAxis: 10, crossAxis: 50 }}
        withArrow
        arrowSize={8}
        arrowRadius={4}
      >
        <div
          className="flex gap-s items-center hover:bg-hover rounded hover:cursor-pointer p-xs transition-all duration-100 ease-linear"
          onClick={() => navigate(`/users/${invitation.member.id}/profile`)}
        >
          <User className="size-l" onClick={() => {}} />

          <div>{invitation.inviter?.username}</div>
        </div>
      </Tooltip>

      <Tooltip
        label="Project description"
        position="top"
        offset={{ mainAxis: 10, crossAxis: 50 }}
        withArrow
        arrowSize={8}
        arrowRadius={4}
      >
        <Text lineClamp={6} className="whitespace-pre text-wrap">
          {invitation.project.description}
        </Text>
      </Tooltip>

      <div className="flex gap-xs items-center">
        <Tooltip
          label="Invited at"
          position="top"
          offset={10}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="flex gap-s items-center pl-xs">
            <CalendarTime className="size-l" />
            <div>{formatDate(new Date(invitation.createdAt))}</div>
          </div>
        </Tooltip>
        <div className="flex gap-s ml-auto">
          <Tooltip
            label="Accept"
            position="top"
            offset={0}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div>
              <Check
                className="size-md text-secondary hover:text-attract hover:cursor-pointer"
                onClick={() => {
                  setValue("status", "ENABLED");
                  changeMembershipStatusRequest();
                }}
              />
            </div>
          </Tooltip>
          <Tooltip
            label="Reject"
            position="top"
            offset={0}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div>
              <X
                className="size-md text-secondary hover:text-danger hover:cursor-pointer"
                onClick={() => {
                  setValue("status", "REJECTED");
                  changeMembershipStatusRequest();
                }}
              />
            </div>
          </Tooltip>
        </div>
      </div>
    </div>
  );
};
