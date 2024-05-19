import { useProject } from "../../../../hooks/ProjectProvider";

const ProjectSettings = (): JSX.Element => {
  const { project } = useProject();

  return (
    <div className="flex flex-col gap-m">
      <div className="card h-[100px]"></div>
    </div>
  );
};

export default ProjectSettings;
