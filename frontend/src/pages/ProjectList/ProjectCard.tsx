import { Text } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { Project } from "../../domain/Project";
import { Users } from "tabler-icons-react";

interface Props {
  project: Project;
}

export const ProjectCard = ({ project }: Props): JSX.Element => {
  const navigate = useNavigate();

  return (
    <div
      className="card p-m h-[235px] hover:bg-hover hover:cursor-pointer transition-all ease-linear duration-200 flex flex-col gap-s"
      onClick={() => {
        navigate(`/projects/${project.id}/details`);
      }}
    >
      <div className="flex justify-between gap-xs">
        <div className="font-bold fnt-md">{project.name}</div>
        <div className="flex gap-xs">
          <div>{project.memberCount}</div>
          <Users />
        </div>
      </div>
      <Text lineClamp={6} className="whitespace-pre text-wrap">
        {project.description}
      </Text>
    </div>
  );
};
