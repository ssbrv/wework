import { Task } from "../../domain/Task";
import { useFetch } from "../useFetch";

const TASKS_API_BASE_URL = "tasks";

export const useTasks = (signal?: AbortSignal) => {
  return useFetch<Task[]>(TASKS_API_BASE_URL, signal);
};
