import { FieldValues, Path, UseFormSetError } from "react-hook-form";
import { badNotification } from "../../components/Notifications/Notifications";

// Define the ExceptionHandler object
const ExceptionHandler = {
  handleException: <T extends FieldValues>(
    setError: UseFormSetError<T>,
    error: any
  ) => {
    // no response and no request
    if (!error.response && !error.request) {
      badNotification(
        "There is something wrong with your request!",
        "Please, try again"
      );
      console.error("Client error: ", error);
      return;
    }

    // no response
    if (!error.response) {
      badNotification(
        "Internal server error occured!",
        "Please, try again later"
      );
      console.error("Server error: ", error);
      return;
    }

    // unexpected response fromat
    if (
      !error.response.data ||
      (!error.response.data.errors && !error.response.data.message)
    ) {
      badNotification(
        "An error occured!",
        "Unfortenutelly, we can't tell you why..."
      );
      console.error("Can't parse an error: ", error.response);
      return;
    }

    // no error list, but there is a message
    if (!error.response.data.errors) {
      badNotification("An error occured!", error.response.data.message);
      return;
    }

    // error list
    const errors = error.response.data.errors;
    errors.forEach((error: { field: Path<T>; defaultMessage: string }) => {
      setError(error.field, {
        type: "manual",
        message: error.defaultMessage,
      });
    });
  },
};

export default ExceptionHandler;
