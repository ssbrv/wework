import { FieldValues, Path, UseFormSetError } from "react-hook-form";
import { badNotification } from "../components/Notifications/Notifications";
import { logout } from "../api/auth/authApi";

export const displayError = <T extends FieldValues>(
  error: any,
  setError?: UseFormSetError<T>,
  logoutOnForbidden?: boolean
): void => {
  if (!error.request) {
    badNotification(
      "There is something wrong with your request!",
      "Please, try again"
    );
    console.error("Client error: ", error);
    return;
  }

  if (!error.response) {
    badNotification(
      "Internal server error occured!",
      "Please, try again later"
    );
    console.error("Server error: ", error);
    return;
  }

  if (
    !error.response.data ||
    (!error.response.data.errors && !error.response.data.message)
  ) {
    badNotification("Unexpected error occured!", "Please, try again later");
    console.error("Can't parse an error: ", error.response);
    return;
  }

  if (logoutOnForbidden && error.response.status === 403) {
    badNotification(
      "Your session has been expired!",
      "Please, log in and try again"
    );
    console.log("Forbidden. Force the logout");
    logout();
    return;
  }

  if (!error.response.data.errors || !setError || setError === undefined) {
    badNotification("An error occured!", error.response.data.message);
    return;
  }

  const errors = error.response.data.errors;
  errors.forEach((error: { field: Path<T>; defaultMessage: string }) => {
    setError(error.field, {
      type: "manual",
      message: error.defaultMessage,
    });
  });
};
