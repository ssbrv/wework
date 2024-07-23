import { Loader } from "@mantine/core";

interface LoadingScreenProps {
  children: React.ReactNode;
}

const LoadingScreen = ({ children }: LoadingScreenProps): JSX.Element => {
  return (
    <div className="h-screen flex items-center justify-center">
      <div className="flex gap-m items-center">
        <Loader type="bars" className=" size-l" />
        {children}
      </div>
    </div>
  );
};

export default LoadingScreen;
