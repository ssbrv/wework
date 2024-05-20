import { createContext, useContext, useMemo } from "react";
import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import { Outlet, useParams } from "react-router-dom";
import { Project } from "../domain/Project";
import { Loader } from "@mantine/core";
import { Task } from "../domain/Task";

interface ProjectContextProps {
  project: Project | undefined;
  tasks: Task[] | undefined;
  mutate: KeyedMutator<Project>;
  mutateTasks: KeyedMutator<Task[]>;
}

const ProjectContext = createContext<ProjectContextProps | undefined>(
  undefined
);

const ProjectProvider = (): JSX.Element => {
  const { projectId } = useParams<{ projectId: string }>();
  const { handleException } = useException();

  const {
    data: project,
    error,
    mutate,
    isLoading,
  } = useSWR<Project>(`projects/${projectId}`, getFetcher);

  if (error) {
    console.log(
      "The exception was caught while fetching data from projects/projectId"
    );
    handleException(error, undefined, true);
  }

  const {
    data: tasks,
    error: errorTasks,
    mutate: mutateTasks,
    isLoading: isLoadingTasks,
  } = useSWR<Task[]>(`tasks/projects/${projectId}`, getFetcher);

  const contextValue = useMemo(
    () => ({ tasks, project, mutate, mutateTasks }),
    [mutate, mutateTasks, project, tasks]
  );
  return (
    <ProjectContext.Provider value={contextValue}>
      {isLoading ? (
        <div className="min-h-screen flex justify-center items-center">
          <Loader />
        </div>
      ) : (
        <Outlet />
      )}
    </ProjectContext.Provider>
  );
};

export const useProject = (): ProjectContextProps => {
  const context = useContext(ProjectContext);
  if (!context)
    throw new Error("useProject must be used within ProjectProvider");
  return context;
};

export default ProjectProvider;
