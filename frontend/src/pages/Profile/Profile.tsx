import { Header } from "../../components/Header/Header";
import { Button, Group, Modal } from "@mantine/core";
import { ButtonBar } from "../../components/ButtonBar/ButtonBar";
import { useExceptionHandler } from "../../hooks/ExceptionHandler";
import { useAuth } from "../../hooks/AuthProvider";
import { goodNotification } from "../../components/Notifications/Notifications";
import BasicInformation from "./BasicInformation";
import Credentials from "./Credentials";
import { useDisclosure } from "@mantine/hooks";

const ProfilePage = (): JSX.Element => {
  const { handleException } = useExceptionHandler();
  const { fullLogout } = useAuth();

  const [fullLogoutOpened, { open: openFullLogout, close: closeFullLogout }] =
    useDisclosure(false);

  async function handleFullLogout(): Promise<void> {
    await fullLogout()
      .then(function () {
        goodNotification(
          "Successfully logged out from all outher devices!",
          "This session is saved, no need to reauthenticate"
        );
      })
      .catch(function (exception) {
        handleException(exception, undefined, true);
      });
  }

  return (
    <div className="p-l flex flex-col gap-m">
      <Header name="Profile" />

      <div className="flex gap-m flex-wrap">
        <BasicInformation />
        <Credentials />
      </div>
      <ButtonBar>
        <Button radius="md" size="md" className="btn-danger">
          Freeze my account
        </Button>
        <Button radius="md" size="md" className="btn-danger">
          Delete my account
        </Button>
        <Button
          radius="md"
          size="md"
          className="btn-danger"
          onClick={openFullLogout}
        >
          Logout all other devices
        </Button>
      </ButtonBar>

      <Modal
        opened={fullLogoutOpened}
        onClose={closeFullLogout}
        centered
        radius="md"
        withCloseButton={false}
      >
        <div className="flex flex-col gap-m p-xs">
          <div className="fnt-md font-bold">Full Logout</div>
          <div className="">
            Are you sure that you want to log yourself out from other devices?
            This session will be saved, but elsewhere a reauthentication will be
            requeried.
          </div>
          <Group className="gap-m">
            <Button
              variant="outline"
              size="md"
              radius="md"
              className="flex-1"
              onClick={closeFullLogout}
            >
              Cancel
            </Button>
            <Button
              radius="md"
              size="md"
              className="flex-1 btn-danger"
              onClick={handleFullLogout}
            >
              Yes
            </Button>
          </Group>
        </div>
      </Modal>
    </div>
  );
};

export default ProfilePage;
