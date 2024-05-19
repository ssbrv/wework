import { Outlet, useNavigate } from "react-router-dom";
import { Header } from "../../../components/Header/Header";
import { useProject } from "../../../hooks/ProjectProvider";
import { CloseButton, Tooltip } from "@mantine/core";
import { TabsBar, TabsBarTab } from "../../../components/TabsBar/TabsBar";
import { Album, Checkbox, Settings, UserPlus, Users } from "tabler-icons-react";

const tabs: TabsBarTab[] = [
  {
    name: "Details",
    link: "details",
    icon: <Album />,
  },
  {
    name: "Tasks",
    link: "tasks",
    icon: <Checkbox />,
  },
  {
    name: "Members",
    link: "members",
    icon: <Users />,
  },
  {
    name: "Invititaions",
    link: "invitations",
    icon: <UserPlus />,
  },
  {
    name: "Settings",
    link: "settings",
    icon: <Settings />,
  },
];

const ProjectLayout = (): JSX.Element => {
  const navigate = useNavigate();
  const { project } = useProject();

  return (
    <div className="p-l flex flex-col gap-m">
      <Header
        name={project?.name}
        controls={[
          <TabsBar defaultTabName="Details" tabs={tabs} />,
          <Tooltip
            label="Go back to project list"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
          >
            <CloseButton
              onClick={() => {
                navigate("/projects");
              }}
            />
          </Tooltip>,
        ]}
      />
      <Outlet />
    </div>
  );
};

export default ProjectLayout;
