import {
  Navbar,
  NavbarLink,
  NavbarBrand,
  NavbarToggle,
  NavbarCollapse,
} from "flowbite-react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";

const Header = ({ loggedIn, setLoggedIn }) => {
  const url = window.location.pathname;

  const navigate = useNavigate();

  const [role, setRole] = useState("");

  useEffect(() => {
    const roleFromCookies = Cookies.get("role");
    if (roleFromCookies)
      setRole(
        roleFromCookies.charAt(5).toUpperCase() +
          roleFromCookies.substring(6).toLowerCase(),
      );
  }, [loggedIn]);

  const handleLogout = () => {
    Cookies.remove("token");
    Cookies.remove("role");
    setLoggedIn(false);
    navigate("/login");
  };

  return (
    <Navbar fluid className="bg-sky-900">
      <NavbarBrand href="/home">
        <img
          src="/src/assets/new_logo_transparent.png"
          className="mr-3 h-9 sm:h-9"
          alt="Flowbite React Logo"
        />
        <span className="self-center whitespace-nowrap text-xl font-semibold text-white">
          Runnymede Drama Group
        </span>
      </NavbarBrand>
      <NavbarToggle className="text-white" />
      <NavbarCollapse>
        <NavbarLink
          href="/home"
          className="text-white"
          active={url.includes("home")}
        >
          {" "}
          Home{" "}
        </NavbarLink>
        {loggedIn && (
          <NavbarLink
            href="/dashboard"
            className="text-white active:text-red-500"
            active={url.includes("dashboard")}
          >
            {role} Dashboard
          </NavbarLink>
        )}
        {!loggedIn ? (
          <NavbarLink
            href="/login"
            className="text-white"
            active={url.includes("login")}
          >
            Login
          </NavbarLink>
        ) : (
          <NavbarLink
            className="text-white hover:cursor-pointer"
            onClick={handleLogout}
          >
            Logout
          </NavbarLink>
        )}
      </NavbarCollapse>
    </Navbar>
  );
};

export default Header;
