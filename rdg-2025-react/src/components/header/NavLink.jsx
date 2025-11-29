import { ChevronDownIcon } from "@heroicons/react/16/solid";
import { useLocation } from "react-router-dom";

const NavLink = ({ title, link, sublinks }) => {
  const location = useLocation();
  const pathname = location.pathname || "/";

  const isActive =
    pathname === link ||
    pathname.startsWith(link + "/") ||
    (link === "/" && pathname === "/");

  return (
    <a className="hover:cursor-pointer flex flex-col group w-full" href={link}>
      <div
        className={`h-2 bg-rdg-blue ${isActive ? "opacity-100" : "opacity-0 "} group-hover:opacity-50`}
      />
      <div className="h-full flex flex-col text-sm justify-center flex-1 md:px-1 px-2">
        <div className="relative">
          <div className="flex justify-center">
            {title}
            {sublinks && (
              <div className="h-5">
                <ChevronDownIcon className="text-black h-full opacity-75 group-hover:rotate-180 transition" />
              </div>
            )}
          </div>
          {sublinks && (
            <div className="absolute min-w-48 bg-gray-100 rounded-xl shadow-md p-2 opacity-0 group-hover:opacity-100 flex flex-col gap-1">
              {sublinks.map((sublink, index) => (
                <a
                  key={index}
                  href={`${sublink.link}`}
                  className="hover:underline"
                >
                  {sublink.title}
                </a>
              ))}
            </div>
          )}
        </div>
      </div>
    </a>
  );
};

export default NavLink;
