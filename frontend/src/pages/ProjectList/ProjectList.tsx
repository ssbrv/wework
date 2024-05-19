import { Button, Modal, Skeleton, TextInput, Tooltip } from "@mantine/core";
import { Header } from "../../components/Header/Header";
import { Search } from "tabler-icons-react";
import { ProjectCard } from "./ProjectCard";
import { SrcollUpAffix } from "../../components/Affix/ScrollUpAffix";
import useSWR from "swr";
import { Project } from "../../domain/Project";
import { getFetcher } from "../../api/fetchers";
import { useException } from "../../hooks/ExceptionProvider";
import { useDisclosure } from "@mantine/hooks";
import CreateProjectForm from "./CreateProjectForm";
import { useState } from "react";
import { CloseButton } from "@mantine/core";

const ProjectList = (): JSX.Element => {
  const { handleException } = useException();
  const [newProjectOpened, { open: openNewProject, close: closeNewProject }] =
    useDisclosure(false);
  const [searchTerm, setSearchTerm] = useState("");

  const {
    data: projects,
    error,
    mutate,
  } = useSWR<Project[]>("projects", getFetcher);

  if (error) {
    console.log("The exception was caught while fetching data from projects");
    handleException(error, undefined, true);
  }

  const filteredProjects = projects?.filter((project) =>
    project.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="p-l flex flex-col gap-m">
      <Header
        name="Projects"
        controls={[
          <Tooltip
            label="Remove filter"
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
            size="md"
            radius="md"
            placeholder="Search by name"
            leftSection={<Search />}
            variant="filled"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.currentTarget.value)}
          />,
          <Button size="md" radius="md" onClick={openNewProject}>
            Create new project
          </Button>,
        ]}
      />

      {projects ? (
        filteredProjects && filteredProjects.length > 0 ? (
          <div className="grid grid-cols-3 gap-m max-sm:grid-cols-1 max-md:grid-cols-2">
            {filteredProjects?.map((project) => (
              <ProjectCard
                key={project.id}
                name={project.name}
                description={project.description}
                id={project.id}
                status={project.status}
                memberCount={project.memberCount}
              />
            ))}
          </div>
        ) : (
          <div className="mt-md text-center fnt-lg">
            Don't have any projects yet? Create one!
          </div>
        )
      ) : (
        <div className="grid grid-cols-3 gap-m max-sm:grid-cols-1 max-md:grid-cols-2">
          <Skeleton className="h-[250px] card" />
          <Skeleton className="h-[250px] card" />
          <Skeleton className="h-[250px] card" />
          <Skeleton className="h-[250px] card" />
          <Skeleton className="h-[250px] card" />
          <Skeleton className="h-[250px] card" />
        </div>
      )}

      <SrcollUpAffix />

      <Modal
        onClose={() => {
          closeNewProject();
          mutate();
        }}
        opened={newProjectOpened}
        withCloseButton={false}
        centered
        radius="md"
        size="xl"
      >
        <CreateProjectForm
          onClose={() => {
            closeNewProject();
            mutate();
          }}
        />
      </Modal>
    </div>
  );
};

export default ProjectList;
