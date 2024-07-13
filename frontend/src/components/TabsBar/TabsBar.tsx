import { Tabs } from "@mantine/core";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export interface TabsBarTab {
  icon?: React.ReactNode;
  name: string;
  link: string;
}

interface TabsBarProps {
  tabs: TabsBarTab[];
  linkLevel: number;
  defaultTab?: string;
}

export const TabsBar = ({
  tabs,
  linkLevel,
  defaultTab = "",
}: TabsBarProps): JSX.Element => {
  const navigate = useNavigate();
  let path = useLocation().pathname.split("/");

  const [selectedTab, setSelectedTab] = useState<string>(() => {
    if (path.length === linkLevel) return defaultTab;
    if (path.length > linkLevel) return path[linkLevel];
    throw new Error(
      "TabsBar component was not properly used: provided path " +
        path +
        " is shorter than linkLevel of " +
        linkLevel
    );
  });

  useEffect(() => {
    if (path[linkLevel] === selectedTab) return;
    navigate(`./${selectedTab}`);
  }, [selectedTab, path, linkLevel, navigate]);

  return (
    <Tabs defaultValue={selectedTab} activateTabWithKeyboard={false}>
      <Tabs.List>
        {tabs.map((tab) => (
          <Tabs.Tab
            key={tab.link}
            value={tab.link}
            leftSection={tab.icon}
            onClick={() => {
              setSelectedTab(tab.link);
            }}
          >
            {tab.name}
          </Tabs.Tab>
        ))}
      </Tabs.List>
    </Tabs>
  );
};
