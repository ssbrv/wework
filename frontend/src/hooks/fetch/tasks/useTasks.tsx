import { TASKS_API_BASE_URL } from "../../../api/tasks";
import { Task } from "../../../domain/Task";
import { useFetchArray } from "../useFetch";

export const useTasksByProjectId = (projectId: number, signal?: AbortSignal) =>
  useTasks(`project/${projectId}`, signal);

export const useAuthoredTasks = (signal?: AbortSignal) =>
  useTasks(`authored`, signal);

export const useAssignedTasks = (signal?: AbortSignal) =>
  useTasks(`assigned`, signal);

export function useTasks(urlAddon?: string, signal?: AbortSignal) {
  const url = urlAddon
    ? `${TASKS_API_BASE_URL}/${urlAddon}`
    : TASKS_API_BASE_URL;

  const { data, error, isLoading, mutate } = useFetchArray<Task>(url, signal);

  return {
    tasks: data,
    error,
    isLoading,
    mutate,
  };
}
