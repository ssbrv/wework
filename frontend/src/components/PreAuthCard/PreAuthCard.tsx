import { WeWorkLogo } from "../WeWorkLogo/WeWorkLogo";

interface Props {
  children?: React.ReactNode;
}

const PreAuthCard = ({ children }: Props): JSX.Element => {
  return (
    <div className="min-h-screen flex p-m justify-center items-center bg-gradient-to-br from-action_light to-danger_light">
      <div className="max-w-[30rem] w-full card bg-primary p-m flex flex-col gap-m">
        <WeWorkLogo />
        {children}
      </div>
    </div>
  );
};

export default PreAuthCard;
