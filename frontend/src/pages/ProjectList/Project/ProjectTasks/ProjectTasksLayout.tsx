import { Button, Modal } from "@mantine/core";
import { DragDrop, List as IconList } from "tabler-icons-react";
import { useDisclosure } from "@mantine/hooks";
import CreateTaskForm from "./CreateTaskForm";
import { TabsBar, TabsBarTab } from "../../../../components/TabsBar/TabsBar";
import { Header } from "../../../../components/Header/Header";
import { Outlet } from "react-router-dom";
import { useProject } from "../../../../hooks/ProjectProvider";

const tabs: TabsBarTab[] = [
  {
    link: "list",
    name: "List view",
    icon: <IconList />,
  },
  {
    link: "board",
    name: "Board view",
    icon: <DragDrop />,
  },
];

const ProjectTasksLayout = (): JSX.Element => {
  const [createTaskOpened, { open: openCreateTask, close: closeCreateTask }] =
    useDisclosure(false);

  const { mutateTasks } = useProject();

  return (
    <div className="flex flex-col gap-m">
      <Header
        name="Tasks"
        controls={[
          <TabsBar tabs={tabs} linkLevel={4} defaultTab="list" />,
          <Button
            size="md"
            radius="md"
            className="btn-action"
            onClick={openCreateTask}
          >
            New task
          </Button>,
        ]}
      />
      <Outlet />
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
    </div>
  );
};

export default ProjectTasksLayout;
