import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"
import Cookies from "js-cookie";

const Dashboard = () => {

  const navigate = useNavigate();

  useEffect(() => {
    const roleFromCookies = Cookies.get("role")
    
    if (roleFromCookies === 'ROLE_USER') {
      navigate('/user-dashboard');
    } else if (roleFromCookies === 'ROLE_ADMIN') {
      navigate('/admin-dashboard');
    } else {
      navigate('/login'); // Redirect to login if no valid role is found
    }
    })

  return null
}

export default Dashboard