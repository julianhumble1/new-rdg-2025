import { Bars4Icon } from "@heroicons/react/16/solid";
import NavLink from "./NavLink.jsx";
import { useState } from "react";
import MobileLink from "./MobileLink.jsx";
import { navLinks } from "./directory.js";
import "./styles.css";
import MobileSubNavMenu from "./MobileSubNavMenu.jsx";

const AltHeader = () => {
  const [navbarOpen, setNavBarOpen] = useState(false);

  return (
    <div className="flex flex-col">
      <div className="flex w-full justify-between max-w-[1440px] mx-auto">
        <a href="/home">
          <img
            src="/images/new_logo_transparent.png"
            className="sm:h-24 sm:m-3 h-12 m-3"
            alt="RDG Logo"
          />
        </a>
        {/* Desktop navigation */}
        <div className=" md:w-1/2 justify-between sm:pr-5 hidden sm:flex">
          {navLinks.map((link, index) => (
            <NavLink
              title={link.title}
              link={link.link}
              key={index}
              sublinks={link.sublinks}
            />
          ))}
        </div>
        {/* Mobile navigation */}
        <button
          onClick={() => setNavBarOpen((prev) => !prev)}
          className="sm:hidden"
        >
          <Bars4Icon className="w-8 m-2 text-rdg-blue" />
        </button>
      </div>
      <div
        className={`mobile-menu sm:hidden ${navbarOpen ? "open" : "closed"}`}
      >
        <div className="mobile-links flex flex-col pb-2">
          {navLinks.map((link, index) =>
            link.sublinks ? (
              <MobileSubNavMenu
                key={index}
                title={link.title}
                link={link.link}
                sublinks={link.sublinks}
              />
            ) : (
              <MobileLink title={link.title} link={link.link} key={index} />
            ),
          )}
        </div>
      </div>
    </div>
  );
};

export default AltHeader;
