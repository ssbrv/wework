import { SrcollUpAffix } from "../../../../../components/Affix/ScrollUpAffix";
import { useLocation, useNavigate } from "react-router-dom";
import { useProject } from "../../../../../hooks/ProjectProvider";
import { List } from "../../../../../components/List/List";
import { useEffect, useState } from "react";
import { Select } from "@mantine/core";

const ProjectTasksListView = (): JSX.Element => {
  const navigate = useNavigate();
  const location = useLocation();
  const { tasks } = useProject();

  // Extract status from query parameters or default to "TODO"
  const searchParams = new URLSearchParams(location.search);
  const initialStatus = searchParams.get("status");

  const [status, setStatus] = useState(initialStatus);

  useEffect(() => {
    // Update the query parameter whenever the status changes
    const params = new URLSearchParams(location.search);
    if (status) params.set("status", status);
    else params.delete("status");
    navigate({ search: params.toString() }, { replace: true });
  }, [status, navigate]);

  const filteredTasks =
    status === null
      ? tasks
      : tasks?.filter((task) => {
          return task.status === status;
        });

  const transformedTasks = filteredTasks
    ?.map((task) => ({
      id: task.id,
      name: task.summary,
    }))
    .sort((a, b) => a.id - b.id);

  return (
    <div>
      <List
        list={transformedTasks}
        onItemClick={(item) => navigate(`../${item.id}`)}
        title="List view"
        controls={[
          <Select
            value={status}
            onChange={(value) => setStatus(value)}
            size="md"
            radius="md"
            variant="filled"
            data={[
              { label: "To-do", value: "TODO" },
              { label: "In progress", value: "IN_PROGRESS" },
              { label: "Completed", value: "COMPLETED" },
            ]}
            styles={{
              input: {
                borderColor: "transparent",
              },
            }}
            clearable
            placeholder="All tasks"
          />,
        ]}
      />
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectTasksListView;
