import { Button, Title } from "@mantine/core";
import { MoodSad } from "tabler-icons-react";
import PreAuth from "../../components/PreAuth/PreAuth";

const NotFoundPage = (): JSX.Element => {
  return (
    <PreAuth>
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
    </PreAuth>
  );
};

export default NotFoundPage;
