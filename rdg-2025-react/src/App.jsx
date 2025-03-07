import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"
import { useEffect, useState } from "react"
import Cookies from "js-cookie"
import Login from "./components/Login.jsx"
import Home from "./components/Home.jsx"
import Header from "./components/header/Header.jsx"
import Dashboard from "./components/dashboards/Dashboard.jsx"
import UserDashboard from "./components/dashboards/UserDashboard.jsx"
import AdminDashboard from "./components/dashboards/AdminDashboard.jsx"
import NewVenueForm from "./components/venues/NewVenueForm.jsx"
import AllVenues from "./components/venues/AllVenues.jsx"
import NewProductionForm from "./components/productions/NewProductionForm.jsx"
import NewFestivalForm from "./components/festivals/NewFestivalForm.jsx"
import VenuePage from "./components/venues/VenuePage.jsx"
import ProductionPage from "./components/productions/ProductionPage.jsx"
import NewPerformanceForm from "./components/performances/NewPerformanceForm.jsx"
import FestivalPage from "./components/festivals/FestivalPage.jsx"
import AllProductions from "./components/productions/AllProductions.jsx"
import AllFestivals from "./components/festivals/AllFestivals.jsx"

import NewPersonForm from "./components/people/NewPersonForm.jsx"
import AllPeople from "./components/people/AllPeople.jsx"

import FacebookFeed from "./components/FacebookFeed.jsx"



function App() {

  const [loggedIn, setLoggedIn] = useState(false)

  useEffect(() => {
    const token = Cookies.get("token")
    if (token) setLoggedIn(true)
  }, [])

  return (
    <Router >
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

        <Route path="/venues/new" element={<NewVenueForm />} />
        <Route path="/venues" element={<AllVenues />} />
        <Route path="/venues/:id" element={<VenuePage />} />

        <Route path="/productions/new" element={<NewProductionForm />} />
        <Route path="/productions" element={<AllProductions />} />
        <Route path="/productions/:id" element={<ProductionPage />} />

        <Route path="/festivals/new" element={<NewFestivalForm />} />
        <Route path="/festivals" element={<AllFestivals />} />
        <Route path="/festivals/:id" element={<FestivalPage />} />

        <Route path="/performances/new" element={<NewPerformanceForm />} />


        <Route path="/people/new" element={<NewPersonForm />} />
        <Route path="/people" element={<AllPeople />} />

        <Route path="/facebook" element={<FacebookFeed />} />

      </Routes>
    </Router>
  )
}

export default App
