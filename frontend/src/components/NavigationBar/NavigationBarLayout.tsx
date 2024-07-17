import { Outlet, useLocation } from "react-router-dom";
import {
  ChevronsRight,
  Checkbox,
  Logout,
  Album,
  User,
  UserPlus,
} from "tabler-icons-react";
import { useState } from "react";
import { NavigationBarItem } from "./NavigationBarItem";
import { useAuth } from "../../hooks/AuthProvider";
import { WeWorkLogo } from "../WeWorkLogo/WeWorkLogo";
import { goodNotification } from "../Notifications/Notifications";

interface NavigationOption {
  icon: React.ReactNode;
  label: string;
  link: string;
}

const NavigationBarLayout = (): JSX.Element => {
  const { logout, myId } = useAuth();
  const [navigationBarRolled, setNavigationBarRolled] = useState(false);

  function rollNavigationBar(): void {
    setNavigationBarRolled(!navigationBarRolled);
  }

  const location = useLocation();

  const [selectedSection, setSelectedSection] = useState<string>(
    location.pathname.split("/")[1]
  );

  const navigationOptions: NavigationOption[] = [
    {
      icon: <Album className="size-sm flex-shrink-0" />,
      label: "My projects",
      link: "/projects",
    },
    {
      icon: <Checkbox className="size-sm flex-shrink-0" />,
      label: "My tasks",
      link: "/tasks",
    },
    {
      icon: <UserPlus className="size-sm flex-shrink-0" />,
      label: "My invitations",
      link: `/invitations`,
    },
    {
      icon: <User className="size-sm flex-shrink-0" />,
      label: "My profile",
      link: `/users/${myId}/profile`,
    },
  ];

  return (
    <div className="min-h-screen bg-background">
      <div
        className={`${
          navigationBarRolled ? "w-60" : "w-20"
        } hover:w-60 h-screen fixed flex flex-col p-s gap-m bg-secondary rounded-r shadow transition-[width] duration-500 ease-out z-[100] overflow-auto`}
      >
        <NavigationBarItem
          icon={
            <WeWorkLogo logoSize="sm" newLine={false} center={true} gap="s" />
          }
        />
        <NavigationBarItem
          label="Pin the navbar"
          selected={false}
          icon={
            <ChevronsRight
              className={`size-sm flex-shrink-0 ${
                navigationBarRolled ? "rotate-180" : "rotate-0"
              } transition-transform duration-500`}
            />
          }
          onClick={rollNavigationBar}
          color="attract"
        />

        {navigationOptions.map((option) => (
          <NavigationBarItem
            icon={option.icon}
            key={option.link}
            link={option.link}
            label={option.label}
            selected={option.link.includes(selectedSection)}
            onSelect={() => {
              setSelectedSection(option.link);
            }}
          />
        ))}
        <div className="mt-auto">
          <NavigationBarItem
            icon={<Logout className="size-sm flex-shrink-0" />}
            selected={false}
            onClick={() => {
              logout();
              goodNotification("Successfully logged out!");
            }}
            label="Log out"
            color="danger"
          />
        </div>
      </div>
      <div
        className={`${
          navigationBarRolled ? "ml-60" : "ml-20"
        } transition-[margin] duration-500 ease-out`}
      >
        <Outlet />
      </div>
    </div>
  );
};

export default NavigationBarLayout;
