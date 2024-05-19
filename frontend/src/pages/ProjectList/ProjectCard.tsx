import { Text } from "@mantine/core";
import { useNavigate } from "react-router-dom";
import { Project } from "../../domain/Project";
import { Users } from "tabler-icons-react";

export const ProjectCard = (project: Project): JSX.Element => {
  const navigate = useNavigate();

  return (
    <div
      className="card p-m h-[250px] hover:bg-hover hover:cursor-pointer transition-all ease-linear duration-200 flex flex-col gap-s"
      onClick={() => {
        navigate(`/projects/${project.id}/details`);
      }}
    >
      <div className="flex justify-between gap-xs">
        <div className="font-bold fnt-md">{project.name}</div>
        <div className="mt-auto ml-auto flex gap-xs items-center">
          <div>{project.memberCount}</div>
          <Users />
        </div>
      </div>

      <Text lineClamp={6}>{project.description}</Text>
    </div>
  );
};
