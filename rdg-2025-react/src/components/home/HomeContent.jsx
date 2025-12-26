import { useNavigate } from "react-router-dom";
import RedButton from "../common/RedButton.jsx";
import Socials from "../modals/Socials.jsx";

const HomeContent = () => {
  const navigate = useNavigate();

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
      <div className="flex justify-between">
        <div className="text-rdg-red font-bold text-lg my-auto">
          Our Current Production
        </div>
        <RedButton
          onClick={() =>
            window.open(
              "https://www.riverhousebarn.co.uk/events/133315?showDates=1&showItems=1",
              "_blank",
              "noopener",
            )
          }
        >
          Book Now
        </RedButton>
      </div>
      <img
        src="/images/rdg_xmas25_lndscape._1882692229.jpg"
        alt="xmas at barn"
        className="mx-8 hover:cursor-pointer"
        onClick={() => navigate("/upcoming")}
      />
      <div className="hidden md:inline">
        <div className="text-center">Find us on socials:</div>
        <Socials />
      </div>
    </>
  );
};

export default HomeContent;
