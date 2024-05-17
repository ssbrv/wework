import { useNavigate } from "react-router-dom";
import {
  Color,
  hoverTextColorMapper,
  textColorMapper,
} from "../../styles/TypesAndMappers";

interface NavigationBarItemProps {
  icon: React.ReactNode;
  label?: string;
  link?: string;
  selected?: boolean;
  onSelect?: (option: string) => void;
  onClick?: () => void;
  color?: Color;
}

export const NavigationBarItem = ({
  icon,
  label,
  link,
  selected = false,
  onSelect,
  color = "action_light",
  onClick,
}: NavigationBarItemProps) => {
  const navigate = useNavigate();
  return (
    <div
      className={`flex flex-shrink-0 h-12 items-center gap-s overflow-hidden ${
        (onClick || onSelect || link) &&
        "hover:cursor-pointer " + hoverTextColorMapper[color]
      } ${selected ? textColorMapper[color] : "text-background"}`}
      onClick={() => {
        if (onClick) onClick();
        if (onSelect && link) onSelect(link);
        if (link) navigate(link);
      }}
    >
      {icon}
      <div className="flex-shrink-0 text-md font-normal">{label}</div>
    </div>
  );
};
