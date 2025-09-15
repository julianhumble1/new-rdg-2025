import { Link } from "react-router-dom";

const AdminDashboardCard = ({ name, basePath, showSeeAll = true }) => {
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
