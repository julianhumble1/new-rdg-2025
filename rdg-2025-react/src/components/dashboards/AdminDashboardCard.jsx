import { useNavigate } from "react-router-dom";
import Button from "../common/Button.jsx";
import {
  ArrowRightIcon,
  PlusCircleIcon,
  PlusIcon,
} from "@heroicons/react/16/solid";

const AdminDashboardCard = ({
  name,
  basePath,
  showSeeAll = true,
  icon,
  showAddNew = true,
  customButton,
}) => {
  const navigate = useNavigate();

  return (
    <div className="bg-slate-100 hover:bg-slate-200 rounded p-3 m-2 flex group">
      <div className="flex flex-col h-auto min-w-24">
        <div className="text-sky-900 w-16 h-16 mx-auto group-hover:scale-110">
          {icon}
        </div>
        <div className="mx-auto font-bold">{name}</div>
      </div>
      <div className="w-full mx-4 flex flex-col gap-2 justify-center">
        {showAddNew && (
          <Button onClick={() => navigate(`/${basePath}/new`)}>
            Add New {name !== "People" ? name.slice(0, -1) : "Person"}
            <PlusIcon className="w-4 h-4 inline ml-2" />
          </Button>
        )}
        {showSeeAll && (
          <Button onClick={() => navigate(`/${basePath}`)}>
            See All {name}
            <ArrowRightIcon className="w-4 h-4 inline ml-2" />
          </Button>
        )}
        {customButton}
      </div>
    </div>
  );
};

export default AdminDashboardCard;
