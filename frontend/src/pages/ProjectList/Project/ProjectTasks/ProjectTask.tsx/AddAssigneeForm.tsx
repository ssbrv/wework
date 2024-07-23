import useSWR from "swr";
import { Membership } from "../../../../../domain/Membership";
import { useProject } from "../../../../../hooks/ProjectProvider";
import { getFetcher } from "../../../../../api/fetchers";
import { List } from "../../../../../components/List/List";
import { useForm } from "react-hook-form";
import { UpdateAssigneeRequest } from "../../../../../http/request/UpdateAssigneeRequest";
import api from "../../../../../api/api";
import { useTask } from "../../../../../hooks/TaskProvider";
import { goodNotification } from "../../../../../components/Notifications/Notifications";
import { useEffect } from "react";
import { displayError } from "../../../../../utils/displayError";

interface Props {
  onClose: () => void;
}

export const AddAssigneeForm = ({ onClose }: Props): JSX.Element => {
  const { project } = useProject();
  const { task } = useTask();

  const { data: memberships, error } = useSWR<Membership[]>(
    `projects/${project?.id}/memberships`,
    getFetcher
  );

  if (error) displayError(error, undefined, true);

  const { setValue, reset, handleSubmit } = useForm<UpdateAssigneeRequest>();

  useEffect(() => {
    reset({ shouldBeAssigned: true, username: undefined });
  }, [reset]);

  const transformedMemberships = memberships?.map((membership) => ({
    id: membership.id,
    name: membership.member.username,
    fullName: membership.member.firstName + " " + membership.member.lastName,
    roleName: membership.role.name,
  }));

  const addAssignee = handleSubmit(
    async (updateAssigneeRequest: UpdateAssigneeRequest) => {
      await api
        .put(`tasks/${task?.id}/assignees`, updateAssigneeRequest)
        .then(function () {
          goodNotification(
            "The task was successfully assigned to " +
              updateAssigneeRequest.username
          );
          onClose();
        })
        .catch(function (exception) {
          displayError(exception, undefined, true);
        });
    }
  );

  return (
    <div className="gap-m flex flex-col p-xs">
      <List
        wrapInCard={false}
        title="Members"
        list={transformedMemberships}
        onItemClick={(member) => {
          setValue("username", member.name);
          addAssignee();
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
    </div>
  );
};
