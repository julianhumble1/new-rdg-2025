import { useLocation } from "react-router-dom";

const NavLink = ({ title, link }) => {
  const location = useLocation();
  const pathname = location.pathname || "/";

  const isActive =
    pathname === link ||
    pathname.startsWith(link + "/") ||
    (link === "/" && pathname === "/");

  return (
    <a className="hover:cursor-pointer flex flex-col group" href={link}>
      <div
        className={`h-2 bg-rdg-blue ${isActive ? "opacity-100" : "opacity-0 "} group-hover:opacity-50`}
      />
      <div className="h-full flex flex-col justify-center flex-1 md:px-4 px-2">
        {title}
      </div>
    </a>
  );
};

export default NavLink;
