import { useProject } from "../../../../../hooks/ProjectProvider";
import { SrcollUpAffix } from "../../../../../components/Affix/ScrollUpAffix";
import useTaskStatus from "../../../../../hooks/useTaskStatus";
import { Task } from "../../../../../domain/Task";
import { Status } from "../../../../../domain/Status";
import api from "../../../../../api/api";
import { useException } from "../../../../../hooks/ExceptionProvider";
import { useEffect, useState } from "react";
import { ArrowsDiagonal, Plus, Trash } from "tabler-icons-react";
import { Button } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import KanbanBoard from "../../../../../components/KanbanBoard/KanbanBoard";

const ProjectTasksBoardView = (): JSX.Element => {
  const { handleException } = useException();
  const { tasks, mutateTasks } = useProject();
  const { taskStatuses } = useTaskStatus();
  const [localTasks, setLocalTasks] = useState<Task[]>(tasks || []);
  const navigate = useNavigate();

  const changeTaskStatus = async (task: Task, newStatus: Status) => {
    // optimistic update
    setLocalTasks(
      localTasks.map((t) =>
        t.id === task.id ? { ...t, status: newStatus } : t
      )
    );

    try {
      await api.put(`tasks/${task?.id}/status`, {
        statusValue: newStatus.value,
      });
      mutateTasks();
    } catch (exception) {
      handleException(exception, undefined, true);
      mutateTasks();
    }
  };

  useEffect(() => {
    setLocalTasks(tasks || []);
  }, [tasks]);

  return (
    <div>
      <KanbanBoard
        columns={taskStatuses}
        items={localTasks.sort((a, b) => a.id - b.id)}
        renderItemContent={(task) => {
          return (
            <div className="flex items-center justify-between gap-m card p-s">
              <div>{task.summary}</div>
              <div className="flex items-center gap-s">
                <ArrowsDiagonal
                  className="cursor-pointer hover:text-action invisible group-hover:visible scale-0 group-hover:scale-100 transition-all duration-200 ease-out"
                  onClick={() => navigate(`../${task.id}`)}
                />
                <Trash className="cursor-pointer hover:text-danger invisible group-hover:visible scale-0 group-hover:scale-100 transition-all duration-200 ease-out" />
              </div>
            </div>
          );
        }}
        renderColumnHeader={(status) => {
          return (
            <div className="card p-m font-bold fnt-md text-center">
              {status.name}
            </div>
          );
        }}
        renderColumnFooter={(status) => {
          return (
            <Button
              size="xl"
              radius="md"
              className="w-full bg-primary bg-opacity-0 text-[#a3a3a3] btn-attract transition-all duration-200 ease-linear"
              leftSection={<Plus className="size-l" />}
            >
              New task
            </Button>
          );
        }}
        filterItem={(task, taskStatus) => {
          return task.status.value === taskStatus.value;
        }}
        onDragEnd={changeTaskStatus}
      />
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectTasksBoardView;
