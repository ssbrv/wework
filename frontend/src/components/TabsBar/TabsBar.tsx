import { Tabs } from "@mantine/core";
import { useNavigate } from "react-router-dom";

export interface TabsBarTab {
  icon?: React.ReactNode;
  link: string;
  name: string;
}

interface TabsBarProps {
  tabs: TabsBarTab[];
  defaultTabName: string;
}

export const TabsBar = ({
  tabs,
  defaultTabName,
}: TabsBarProps): JSX.Element => {
  const navigate = useNavigate();

  return (
    <Tabs defaultValue={defaultTabName} activateTabWithKeyboard={false}>
      <Tabs.List>
        {tabs.map((tab) => (
          <Tabs.Tab
            key={tab.name}
            value={tab.name}
            leftSection={tab.icon}
            onClick={() => {
              navigate(`./${tab.link}`);
            }}
          >
            {tab.name}
          </Tabs.Tab>
        ))}
      </Tabs.List>
    </Tabs>
  );
};
