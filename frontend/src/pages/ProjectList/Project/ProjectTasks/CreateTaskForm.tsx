import { useForm } from "react-hook-form";
import { useException } from "../../../../hooks/ExceptionProvider";
import api from "../../../../api/api";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { Button, Group, Textarea, TextInput } from "@mantine/core";
import { CreateUpdateTaskRequest } from "../../../../http/request/CreateUpdateTaskRequest";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { useProject } from "../../../../hooks/ProjectProvider";

interface Props {
  onClose: () => void;
}

const CreateTaskForm = ({ onClose }: Props): JSX.Element => {
  const { handleException } = useException();
  const navigate = useNavigate();
  const [charCount, setCharCount] = useState(0);
  const { project } = useProject();

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<CreateUpdateTaskRequest>();

  const createTask = handleSubmit(
    async (createTaskRequest: CreateUpdateTaskRequest) => {
      await api
        .post(`tasks/projects/${project?.id}`, createTaskRequest)
        .then(function (response) {
          goodNotification("Task was created successfully!");
          onClose();
          navigate(`${response.data.id}`);
        })
        .catch(function (exception) {
          handleException(exception, setError, true);
        });
    }
  );

  const handleTextareaChange = (
    event: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    setCharCount(event.target.value.length);
  };

  return (
    <form className="gap-m flex flex-col p-xs" onSubmit={createTask}>
      <div className="font-bold fnt-md">New task form</div>
      <TextInput
        {...key("summary")}
        variant="filled"
        size="md"
        radius="md"
        label="Summary"
        error={errors.summary?.message}
      />
      <div className="flex flex-col gap-xs">
        <Textarea
          {...key("description")}
          variant="filled"
          size="md"
          radius="md"
          label="Description"
          minRows={10}
          maxRows={13}
          autosize
          onChange={handleTextareaChange}
          withErrorStyles={false}
          error={errors.description?.message}
        />
        <div className="text-right">{charCount} characters</div>
      </div>
      <Group className="gap-m">
        <Button
          variant="outline"
          size="md"
          radius="md"
          className="flex-1"
          onClick={onClose}
        >
          Cancel
        </Button>

        <Button
          radius="md"
          size="md"
          type="submit"
          className="flex-1 btn-attract text-primary"
        >
          Create
        </Button>
      </Group>
    </form>
  );
};

export default CreateTaskForm;
