import { HomeIcon, UserCircleIcon } from "@heroicons/react/16/solid";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";

const ArchiveHeader = ({ loggedIn, setLoggedIn }) => {
  const navigate = useNavigate();

  const handleLogout = () => {
    setLoggedIn(false);
    Cookies.remove("role");
    Cookies.remove("token");
    navigate("/home");
  };

  return (
    <div className="flex justify-end text-sm text-rdg-blue mt-3 mb-1 mr-3">
      {!loggedIn ? (
        <button
          className="flex gap-1 hover:underline"
          onClick={() => navigate("/archive/login")}
        >
          <UserCircleIcon className="w-4" />
          Admin Login
        </button>
      ) : (
        <div className="flex gap-2">
          <button
            className="hover:underline flex gap-1"
            onClick={() => navigate("/archive/dashboard")}
          >
            <HomeIcon className="w-4" />
            <div>Dashboard</div>
          </button>
          <button className="flex gap-1 hover:underline" onClick={handleLogout}>
            <UserCircleIcon className="w-4" />

            <div>Logout</div>
          </button>
        </div>
      )}
    </div>
  );
};

export default ArchiveHeader;
