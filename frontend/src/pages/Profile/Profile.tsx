import { useAuth } from "../../AuthProvider";
import LgCardWithLogoOnGradientBackground from "../../components/LgCardWithLogoOnGradientBackground/LgCardWithLogoOnGradientBackground";
import { Button } from "@mantine/core";
import { useNavigate } from "react-router-dom";

const ProfilePage = (): JSX.Element => {
  const { setToken } = useAuth();
  const navigate = useNavigate();

  function logout(): void {
    setToken(null);
    navigate("/login");
  }

  return (
    <LgCardWithLogoOnGradientBackground>
      <Button radius="md" size="md" color="pink" onClick={logout}>
        Log out
      </Button>
    </LgCardWithLogoOnGradientBackground>
  );
};

export default ProfilePage;
