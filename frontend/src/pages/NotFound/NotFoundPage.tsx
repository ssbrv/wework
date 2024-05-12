import { Button, Stack, Title } from "@mantine/core";
import { WeWorkLogo } from "../../components/WeWorkLogo/WeWorkLogo";
import { MoodSad } from "tabler-icons-react";

const NotFoundPage = (): JSX.Element => {
  return (
    <div className="min-h-screen flex p-4 justify-center items-center bg-gradient-to-br from-gradient-from to-gradient-to">
      <div className="max-w-[30rem] w-full bg-secondary card-lg p-8 flex flex-col gap-8">
        <Stack className="gap-6">
          <WeWorkLogo />
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
        </Stack>
      </div>
    </div>
  );
};

export default NotFoundPage;
