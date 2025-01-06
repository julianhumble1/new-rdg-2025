import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"
import Cookies from "js-cookie";

const Dashboard = () => {

  const navigate = useNavigate();

  useEffect(() => {
    console.log("redirecting dashboard")
    const roleFromCookies = Cookies.get("role")
    console.log(`Role: ${roleFromCookies}`)
    
    if (roleFromCookies === 'ROLE_USER') {
      navigate('/user-dashboard');
    } else if (roleFromCookies === 'ROLE_ADMIN') {
      navigate('/admin-dashboard');
    } else if (roleFromCookies === 'ROLE_MODERATOR') {
      navigate('/mod-dashboard');
    } else {
      navigate('/login'); // Redirect to login if no valid role is found
    }
    })

  return null
}

export default Dashboard