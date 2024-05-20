import { createContext, useContext, useMemo } from "react";
import useSWR, { KeyedMutator } from "swr";
import { getFetcher } from "../api/fetchers";
import { useException } from "./ExceptionProvider";
import { Outlet, useParams } from "react-router-dom";
import { Loader } from "@mantine/core";
import { Task } from "../domain/Task";

interface TaskContextProps {
  task: Task | undefined;
  mutate: KeyedMutator<Task>;
}

const TaskContext = createContext<TaskContextProps | undefined>(undefined);

const TaskProvider = (): JSX.Element => {
  const { taskId } = useParams<{ taskId: string }>();
  const { handleException } = useException();

  const {
    data: task,
    error,
    mutate,
    isLoading,
  } = useSWR<Task>(`tasks/${taskId}`, getFetcher);

  if (error) {
    console.log(
      "The exception was caught while fetching data from tasks/taskId"
    );
    handleException(error, undefined, true);
  }

  const contextValue = useMemo(() => ({ task, mutate }), [mutate, task]);
  return (
    <TaskContext.Provider value={contextValue}>
      {isLoading ? (
        <div className="min-h-screen flex justify-center items-center">
          <Loader />
        </div>
      ) : (
        <Outlet />
      )}
    </TaskContext.Provider>
  );
};

export const useTask = (): TaskContextProps => {
  const context = useContext(TaskContext);
  if (!context) throw new Error("useTask must be used within TaskProvider");
  return context;
};

export default TaskProvider;
