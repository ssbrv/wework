import { createContext, useCallback, useContext, useMemo } from "react";
import { FieldValues, Path, UseFormSetError } from "react-hook-form";
import { useAuth } from "./AuthProvider";
import { badNotification } from "../components/Notifications/Notifications";

interface ExceptionContextProps {
  handleException: <T extends FieldValues>(
    error: any,
    setError?: UseFormSetError<T>,
    logoutOnForbidden?: boolean
  ) => void;
}

const ExceptionContext = createContext<ExceptionContextProps | undefined>(
  undefined
);

const ExceptionProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const { logout } = useAuth();

  const handleException = useCallback(
    <T extends FieldValues>(
      error: any,
      setError?: UseFormSetError<T>,
      logoutOnForbidden?: boolean
    ): void => {
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

      if (logoutOnForbidden && error.response.status === 403) {
        badNotification("An error occured!", error.response.data.message);
        console.log("Probably, the token is invalid. Force the logout");
        logout();
        return;
      }

      // no error list, but there is a message
      if (!error.response.data.errors || !setError || setError === undefined) {
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
    [logout]
  );

  const contextValue = useMemo(() => ({ handleException }), [handleException]);

  return (
    <ExceptionContext.Provider value={contextValue}>
      {children}
    </ExceptionContext.Provider>
  );
};

export const useExceptionHandler = (): ExceptionContextProps => {
  const context = useContext(ExceptionContext);
  if (!context)
    throw new Error(
      "useExceptionHandler must be used within ExceptionProvider"
    );
  return context;
};

export default ExceptionProvider;
