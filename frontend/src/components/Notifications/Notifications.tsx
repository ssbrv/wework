import { notifications } from "@mantine/notifications";

// autoclose, green background
export const goodNotification = (
  notificationTitle?: string,
  notificationMessage?: string
) => {
  notifications.show({
    color: "green",
    title: notificationTitle,
    message: notificationMessage,
    radius: "md",
    autoClose: 4000,
    style: { backgroundColor: "#d9ffde" },
  });
};

// autoclose, white background
export const okNotification = (
  notificationTitle?: string,
  notificationMessage?: string
) => {
  notifications.show({
    title: notificationTitle,
    message: notificationMessage,
    radius: "md",
    autoClose: 4000,
  });
};

// no autoclose, pink background
export const badNotification = (
  notificationTitle?: string,
  notificationMessage?: string
) => {
  notifications.show({
    color: "red",
    title: notificationTitle,
    message: notificationMessage,
    radius: "md",
    style: { backgroundColor: "#fff1f0" },
    autoClose: 10000,
  });
};
