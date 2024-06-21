import { ActionIcon, Menu, Text, Tooltip } from "@mantine/core";
import { Membership } from "../../domain/Membership";
import {
  ArrowForwardUp,
  CalendarTime,
  Check,
  CircleOff,
  DotsVertical,
  User,
  UserCircle,
  UserOff,
  Users,
  Writing,
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
    <div className="card p-m flex flex-col gap-s">
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
          position="left"
          offset={10}
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

      {invitation.project.description && (
        <Tooltip
          label="Project description"
          position="top"
          offset={{ mainAxis: 10, crossAxis: 50 }}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="flex gap-s p-xs">
            <Writing className="size-l flex-shrink-0" />
            <Text lineClamp={6} className="whitespace-pre text-wrap">
              {invitation.project.description}
            </Text>
          </div>
        </Tooltip>
      )}

      <div className="flex">
        <Tooltip
          label="Your entry role"
          position="right"
          offset={10}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="flex gap-s items-center p-xs">
            <UserCircle className="size-l flex-shrink-0" />
            <div>{invitation.role.name}</div>
          </div>
        </Tooltip>
      </div>

      <Tooltip
        label="Go to inviter's profile"
        position="top"
        offset={{ mainAxis: 10, crossAxis: 50 }}
        withArrow
        arrowSize={8}
        arrowRadius={4}
      >
        <div
          className="flex gap-s items-center hover:bg-hover rounded hover:cursor-pointer p-xs transition-all duration-200 ease-linear"
          onClick={() => navigate(`/users/${invitation.inviter?.id}/profile`)}
        >
          <User className="size-l flex-shrink-0" />

          <div>{invitation.inviter?.username}</div>
        </div>
      </Tooltip>

      <div className="flex gap-md items-center mt-auto justify-between">
        <Tooltip
          label="Invited at"
          position="top"
          offset={10}
          withArrow
          arrowSize={8}
          arrowRadius={4}
        >
          <div className="flex gap-s items-center pl-xs">
            <CalendarTime className="size-l flex-shrink-0" />
            <div>{formatDate(new Date(invitation.createdAt))}</div>
          </div>
        </Tooltip>

        <div className="flex gap-s items-center">
          <Tooltip
            label="Accept"
            position="top"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div>
              <Check
                className="size-l text-secondary hover:text-attract transition-all duration-200 ease-linear"
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
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div>
              <X
                className="size-l text-secondary hover:text-danger transition-all duration-200 ease-linear"
                onClick={() => {
                  setValue("status", "REJECTED");
                  changeMembershipStatusRequest();
                }}
              />
            </div>
          </Tooltip>
          <Tooltip
            label="Menu"
            position="top"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <div>
              <Menu radius="md" withArrow position="top" shadow="md">
                <Menu.Target>
                  <ActionIcon className="bg-primary hover:bg-primary text-secondary hover:text-action transition-all duration-200 ease-linear">
                    <DotsVertical />
                  </ActionIcon>
                </Menu.Target>
                <Menu.Dropdown>
                  <Menu.Item leftSection={<ArrowForwardUp />}>
                    See project
                  </Menu.Item>
                  <Menu.Item leftSection={<UserOff />}>Block inviter</Menu.Item>
                  <Menu.Item leftSection={<CircleOff />}>
                    Block project
                  </Menu.Item>
                </Menu.Dropdown>
              </Menu>
            </div>
          </Tooltip>
        </div>
      </div>
    </div>
  );
};
