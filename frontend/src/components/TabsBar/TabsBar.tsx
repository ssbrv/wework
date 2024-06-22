import { Tabs } from "@mantine/core";
import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

export interface TabsBarTab {
  icon?: React.ReactNode;
  name: string;
  link: string;
}

interface TabsBarProps {
  tabs: TabsBarTab[];
  linkLevel: number;
}

export const TabsBar = ({ tabs, linkLevel }: TabsBarProps): JSX.Element => {
  const navigate = useNavigate();
  const location = useLocation();
  const [selectedTab, setSelectedTab] = useState<string>(
    location.pathname.split("/")[linkLevel]
  );
  console.log(selectedTab);
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
