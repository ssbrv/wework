import { Stack } from "@mantine/core";
import { WeWorkLogo } from "../WeWorkLogo/WeWorkLogo";

interface Props {
  children?: React.ReactNode;
  navigation?: React.ReactNode;
}

const PreAuth = ({ children, navigation }: Props): JSX.Element => {
  return (
    <div className="min-h-screen flex p-4 justify-center items-center bg-gradient-to-br from-gradient-from to-gradient-to">
      <div className="max-w-[30rem] w-full card-lg bg-secondary p-8 flex flex-col gap-8">
        <WeWorkLogo />
        <Stack className="gap-6">{children}</Stack>
      </div>
    </div>
  );
};

export default PreAuth;
