import { Outlet } from "react-router-dom";
import ArchiveHeader from "./ArchiveHeader.jsx";

const ArchiveLayout = ({ loggedIn, setLoggedIn }) => {
  return (
    <>
      <div className="flex-1">
        <Outlet />
      </div>
      <ArchiveHeader loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
    </>
  );
};

export default ArchiveLayout;
