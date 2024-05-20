import { Group, Text } from "@mantine/core";
import { Membership } from "../../domain/Membership";
import { Check, User, UserCircle, X } from "tabler-icons-react";
import { KeyedMutator } from "swr";
import { ChangeMembershipStatusRequest } from "../../http/request/ChangeMembershipStatusRequest";
import api from "../../api/api";
import { goodNotification } from "../../components/Notifications/Notifications";
import { useException } from "../../hooks/ExceptionProvider";
import { useForm } from "react-hook-form";

interface Props {
  invitation: Membership;
  mutate: KeyedMutator<Membership[]>;
}

export const InvitationCard = ({ invitation, mutate }: Props): JSX.Element => {
  const { handleException } = useException();
  const { setValue, handleSubmit } = useForm<ChangeMembershipStatusRequest>();

  const changeMembershipStatusRequest = handleSubmit(
    async (
      changeMembershipStatusRequest: ChangeMembershipStatusRequest
    ): Promise<void> => {
      await api
        .put(`/memberships/${invitation.id}`, changeMembershipStatusRequest)
        .then(function () {
          goodNotification(
            "The invitation was " +
              (changeMembershipStatusRequest.status === "ENABLED"
                ? "accepted successfully!"
                : "rejected successfully!!")
          );
          mutate();
        })
        .catch(function (exception) {
          handleException(exception, undefined, true);
        });
    }
  );

  return (
    <div className="relative card p-m h-[350px] group">
      <div className="z-10 flex flex-col gap-s">
        <div className="font-bold fnt-md">{invitation.project.name}</div>

        <div className="flex gap-s items-center">
          <UserCircle className="size-l" />
          <div>{invitation.role.name}</div>
        </div>

        <div className="flex gap-s items-center">
          <User className="size-l" />
          <div>{invitation.inviter?.username}</div>
        </div>

        <Text lineClamp={6}>{invitation.project.description}</Text>
      </div>

      <div className="rounded absolute inset-0 z-20 hidden group-hover:flex justify-center items-center hover:backdrop-blur-[2px] transition-all ease-linear duration-200">
        <Group className="justify-center gap-l">
          <Check
            className="size-lg text-secondary hover:text-attract hover:cursor-pointer"
            onClick={() => {
              setValue("status", "ENABLED");
              changeMembershipStatusRequest();
            }}
          />
          <X
            className="size-lg text-secondary hover:text-danger hover:cursor-pointer"
            onClick={() => {
              setValue("status", "REJECTED");
              changeMembershipStatusRequest();
            }}
          />
        </Group>
      </div>
    </div>
  );
};
