import {
  CloseButton,
  List,
  SegmentedControl,
  TextInput,
  Tooltip,
} from "@mantine/core";
import { Header } from "../../components/Header/Header";
import { SrcollUpAffix } from "../../components/Affix/ScrollUpAffix";
import { useState } from "react";
import useSWR from "swr";
import { Task } from "../../domain/Task";
import { getFetcher } from "../../api/fetchers";
import { useException } from "../../hooks/ExceptionProvider";
import { Project } from "../../domain/Project";
import { Search } from "tabler-icons-react";
import { TaskStatus } from "../../domain/enumerations/Enumerations";
import { useNavigate } from "react-router-dom";
import { ArrowsDiagonal } from "tabler-icons-react";

interface GroupedByProjectTasks {
  tasks: Task[];
  project: Project;
}

interface GroupedByStatusTasks {
  tasks: Task[];
  status: TaskStatus;
}

const TaskList = (): JSX.Element => {
  const navigate = useNavigate();
  const [type, setType] = useState("Authored");
  const [groupping, setGroupping] = useState("project");
  const { handleException } = useException();
  const [searchTerm, setSearchTerm] = useState("");

  const { data: authoredTasks, error: errorAuthoredTasks } = useSWR<Task[]>(
    "tasks/authored",
    getFetcher
  );

  if (errorAuthoredTasks) handleException(errorAuthoredTasks, undefined, true);

  const { data: assignedTasks, error: errorAssignedTasks } = useSWR<Task[]>(
    "tasks/assigned",
    getFetcher
  );

  if (errorAssignedTasks) handleException(errorAssignedTasks, undefined, true);

  const { data: projects, error: errorProjects } = useSWR<Project[]>(
    "projects",
    getFetcher
  );

  if (errorProjects) handleException(errorProjects, undefined, true);

  const tasks = type === "Authored" ? authoredTasks : assignedTasks;

  const filteredTasks = tasks?.filter((task) =>
    task.summary.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const groupTasksByProject = (): GroupedByProjectTasks[] => {
    if (!projects || !filteredTasks) return [];
    return projects
      .map((project) => ({
        project,
        tasks: filteredTasks.filter((task) => task.project.id === project.id),
      }))
      .filter((group) => group.tasks.length > 0);
  };

  const groupTasksByStatus = (): GroupedByStatusTasks[] => {
    const statuses: TaskStatus[] = ["TODO", "IN_PROGRESS", "COMPLETED"];
    return statuses
      .map((status) => ({
        status,
        tasks: filteredTasks?.filter((task) => task.status === status) || [],
      }))
      .filter((group) => group.tasks.length > 0);
  };

  const renderTasks = () => {
    if (groupping === "project") {
      const groupedByProject = groupTasksByProject();
      return groupedByProject.map((group) => (
        <div
          key={group.project.id}
          className="card p-m flex flex-col gap-s min-h-[250px]"
        >
          <div className="flex gap-s items-center justify-between">
            <div className="fnt-lg font-bold text-center">
              {group.project.name}
            </div>
            <ArrowsDiagonal
              className="hover:cursor-pointer hover:text-action transition-all ease-linear duration-100"
              onClick={() => {
                navigate(`/projects/${group.project.id}/details`);
              }}
            />
          </div>

          <List>
            {group.tasks.map((task) => (
              <List.Item
                key={task.id}
                className="hover:bg-hover rounded hover:cursor-pointer p-xs transition-all duration-100 ease-linear"
                onClick={() => {
                  navigate(`/projects/${group.project.id}/tasks/${task.id}`);
                }}
              >
                {task.summary}
              </List.Item>
            ))}
          </List>
        </div>
      ));
    } else if (groupping === "status") {
      const groupedByStatus = groupTasksByStatus();
      return groupedByStatus.map((group) => (
        <div
          key={group.status}
          className="card p-m flex flex-col gap-s min-h-[250px]"
        >
          <div className="fnt-lg font-bold text-center">{group.status}</div>

          <List>
            {group.tasks.map((task) => (
              <List.Item
                key={task.id}
                className="hover:bg-hover rounded hover:cursor-pointer p-xs transition-all duration-100 ease-linear"
                onClick={() => {
                  navigate(`/projects/${task.project.id}/tasks/${task.id}`);
                }}
              >
                {task.summary}
              </List.Item>
            ))}
          </List>
        </div>
      ));
    } else {
      return filteredTasks?.map((task) => (
        <div key={task.id}>{task.summary}</div>
      ));
    }
  };

  return (
    <div className="p-l flex flex-col gap-m">
      <Header
        name="Tasks"
        controls={[
          <Tooltip
            key="clear-search"
            label="Clear out search"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <CloseButton
              onClick={() => {
                setSearchTerm("");
              }}
            />
          </Tooltip>,
          <TextInput
            key="search"
            size="md"
            radius="md"
            placeholder="Search by summary"
            leftSection={<Search />}
            variant="filled"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.currentTarget.value)}
          />,
          <SegmentedControl
            key="group-selector"
            size="md"
            radius="md"
            data={[
              { label: "Group by project", value: "project" },
              { label: "Group by status", value: "status" },
            ]}
            value={groupping}
            onChange={setGroupping}
          />,
          <SegmentedControl
            key="type-selector"
            size="md"
            radius="md"
            data={["Authored", "Assigned"]}
            value={type}
            onChange={setType}
          />,
        ]}
      />
      <div className="grid grid-cols-3 gap-m">{renderTasks()}</div>
      <SrcollUpAffix />
    </div>
  );
};

export default TaskList;
