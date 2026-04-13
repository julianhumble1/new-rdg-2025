import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";
import DateHelper from "../../utils/DateHelper.js";
import { Link } from "react-router-dom";
import CloudinaryImage from "../common/CloudinaryImage.jsx";

const HomeProductionSpotLight = ({ production, performances: performancesProp }) => {
  const [performanceStatement, setPerformanceStatement] = useState("");
  const [performances, setPerformances] = useState(performancesProp ?? []);

  useEffect(() => {
    if (performancesProp) {
      setPerformances(performancesProp);
      if (performancesProp.length > 0) {
        setPerformanceStatement(DateHelper.createPerformanceStatement(performancesProp));
      }
      return;
    }
    const getPerformanceStatement = async () => {
      const response = await ProductionService.getProductionById(production.id);
      setPerformances(response.data.performances);
      setPerformanceStatement(
        DateHelper.createPerformanceStatement(response.data.performances),
      );
    };

    getPerformanceStatement();
  }, [production.id, performancesProp]);

  // if (performances.length === 0) return null;

  return (
    <div className="flex w-full bg-gray-100  rounded-xl shadow-md hover:shadow-xl transition sm:h-56 max-h-56 relative flex-1">
      <div className="h-40 w-60 shrink-0 rounded-l-xl overflow-hidden">
        <CloudinaryImage idNumber={production.id} folder="flyers" fallbackSrc="/images/new_logo_transparent.png" />
      </div>
      <div className="flex flex-col gap-1 p-3 w-1/2 md:w-auto ">
        <div className="flex">
          <Link
            to={`/archive/productions/${production.id}`}
            className="lg:text-2xl sm:text-lg text-md hover:underline font-bold tracking-tight text-gray-900 dark:text-white sm:line-clamp-2"
          >
            {production.name}
          </Link>
        </div>
        {performances.length > 0 && performances[0].boxOffice && (
          <div
            className="sm:text-sm text-xs bg-rdg-red rounded-full py-1 px-2 sm:py-2 sm:px-4 font-bold text-white hover:scale-105 right-2 bottom-2 absolute hover:cursor-pointer"
            onClick={() => {
              let url = performances[0].boxOffice || "";
              // ensure we open an absolute URL (prepend https:// when protocol missing)
              if (!/^https?:\/\//i.test(url)) {
                url = `https://${url.replace(/^\/+/, "")}`;
              }
              window.open(url, "_blank", "noopener,noreferrer");
            }}
          >
            Book Now
          </div>
        )}
        {performanceStatement && (
          <div className="text-xs xl:text-sm italic hidden md:inline">
            {performanceStatement}
          </div>
        )}
      </div>
    </div>
  );
};

export default HomeProductionSpotLight;