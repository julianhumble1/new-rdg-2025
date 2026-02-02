import { useNavigate } from "react-router-dom";
import RedButton from "../common/RedButton.jsx";
import Socials from "../modals/Socials.jsx";
import UpcomingProduction from "./UpcomingProduction.jsx";
import UpcomingProductions from "./UpcomingProductions.jsx";

const HomeContent = () => {
  const navigate = useNavigate();

  const openTicketLink = () => {
    window.open(
      "https://www.ticketsource.co.uk/the-sundowners",
      "_blank",
      "noopener",
    );
  };

  return (
    <>
      <div>
        Runnymede Drama Group is one of the most lively and successful community
        theatre groups in the area.
      </div>
      <div>
        We exist to entertain local audiences with high-quality productions that
        spark imagination and bring people together. We aim to provide an open,
        supportive space for people of all ages to build confidence and develop
        new skills.
      </div>
      <div>
        Rehearsing at our dedicated studio in Chertsey in Surrey, we stage at
        least five productions each year at venues including The Riverhouse Barn
        and Cecil Hepworth Playhouse in Walton, and the Rhoda McGaw Theatre in
        Woking.
      </div>
      {/* <UpcomingProduction /> */}
      <UpcomingProductions />
      <div className="hidden md:inline">
        <div className="text-center">Find us on socials:</div>
        <Socials />
      </div>
    </>
  );
};

export default HomeContent;
