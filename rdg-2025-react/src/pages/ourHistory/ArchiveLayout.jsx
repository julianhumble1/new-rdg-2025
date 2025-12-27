import { Outlet } from "react-router-dom";

const ArchiveLayout = () => {
  return (
    <div>
          <div className="flex justify-end">
              <button className="text-sm font-blue hover-underline">Admin Login</button>
      </div>
        <Outlet />
    </div>
  );
};

export default ArchiveLayout;
