import { Button, Title } from "@mantine/core";
import { MoodSad } from "tabler-icons-react";
import LgCardWithLogoOnGradientBackground from "../../components/LgCardWithLogoOnGradientBackground/LgCardWithLogoOnGradientBackground";

const NotFoundPage = (): JSX.Element => {
  return (
    <LgCardWithLogoOnGradientBackground>
      <div className="flex items-center w-full gap-4">
        <MoodSad className="size-12 text-danger" />
        <Title order={2}>The page was not found...</Title>
      </div>
      <Button radius="md" size="md" component="a" href="login">
        Go to login
      </Button>
      <Button radius="md" size="md" component="a" href="profile">
        Go to my profile
      </Button>
    </LgCardWithLogoOnGradientBackground>
  );
};

export default NotFoundPage;
