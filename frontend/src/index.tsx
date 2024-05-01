import ReactDOM from "react-dom/client";
import "./index.css";
import { Router } from "./Router";
import { MantineProvider } from "@mantine/core";
import { ModalsProvider } from "@mantine/modals";
import { Notifications } from "@mantine/notifications";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);
root.render(
  <MantineProvider>
    <ModalsProvider>
      <Notifications position="top-right" />
      <Router />
    </ModalsProvider>
  </MantineProvider>
);
