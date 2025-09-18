import { Link, useNavigate } from "react-router-dom";
import Button from "../common/Button.jsx";
import { ArrowRightIcon, PlusCircleIcon, PlusIcon } from "@heroicons/react/16/solid";

const AdminDashboardCard = ({ name, basePath, showSeeAll = true, icon }) => {
  const navigate = useNavigate();

  return (
    <div className="bg-slate-100 hover:bg-slate-200 rounded p-3 m-2 flex">
      <div className="flex flex-col h-auto min-w-24">
        <div className="text-sky-900 w-16 h-16 mx-auto">{icon}</div>
        <div className="mx-auto font-bold">{name}</div>
      </div>
      <div className="w-full mx-4 flex flex-col gap-2 justify-center">
        <Button onClick={() => navigate(`/${basePath}/new`)}>
          Add New {name !== "People" ? name.slice(0, -1) : "Person"}
          <PlusCircleIcon className="w-3 h-3 inline ml-2"/>
        </Button>
        {showSeeAll && (
          <Button
          onClick={() => navigate(`/${basePath}`)}
          >
            See All {name}
          <ArrowRightIcon className="w-3 h-3 inline ml-2" />
          </Button>
        )}
      </div>
    </div>
  );

  return (
    <div className="hover:bg-slate-300 hover:drop-shadow-sm transition col-span-1 w-full">
      <div className="flex justify-center">
        <div className="flex flex-col p-6 my-6 text-lg w-3/4">
          <div className="font-bold py-3 w-full italic">{name}</div>
          <Link
            to={`/${basePath}/new`}
            className="underline text-blue-500 hover:text-blue-700 text-sm"
          >
            Add New {name !== "People" ? name.slice(0, -1) : "Person"}
          </Link>
          {showSeeAll && (
            <Link
              to={`/${basePath}`}
              className="underline text-blue-500 hover:text-blue-700 text-sm"
            >
              See All {name}
            </Link>
          )}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboardCard;
