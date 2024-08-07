import { Header } from "../../components/Header/Header";
import { Button, Group, Modal, Tooltip } from "@mantine/core";
import { ButtonBar } from "../../components/ButtonBar/ButtonBar";
import { goodNotification } from "../../components/Notifications/Notifications";
import BasicInformation from "./BasicInformation";
import Credentials from "./Credentials";
import { useDisclosure } from "@mantine/hooks";
import { useUser } from "../../providers/UserProvider";
import { useNavigate } from "react-router-dom";
import { ArrowBackUp } from "tabler-icons-react";
import { displayError } from "../../utils/displayError";
import { deleteAccount, fullLogout } from "../../api/auth/authApi";

const ProfilePage = (): JSX.Element => {
  const { isItMe } = useUser();
  const navigate = useNavigate();

  const [fullLogoutOpened, { open: openFullLogout, close: closeFullLogout }] =
    useDisclosure(false);

  const [
    deleteAccountOpened,
    { open: openDeleteAccount, close: closeDeleteAccout },
  ] = useDisclosure(false);

  async function handleFullLogout(): Promise<void> {
    await fullLogout()
      .then(function () {
        goodNotification(
          "Successfully logged out from all outher devices!",
          "This session is saved, no need to reauthenticate"
        );
      })
      .catch(function (exception) {
        displayError(exception, undefined, true);
      });
  }

  return (
    <div className="p-l flex flex-col gap-m">
      <Header
        name={isItMe ? "My profile" : "Profile"}
        controls={[
          <Tooltip
            label="Go back"
            position="bottom"
            offset={10}
            withArrow
            arrowSize={8}
            arrowRadius={4}
            openDelay={800}
          >
            <div
              className="flex hover:text-action hover:cursor-pointer transition-all duration-200 ease-out"
              onClick={() => navigate(-1)}
            >
              <ArrowBackUp className="size-l" />
            </div>
          </Tooltip>,
        ]}
      />

      <div className="flex gap-m flex-wrap">
        <BasicInformation />
        <Credentials />
      </div>

      {isItMe && (
        <ButtonBar>
          <Button
            radius="md"
            size="md"
            className="btn-danger"
            onClick={openDeleteAccount}
          >
            Delete account
          </Button>
          <Button
            radius="md"
            size="md"
            className="btn-danger"
            onClick={openFullLogout}
          >
            Logout from all other devices
          </Button>
        </ButtonBar>
      )}

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
              size="md"
              radius="md"
              className="flex-1 btn-attract"
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

      <Modal
        opened={deleteAccountOpened}
        onClose={closeDeleteAccout}
        centered
        radius="md"
        withCloseButton={false}
      >
        <div className="flex flex-col gap-m p-xs">
          <div className="fnt-md font-bold">Account deletion</div>
          <div className="">
            Are you sure you want to delete your account from this system?
          </div>
          <Group className="gap-m">
            <Button
              size="md"
              radius="md"
              className="flex-1 btn-attract"
              onClick={closeDeleteAccout}
            >
              Cancel
            </Button>
            <Button
              radius="md"
              size="md"
              className="flex-1 btn-danger"
              onClick={async () => {
                await deleteAccount().then(function () {
                  goodNotification("Your account was successfully deleted!");
                  navigate("/login");
                });
              }}
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
