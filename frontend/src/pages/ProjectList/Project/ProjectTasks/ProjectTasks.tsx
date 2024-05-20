import { Button, Modal } from "@mantine/core";
import { List } from "../../../../components/List/List";
import { useProject } from "../../../../hooks/ProjectProvider";
import { useDisclosure } from "@mantine/hooks";
import { SrcollUpAffix } from "../../../../components/Affix/ScrollUpAffix";
import CreateTaskForm from "./CreateTaskForm";
import { useNavigate } from "react-router-dom";

const ProjectTasks = (): JSX.Element => {
  const navigate = useNavigate();
  const { tasks, mutateTasks } = useProject();
  const [createTaskOpened, { open: openCreateTask, close: closeCreateTask }] =
    useDisclosure(false);

  const transformedTasks = tasks?.map((task) => ({
    id: task.id,
    name: task.summary,
  }));

  return (
    <div className="flex flex-col gap-m">
      <List
        title="Tasks"
        controls={[
          <Button
            size="md"
            radius="md"
            className="btn-action"
            onClick={openCreateTask}
          >
            Create task
          </Button>,
        ]}
        list={transformedTasks}
        onItemClick={(item) => navigate(`${item.id}`)}
      />
      <Modal
        onClose={() => {
          closeCreateTask();
          mutateTasks();
        }}
        opened={createTaskOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="xl"
      >
        <CreateTaskForm
          onClose={() => {
            closeCreateTask();
            mutateTasks();
          }}
        />
      </Modal>
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectTasks;
