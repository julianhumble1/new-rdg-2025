import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";
import HomeProductionSpotLight from "./HomeProductionSpotLight.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";

const getEarliestPerformanceTime = (performances) => {
  if (!performances || performances.length === 0) return Infinity;
  return Math.min(...performances.map((p) => new Date(p.time).getTime()));
};

const UpcomingProductions = () => {
  const [productions, setProductions] = useState([]);

  useEffect(() => {
    const getProductions = async () => {
      const response = await ProductionService.getFutureProductions();
      if (response.status === 200) {
        const withPerformances = response.data.productions.map((p) => ({
          ...p.production,
          performances: p.performances ?? [],
        }));
        withPerformances.sort(
          (a, b) => getEarliestPerformanceTime(a.performances) - getEarliestPerformanceTime(b.performances),
        );
        setProductions(withPerformances);
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
              performances={production.performances}
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