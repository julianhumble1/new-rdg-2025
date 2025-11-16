import { useLocation } from "react-router-dom";

const MobileLink = ({ title, link }) => {
  const location = useLocation();
  const pathname = location.pathname || "/";

  const isActive =
    pathname === link ||
    pathname.startsWith(link + "/") ||
    (link === "/" && pathname === "/");

  return (
    <a className="w-full group flex h-8 gap-3 sm:hidden " href={link}>
      <div
        className={`h-full w-2 bg-rdg-blue ${isActive ? "opacity-100" : "opacity-0"}`}
      />
      <div className="flex flex-col justify-center ">{title}</div>
    </a>
  );
};

export default MobileLink;
