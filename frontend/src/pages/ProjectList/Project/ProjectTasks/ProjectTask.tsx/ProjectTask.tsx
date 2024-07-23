import {
  Button,
  Modal,
  Select,
  Textarea,
  TextInput,
  Tooltip,
} from "@mantine/core";
import { Header } from "../../../../../components/Header/Header";
import { useNavigate } from "react-router-dom";
import { useDisclosure } from "@mantine/hooks";
import { useForm } from "react-hook-form";
import { useEffect, useState } from "react";
import { CreateUpdateTaskRequest } from "../../../../../http/request/CreateUpdateTaskRequest";
import { useTask } from "../../../../../providers/TaskProvider";
import api from "../../../../../api/api";
import { goodNotification } from "../../../../../components/Notifications/Notifications";
import { ChangeStatusRequest } from "../../../../../http/request/ChangeTaskStatusRequest";
import { List } from "../../../../../components/List/List";
import { SrcollUpAffix } from "../../../../../components/Affix/ScrollUpAffix";
import { AddAssigneeForm } from "./AddAssigneeForm";
import { ArrowBackUp, UserMinus } from "tabler-icons-react";
import { useProject } from "../../../../../providers/ProjectProvider";
import useTaskStatus from "../../../../../hooks/useTaskStatus";
import { displayError } from "../../../../../utils/displayError";

const ProjectTask = (): JSX.Element => {
  const navigate = useNavigate();
  const { mutateTasks } = useProject();
  const { task, mutate: mutateTask } = useTask();
  const [charCount, setCharCount] = useState(task?.description?.length);
  const [editPressed, { open: pressEdit, close: unpressEdit }] =
    useDisclosure(false);
  const [
    addAssigneeOpened,
    { open: openAddAssignee, close: closeAddAssignee },
  ] = useDisclosure(false);
  const { taskStatuses: statuses } = useTaskStatus();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    reset,
  } = useForm<CreateUpdateTaskRequest>();

  const {
    setError: setStatusError,
    formState: { errors: statusErrors },
    reset: statusReset,
    handleSubmit: statusSubmit,
    watch: getStatusFormValues,
    setValue: setStatusFormValue,
  } = useForm<ChangeStatusRequest>();

  function resetForm(): void {
    reset({
      summary: task?.summary,
      description: task?.description,
    });
    setCharCount(task?.description?.length);
  }

  useEffect(() => {
    resetForm();
    statusReset({ statusValue: task?.status.value });
  }, [task]);

  const handleEdit = handleSubmit(
    async (updateTaskRequeset: CreateUpdateTaskRequest) => {
      await api
        .put(`tasks/${task?.id}`, updateTaskRequeset)
        .then(function () {
          goodNotification("Task was edited successfully!");
          mutateTask();
          mutateTasks();
          unpressEdit();
        })
        .catch(function (exception) {
          displayError(exception, setError, true);
        });
    }
  );

  const handleTextareaChange = (
    event: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    setCharCount(event.target.value.length);
  };

  const changeStatus = statusSubmit(
    async (changeTaskStatusRequest: ChangeStatusRequest) => {
      await api
        .put(`tasks/${task?.id}/status`, changeTaskStatusRequest)
        .then(function () {
          goodNotification("Task status was saved!");
          mutateTask();
          mutateTasks();
        })
        .catch(function (exception) {
          displayError(exception, setStatusError, true);
          statusReset({ statusValue: task?.status.value });
        });
    }
  );

  const transformedAssignees = task?.assignees?.map((assignee) => ({
    id: assignee.id,
    name: assignee.username,
    fullName: assignee.firstName + " " + assignee.lastName,
  }));

  async function deleteTask() {
    await api
      .delete(`tasks/${task?.id}`)
      .then(function () {
        goodNotification("Task was successfully deleted!");
        navigate("../");
      })
      .catch(function (exception) {
        displayError(exception, undefined, true);
      });
  }

  const unassign = async (username: string) => {
    await api
      .put(`tasks/${task?.id}/assignees`, {
        username,
        shouldBeAssigned: false,
      })
      .then(function () {
        goodNotification(
          "Task was unassigned from " + username + " successfully!"
        );
        mutateTask();
        mutateTasks();
      })
      .catch(function (exception) {
        displayError(exception, undefined, true);
      });
  };

  return (
    <div className="flex flex-col gap-m">
      <Header
        name={task?.summary}
        controls={[
          <Button
            size="md"
            radius="md"
            className="btn-danger"
            onClick={deleteTask}
          >
            Delete task
          </Button>,
          <Tooltip
            label="Go back"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
            openDelay={800}
          >
            <div
              className="flex hover:text-action hover:cursor-pointer transition-all duration-200 ease-out"
              onClick={() => navigate(-1)}
            >
              <ArrowBackUp className="size-l" />
            </div>
          </Tooltip>,
        ]}
      />

      <div className="flex gap-m flex-wrap">
        <form
          className="flex flex-col card p-m gap-s flex-1"
          onSubmit={handleEdit}
        >
          <TextInput
            {...key("summary")}
            variant="filled"
            radius="md"
            size="md"
            label="Task summary"
            error={errors.summary?.message}
            readOnly={!editPressed}
            styles={{
              input: {
                borderColor: "transparent",
              },
            }}
          />
          <div className="flex flex-col gap-xs">
            <Textarea
              {...key("description")}
              variant="filled"
              radius="md"
              size="md"
              label="Task description"
              error={errors.description?.message}
              minRows={10}
              maxRows={14}
              autosize
              readOnly={!editPressed}
              styles={{
                input: {
                  borderColor: "transparent",
                },
              }}
              onChange={handleTextareaChange}
              withErrorStyles={false}
            />
            <div className="text-right">{charCount} characters</div>
          </div>
          <div className="ml-auto">
            {!editPressed ? (
              <Button
                radius="md"
                size="md"
                className="btn-action"
                onClick={pressEdit}
              >
                Edit task
              </Button>
            ) : (
              <div className="flex gap-m">
                <Button
                  radius="md"
                  size="md"
                  variant="outline"
                  onClick={() => {
                    resetForm();
                    unpressEdit();
                  }}
                >
                  Cancel
                </Button>
                <Button
                  radius="md"
                  size="md"
                  className="btn-attract"
                  type="submit"
                >
                  Save changes
                </Button>
              </div>
            )}
          </div>
        </form>
        <div className="flex flex-col gap-m">
          <div className="card flex flex-col gap-s p-m">
            <Select
              size="md"
              radius="md"
              variant="filled"
              data={statuses.map((status) => ({
                value: status.value,
                label: status.name,
              }))}
              error={statusErrors.statusValue?.message}
              label="Task status"
              styles={{
                input: {
                  borderColor: "transparent",
                },
              }}
              value={getStatusFormValues("statusValue")}
              onChange={(value) =>
                setStatusFormValue("statusValue", value ?? "")
              }
            />
            <Button
              size="md"
              radius="md"
              className="btn-attract"
              onClick={changeStatus}
            >
              Save status
            </Button>
          </div>
          <div className="card flex flex-col gap-s p-m">
            <TextInput
              value={task?.author?.username}
              variant="filled"
              radius="md"
              size="md"
              label="Author"
              readOnly
              styles={{
                input: {
                  borderColor: "transparent",
                },
              }}
            />
            <Button
              size="md"
              radius="md"
              className="btn-action"
              onClick={() => {
                if (task?.author) navigate(`/users/${task?.author.id}/profile`);
              }}
            >
              Go to author's profile
            </Button>
          </div>
        </div>
      </div>

      <List
        title="Assignees"
        list={transformedAssignees}
        controls={[
          <Button
            size="md"
            radius="md"
            className="btn-action"
            onClick={openAddAssignee}
          >
            Add assignee
          </Button>,
        ]}
        onItemClick={(assignee) => navigate(`/users/${assignee.id}/profile`)}
        displayAttributes={[
          {
            index: 0,
            attribute: "fullName",
          },
        ]}
        itemToolBar={[
          {
            index: 0,
            icon: <UserMinus />,
            function: (assignee) => {
              unassign(assignee.name);
            },
            toolTipLabel: "Unassign",
          },
        ]}
      />

      <Modal
        onClose={() => {
          closeAddAssignee();
          mutateTask();
          mutateTasks();
        }}
        opened={addAssigneeOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="xl"
      >
        <AddAssigneeForm
          onClose={() => {
            closeAddAssignee();
            mutateTask();
            mutateTasks();
          }}
        />
      </Modal>

      <SrcollUpAffix />
    </div>
  );
};

export default ProjectTask;
