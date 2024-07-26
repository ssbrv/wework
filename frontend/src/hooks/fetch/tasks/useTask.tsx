import { Task } from "../../../domain/Task";
import { TASKS_API_BASE_URL } from "../../../api/tasks";
import { useFetch } from "../useFetch";

export const useTaskById = (id: number, signal?: AbortSignal) =>
  useTask(`${id}`, signal);

export function useTask(urlAddon?: string, signal?: AbortSignal) {
  const url = urlAddon
    ? `${TASKS_API_BASE_URL}/${urlAddon}`
    : TASKS_API_BASE_URL;

  return useFetch<Task>(url, signal);
}
