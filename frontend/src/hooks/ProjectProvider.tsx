import { createContext, useContext, useEffect, useMemo, useState } from "react";
import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import { Outlet, useParams } from "react-router-dom";
import { Project } from "../domain/Project";
import { Loader } from "@mantine/core";

interface ProjectContextProps {
  project: Project | undefined;
  mutate: KeyedMutator<Project>;
}

const ProjectContext = createContext<ProjectContextProps | undefined>(
  undefined
);

const ProjectProvider = (): JSX.Element => {
  const { projectId } = useParams<{ projectId: string }>();
  const { handleException } = useException();
  const [projectLoaded, setProjectLoaded] = useState(false);

  const {
    data: project,
    error,
    mutate,
  } = useSWR<Project>(`projects/${projectId}`, getFetcher);

  useEffect(() => {
    if (project) setProjectLoaded(true);
  }, [project]);

  if (error) {
    console.log("The exception was caught while fetching data from users/id");
    handleException(error, undefined, true);
  }

  const contextValue = useMemo(() => ({ project, mutate }), [mutate, project]);
  return (
    <ProjectContext.Provider value={contextValue}>
      {projectLoaded ? (
        <Outlet />
      ) : (
        <div className="min-h-screen flex justify-center items-center">
          <Loader />
        </div>
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
