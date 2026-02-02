import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";
import HomeProductionSpotLight from "./HomeProductionSpotLight.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";

const UpcomingProductions = () => {
  const [productions, setProductions] = useState([]);

  useEffect(() => {
    const getProductions = async () => {
      const response = await ProductionService.getFutureProductions();
      if (response.status === 200) {
        setProductions(response.data.productions);
      }
    };
    getProductions();
  }, []);

  const pictureLinks = [
    "/images/productions/batb.jpeg",
    "/images/productions/rock-paper-scissors.jpeg",
    "/images/productions/pinter.jpeg",
  ];

  return (
    <>
      <div className="text-rdg-red font-bold text-lg my-auto">
        Upcoming Productions:
      </div>
      {productions.length > 0 ? (
        productions
          .slice(0, 3)
          .map((production, idx) => (
            <HomeProductionSpotLight
              production={production}
              key={production.id}
              image={pictureLinks[idx]}
            />
          ))
      ) : (
        <CustomSpinner />
      )}
    </>
  );
};

export default UpcomingProductions;
