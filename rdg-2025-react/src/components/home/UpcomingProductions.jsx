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

  return (
    <>
      <div className="text-rdg-red font-bold text-lg ">
        Upcoming Productions:
      </div>
      {productions.length > 0 ? (
        productions
          .slice(0, 3)
          .map((production) => (
            <HomeProductionSpotLight
              production={production}
              key={production.id}
            />
          ))
      ) : (
        <CustomSpinner />
      )}
    </>
  );
};

export default UpcomingProductions;
