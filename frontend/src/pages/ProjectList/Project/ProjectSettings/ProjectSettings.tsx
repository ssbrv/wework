import { Button, Group, Modal, NativeSelect } from "@mantine/core";
import { ButtonBar } from "../../../../components/ButtonBar/ButtonBar";
import { useProject } from "../../../../hooks/ProjectProvider";
import { useDisclosure } from "@mantine/hooks";
import api from "../../../../api/api";
import { useException } from "../../../../hooks/ExceptionProvider";
import { goodNotification } from "../../../../components/Notifications/Notifications";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { ChangeProjectStatusRequest } from "../../../../http/request/ChangeProjectStatusRequest";
import { useEffect } from "react";

const ProjectSettings = (): JSX.Element => {
  const { project, mutate } = useProject();
  const { handleException } = useException();
  const navigate = useNavigate();

  const [leaveOpened, { open: openLeave, close: closeLeave }] =
    useDisclosure(false);
  const [
    changeStatusOpened,
    { open: openChangeStatus, close: closeChangeStatus },
  ] = useDisclosure(false);
  const [deleteOpened, { open: openDelete, close: closeDelete }] =
    useDisclosure(false);

  const { register, handleSubmit, reset } =
    useForm<ChangeProjectStatusRequest>();

  function resetForm(): void {
    reset({ status: project?.status });
  }

  useEffect(() => {
    resetForm();
  }, [project]);

  const deleteProject = async (): Promise<void> => {
    await api
      .delete(`/projects/${project?.id}`)
      .then(function () {
        goodNotification(
          "Project was deleted",
          "Transferring you to project list page"
        );
        navigate("/projects");
      })
      .catch(function (exception) {
        handleException(exception, undefined, true);
      });

    closeDelete();
  };

  const leaveProject = async (): Promise<void> => {
    await api
      .put(`/projects/${project?.id}/membership`)
      .then(function () {
        goodNotification(
          "Left project successfully",
          "Transferring you to project list page"
        );
        navigate("/projects");
      })
      .catch(function (exception) {
        handleException(exception, undefined, true);
      });

    closeLeave();
  };

  const changeStatus = handleSubmit(
    async (
      changeProjectStatusRequest: ChangeProjectStatusRequest
    ): Promise<void> => {
      await api
        .put(`/projects/${project?.id}/status`, changeProjectStatusRequest)
        .then(function () {
          goodNotification("Project status changed successfully!");
          closeChangeStatus();
          mutate();
        })
        .catch(function (exception) {
          handleException(exception, undefined, true);
          closeChangeStatus();
          resetForm();
        });
    }
  );

  return (
    <div className="flex flex-col gap-m">
      <ButtonBar>
        <Button
          size="md"
          radius="md"
          className="btn-action"
          onClick={openChangeStatus}
        >
          Change project status
        </Button>
        <Button
          size="md"
          radius="md"
          className="btn-danger"
          onClick={openLeave}
        >
          Leave project
        </Button>

        <Button
          size="md"
          radius="md"
          className="btn-danger"
          onClick={openDelete}
        >
          Delete project
        </Button>
      </ButtonBar>
      <Modal
        onClose={closeLeave}
        opened={leaveOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="sm"
      >
        <div className="flex flex-col p-s gap-m">
          <div className="fnt-md text-center">
            Are you sure you want to leave project{" "}
            <span className="font-bold italic">{project?.name}?</span>
          </div>
          <Group className="gap-m">
            <Button
              size="md"
              radius="md"
              className="btn-attract flex-1"
              onClick={closeLeave}
            >
              No!
            </Button>
            <Button
              size="md"
              radius="md"
              className="btn-danger flex-1"
              onClick={leaveProject}
            >
              Yes!
            </Button>
          </Group>
        </div>
      </Modal>
      <Modal
        onClose={() => {
          closeChangeStatus();
          resetForm();
        }}
        opened={changeStatusOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="sm"
      >
        <div className="flex flex-col p-s gap-m">
          <div className="fnt-md font-bold">Project status</div>
          <NativeSelect
            {...register("status")}
            size="md"
            radius="md"
            variant="filled"
            data={[
              { label: "Active", value: "ENABLED" },
              { label: "Closed", value: "DISABLED" },
            ]}
            styles={{
              input: {
                borderColor: "transparent",
              },
            }}
          />
          <Group className="gap-m">
            <Button
              size="md"
              radius="md"
              className="flex-1"
              variant="outline"
              onClick={() => {
                closeChangeStatus();
                resetForm();
              }}
            >
              Cancel
            </Button>
            <Button
              size="md"
              radius="md"
              className="btn-attract flex-1"
              onClick={changeStatus}
            >
              Save
            </Button>
          </Group>
        </div>
      </Modal>
      <Modal
        onClose={closeDelete}
        opened={deleteOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="sm"
      >
        <div className="flex flex-col p-s gap-m">
          <div className="fnt-md text-center">
            Are you sure you want to delete project{" "}
            <span className="font-bold italic">{project?.name}?</span>
          </div>
          <Group className="gap-m">
            <Button
              size="md"
              radius="md"
              className="btn-attract flex-1"
              onClick={closeDelete}
            >
              No!
            </Button>
            <Button
              size="md"
              radius="md"
              className="btn-danger flex-1"
              onClick={deleteProject}
            >
              Yes!
            </Button>
          </Group>
        </div>
      </Modal>
    </div>
  );
};

export default ProjectSettings;
