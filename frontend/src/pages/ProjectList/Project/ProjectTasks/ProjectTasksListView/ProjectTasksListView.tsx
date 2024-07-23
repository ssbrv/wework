import { SrcollUpAffix } from "../../../../../components/Affix/ScrollUpAffix";
import { useLocation, useNavigate } from "react-router-dom";
import { useProject } from "../../../../../providers/ProjectProvider";
import { List } from "../../../../../components/List/List";
import { useEffect, useState } from "react";
import { Select } from "@mantine/core";
import useTaskStatus from "../../../../../hooks/useTaskStatus";

const ProjectTasksListView = (): JSX.Element => {
  const navigate = useNavigate();
  const location = useLocation();
  const { tasks } = useProject();
  const { taskStatuses: statuses } = useTaskStatus();

  const searchParams = new URLSearchParams(location.search);
  const [statusValue, setStatusValue] = useState(
    searchParams.get("statusValue")
  );

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    if (statusValue) params.set("statusValue", statusValue);
    else params.delete("statusValue");
    navigate({ search: params.toString() }, { replace: true });
  }, [statusValue, navigate]);

  const filteredTasks =
    statusValue === null
      ? tasks
      : tasks?.filter((task) => {
          return task.status.value === statusValue;
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
            value={statusValue}
            onChange={(value) => setStatusValue(value)}
            size="md"
            radius="md"
            variant="filled"
            data={statuses.map((status) => ({
              value: status.value,
              label: status.name,
            }))}
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
