import { Outlet } from "react-router-dom";
import ArchiveHeader from "./ArchiveHeader.jsx";

const ArchiveLayout = ({ loggedIn, setLoggedIn }) => {
  return (
    <div className="bg-slate-100 h-full flex-1">
      <ArchiveHeader loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
      <Outlet />
    </div>
  );
};

export default ArchiveLayout;
