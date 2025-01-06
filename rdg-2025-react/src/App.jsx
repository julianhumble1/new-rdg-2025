import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"
import { useEffect, useState } from "react"
import Cookies from "js-cookie"
import Login from "./components/Login.jsx"
import Home from "./components/Home.jsx"
import './App.css'
import Header from "./components/Header.jsx"
import Dashboard from "./components/dashboards/Dashboard.jsx"
import UserDashboard from "./components/dashboards/UserDashboard.jsx"
import AdminDashboard from "./components/dashboards/AdminDashboard.jsx"


function App() {

  const [loggedIn, setLoggedIn] = useState(false)

  

  useEffect(() => {
    const token = Cookies.get("token")
    if (token) setLoggedIn(true)
  }, [])

  return (
    <Router>
      <Header
        loggedIn={loggedIn}
        setLoggedIn={setLoggedIn}
      />
      <Routes>
        <Route path="/" element={<Navigate to = "/home" replace/>}>
        </Route>
        <Route path="/home" element={<Home />} >
        </Route>
        <Route path="/login"
          element={
            <Login
              loggedIn={loggedIn}
              setLoggedIn={setLoggedIn}
            />
          }>
        </Route>
        <Route path="/dashboard" element={<Dashboard  />} />
        <Route path="/user-dashboard" element={<UserDashboard  />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
      </Routes>
    </Router>
  )
}

export default App
