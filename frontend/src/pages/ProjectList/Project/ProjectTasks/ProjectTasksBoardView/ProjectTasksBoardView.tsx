import { useNavigate } from "react-router-dom";
import { useProject } from "../../../../../hooks/ProjectProvider";
import { List } from "../../../../../components/List/List";
import { SrcollUpAffix } from "../../../../../components/Affix/ScrollUpAffix";

const ProjectTasksBoardView = (): JSX.Element => {
  console.log("ProjectTasksListView");

  const navigate = useNavigate();
  const { tasks } = useProject();

  const transformedTasks = tasks?.map((task) => ({
    id: task.id,
    name: task.summary,
  }));

  return (
    <div>
      <List
        list={transformedTasks}
        onItemClick={(item) => navigate(`../${item.id}`)}
        title="List view"
      />
      <SrcollUpAffix />
    </div>
  );
};

export default ProjectTasksBoardView;
