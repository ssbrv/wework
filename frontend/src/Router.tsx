import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthProvider from "./AuthProvider";
import WelcomePage from "./pages/Welcome/WelcomePage";

export const Router = (): JSX.Element => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<WelcomePage />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};
