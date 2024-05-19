import { Button, Group, Textarea, TextInput } from "@mantine/core";
import { useException } from "../../hooks/ExceptionProvider";
import { useForm } from "react-hook-form";
import { goodNotification } from "../../components/Notifications/Notifications";
import { CreateUpdateProjectRequest } from "../../http/request/CreateUpdateProjectRequest";
import api from "../../api/api";
import { useState } from "react";

interface Props {
  onClose: () => void;
}

const CreateProjectForm = ({ onClose }: Props): JSX.Element => {
  const { handleException } = useException();
  const [charCount, setCharCount] = useState(0);

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<CreateUpdateProjectRequest>();

  const createProject = handleSubmit(
    async (createProjectRequest: CreateUpdateProjectRequest) => {
      await api
        .post("projects", createProjectRequest)
        .then(function () {
          goodNotification("Project was created successfully!");
          onClose();
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
    <form className="gap-m flex flex-col p-xs" onSubmit={createProject}>
      <div className="fnt-md font-bold">New project form</div>
      <TextInput
        {...key("name")}
        variant="filled"
        radius="md"
        size="md"
        label="Project name"
        error={errors.name?.message}
      />

      <div className="flex flex-col gap-xs">
        <Textarea
          {...key("description")}
          variant="filled"
          radius="md"
          size="md"
          label="Project description"
          error={errors.description?.message}
          minRows={10}
          maxRows={13}
          autosize
          onChange={handleTextareaChange}
          withErrorStyles={false}
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
          className="flex-1 btn-attract"
        >
          Save
        </Button>
      </Group>
    </form>
  );
};

export default CreateProjectForm;
