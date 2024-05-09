import { Navigate } from "react-router-dom";

const WelcomePage = (): JSX.Element => {
  return <Navigate to="login" replace={true} />;
};

export default WelcomePage;
