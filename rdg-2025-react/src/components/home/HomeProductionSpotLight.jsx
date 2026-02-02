import { useEffect, useState } from "react";
import ProductionService from "../../services/ProductionService.js";
import DateHelper from "../../utils/DateHelper.js";
import { Link } from "react-router-dom";

const HomeProductionSpotLight = ({ production, image }) => {
  const [performanceStatement, setPerformanceStatement] = useState("");

  const [performances, setPerformances] = useState([]);

  useEffect(() => {
    const getPerformanceStatement = async () => {
      const response = await ProductionService.getProductionById(production.id);
      setPerformances(response.data.performances);
      setPerformanceStatement(
        DateHelper.createPerformanceStatement(response.data.performances),
      );
    };

    getPerformanceStatement();
  }, [production.id]);

  if (performances.length === 0) return null;

  return (
    <div className="flex w-full bg-gray-100  rounded-xl shadow-md hover:shadow-xl transition h-44 max-h-44">
      <img className="w-1/2 md:w-1/3 rounded-l-xl " src={image} alt="image 1" />
      <div className="flex flex-col gap-1 justify-center p-2 ">
        <div className="flex">
          <Link
            to={`/archive/productions/${production.id}`}
            className={`lg:text-2xl text-md hover:underline font-bold tracking-tight text-gray-900 dark:text-white line-clamp-1 ${performances[0].boxOffice ? "w-4/5" : ""}`}
          >
            {production.name}
          </Link>
          {performances[0].boxOffice && (
            <button
              className="text-xs bg-rdg-red rounded-full px-2 font-bold text-white hover:scale-105"
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
            </button>
          )}
        </div>
        <div className="text-xs italic">{performanceStatement}</div>
        <div className=" text-gray-700 text-xs line-clamp-5">
          {production.description}
        </div>
      </div>
    </div>
  );
};

export default HomeProductionSpotLight;
