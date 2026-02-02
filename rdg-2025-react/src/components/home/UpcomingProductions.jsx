import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";
import HomeProductionSpotLight from "./HomeProductionSpotLight.jsx";

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
    "/images/productions/rock-paper-scissors.jpg",
    "/images/productions/beauty social adv2_.jpg",
    "/images/productions/pinter.jpg",
  ];

  return productions.map((production, idx) => (
    <HomeProductionSpotLight
      production={production}
      key={production.id}
      image={pictureLinks[idx]}
    />
  ));
};

export default UpcomingProductions;
