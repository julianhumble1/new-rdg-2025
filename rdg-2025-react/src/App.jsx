import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { useEffect, useState } from "react";
import Cookies from "js-cookie";
import Login from "./components/Login.jsx";
import Dashboard from "./components/dashboards/Dashboard.jsx";
import UserDashboard from "./components/dashboards/UserDashboard.jsx";
import AdminDashboard from "./components/dashboards/AdminDashboard.jsx";
import NewVenueForm from "./components/venues/NewVenueForm.jsx";
import AllVenues from "./components/venues/AllVenues.jsx";
import NewProductionForm from "./components/productions/NewProductionForm.jsx";
import NewFestivalForm from "./components/festivals/NewFestivalForm.jsx";
import VenuePage from "./components/venues/VenuePage.jsx";
import ProductionPage from "./components/productions/ProductionPage.jsx";
import NewPerformanceForm from "./components/performances/NewPerformanceForm.jsx";
import FestivalPage from "./components/festivals/FestivalPage.jsx";
import AllProductions from "./components/productions/AllProductions.jsx";
import AllFestivals from "./components/festivals/AllFestivals.jsx";
import NewPersonForm from "./components/people/NewPersonForm.jsx";
import AllPeople from "./components/people/AllPeople.jsx";
import PersonPage from "./components/people/PersonPage.jsx";
import NewCreditForm from "./components/credits/NewCreditForm.jsx";
import EditCreditForm from "./components/credits/EditCreditForm.jsx";
import EditPerformanceForm from "./components/performances/EditPerformanceForm.jsx";
import NewAwardForm from "./components/awards/NewAwardForm.jsx";
import EditAwardForm from "./components/awards/EditAwardForm.jsx";
import HomeImageSelect from "./components/home/HomeImageSelect.jsx";
import { Bounce, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import NewEventForm from "./components/events/NewEventForm.jsx";
import EventPage from "./components/events/EventPage.jsx";
import AllEvents from "./components/events/AllEvents.jsx";
import AltHeader from "./components/header/AltHeader.jsx";
import AltHome from "./components/home/AltHome.jsx";
import About from "./pages/about/About.jsx";
import OurVenues from "./pages/about/OurVenues/OurVenues.jsx";
import Committee from "./pages/about/Committee/Committee.jsx";
import Legal from "./pages/about/Legal/Legal.jsx";
import Join from "./pages/join/Join.jsx";
import Archive from "./pages/ourHistory/Archive.jsx";
import ArchiveLayout from "./pages/ourHistory/ArchiveLayout.jsx";
import Upcoming from "./pages/upcoming/Upcoming.jsx";
import Contact from "./pages/contact/Contact.jsx";
import AllAwards from "./components/awards/AllAwards.jsx";

function App() {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    const token = Cookies.get("token");
    if (token) setLoggedIn(true);
  }, []);

  return (
    <>
      <Router>
        <div className="flex flex-col min-h-screen h-full tracking-wider max-w-[1440px] mx-auto">
          <AltHeader />
          <Routes>
            <Route path="/" element={<Navigate to="/home" replace />}></Route>
            <Route path="/home" element={<AltHome />} />

            <Route path="/home/images" element={<HomeImageSelect />} />

            <Route
              path="/archive"
              element={
                <ArchiveLayout loggedIn={loggedIn} setLoggedIn={setLoggedIn} />
              }
            >
              <Route index element={<Archive />} />

              <Route
                path="login"
                element={<Login setLoggedIn={setLoggedIn} />}
              />
              <Route path="dashboard" element={<Dashboard />} />
              <Route path="user-dashboard" element={<UserDashboard />} />
              <Route path="admin-dashboard" element={<AdminDashboard />} />

              <Route path="venues">
                <Route index element={<AllVenues />} />
                <Route path="new" element={<NewVenueForm />} />
                <Route path=":id" element={<VenuePage />} />
              </Route>

              <Route path="productions">
                <Route index element={<AllProductions />} />
                <Route path="new" element={<NewProductionForm />} />
                <Route path=":id" element={<ProductionPage />} />
              </Route>

              <Route path="festivals">
                <Route index element={<AllFestivals />} />
                <Route path="new" element={<NewFestivalForm />} />
                <Route path=":id" element={<FestivalPage />} />
              </Route>

              <Route path="performances">
                <Route path="new" element={<NewPerformanceForm />} />
                <Route path="edit/:id" element={<EditPerformanceForm />} />
              </Route>

              <Route path="people">
                <Route index element={<AllPeople />} />
                <Route path=":id" element={<PersonPage />} />
                <Route path="new" element={<NewPersonForm />} />
              </Route>

              <Route path="credits">
                <Route path="new" element={<NewCreditForm />} />
                <Route path="edit/:id" element={<EditCreditForm />} />
              </Route>

              <Route path="awards">
                <Route index element={<AllAwards />} />
                <Route path="new" element={<NewAwardForm />} />
                <Route path="edit/:id" element={<EditAwardForm />} />
              </Route>

              <Route path="events">
                <Route index element={<AllEvents />} />
                <Route path="new" element={<NewEventForm />} />
                <Route path=":id" element={<EventPage />} />
              </Route>
            </Route>

            <Route path="/about">
              <Route index element={<About />} />
              <Route path="ourvenues" element={<OurVenues />} />
              <Route path="committee" element={<Committee />} />
              <Route path="legal" element={<Legal />} />
            </Route>

            <Route path="/join" element={<Join />} />

            <Route path="/upcoming" element={<Upcoming />} />

            <Route path="/contact" element={<Contact />} />
          </Routes>
        </div>
      </Router>
      <ToastContainer
        position="bottom-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
        transition={Bounce}
        style={{ zIndex: 9999 }}
      />
    </>
  );
}

export default App;
