import { Button, Textarea, TextInput } from "@mantine/core";
import { useProject } from "../../../../hooks/ProjectProvider";
import { useForm } from "react-hook-form";
import { CreateUpdateProjectRequest } from "../../../../http/request/CreateUpdateProjectRequest";
import { useException } from "../../../../hooks/ExceptionProvider";
import { useEffect, useState } from "react";
import api from "../../../../api/api";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { ButtonBar } from "../../../../components/ButtonBar/ButtonBar";
import { useDisclosure } from "@mantine/hooks";

const ProjectDetails = (): JSX.Element => {
  const { handleException } = useException();
  const { project, mutate } = useProject();
  const [charCount, setCharCount] = useState(project?.description?.length);
  const [editPressed, { open: pressEdit, close: unpressEdit }] =
    useDisclosure(false);

  const {
    register: key,
    handleSubmit,
    setError,
    formState: { errors },
    reset,
  } = useForm<CreateUpdateProjectRequest>();

  function resetForm(): void {
    reset({
      name: project?.name,
      description: project?.description,
    });
  }

  useEffect(() => {
    resetForm();
    setCharCount(project?.description?.length);
  }, [project]);

  const handleEdit = handleSubmit(
    async (updateProjectRequest: CreateUpdateProjectRequest) => {
      await api
        .put(`projects/${project?.id}`, updateProjectRequest)
        .then(function () {
          goodNotification("Project information was edited successfully!");
          mutate();
          unpressEdit();
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
    <form className="flex flex-col gap-m" onSubmit={handleEdit}>
      <div className="flex flex-col card p-m gap-s">
        <TextInput
          {...key("name")}
          variant="filled"
          radius="md"
          size="md"
          label="Project name"
          error={errors.name?.message}
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
            label="Project description"
            error={errors.description?.message}
            minRows={10}
            maxRows={10}
            rows={10}
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
      </div>
      <ButtonBar>
        {!editPressed ? (
          <Button
            radius="md"
            size="md"
            className="btn-action"
            onClick={pressEdit}
          >
            Edit project's basic information
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
            <Button radius="md" size="md" className="btn-attract" type="submit">
              Save changes
            </Button>
          </div>
        )}
      </ButtonBar>
    </form>
  );
};

export default ProjectDetails;
