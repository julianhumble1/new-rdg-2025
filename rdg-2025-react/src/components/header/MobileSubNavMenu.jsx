import { ChevronDownIcon } from "@heroicons/react/16/solid";
import { useState } from "react";
import { useLocation } from "react-router-dom";
import MobileLink from "./MobileLink.jsx";

const MobileSubNavMenu = ({ link, title, sublinks }) => {
  const location = useLocation();
  const pathname = location.pathname || "/";

  const isActive = sublinks.some((sublink) => pathname === sublink.link);

  const [open, setOpen] = useState(isActive);

  return (
    <>
      <button
        className="h-[48px] pl-[12px] flex gap-3"
        onClick={() => setOpen((prev) => !prev)}
      >
        <div className={`h-full w-2 bg-rdg-blue opacity-0`} />
        <div className="flex flex-col justify-center">{title}</div>
        <div className="flex flex-col justify-center">
          <ChevronDownIcon
            className={`opacity-75 h-5  ${open ? "rotate-180" : ""} transition`}
          />
        </div>
      </button>
      {open && (
        <div className="flex flex-col pl-4">
          {sublinks.map((sublink, index) => (
            <MobileLink link={sublink.link} title={sublink.title} key={index} />
          ))}
        </div>
      )}
    </>
  );
};

export default MobileSubNavMenu;
