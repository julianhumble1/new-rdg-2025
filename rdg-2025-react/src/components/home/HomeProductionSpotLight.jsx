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

  // if (performances.length === 0) return null;

  return (
    <div className="flex w-full bg-gray-100  rounded-xl shadow-md hover:shadow-xl transition h-44 max-h-44 relative">
      {performances.length > 0 && performances[0].boxOffice && (
        <div
          className="text-sm bg-rdg-red rounded-full p-2 px-4 font-bold text-white hover:scale-105 right-2 bottom-2 absolute hover:cursor-pointer"
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
      <img
        className=" aspect-auto rounded-l-xl h-full"
        src={image}
        alt="image 1"
      />
      <div className="flex flex-col gap-1 p-3 ">
        <div className="flex">
          <Link
            to={`/archive/productions/${production.id}`}
            className="lg:text-2xl text-md hover:underline font-bold tracking-tight text-gray-900 dark:text-white line-clamp-2"
          >
            {production.name}
          </Link>
        </div>
        {performanceStatement && (
          <div className="text-sm italic">{performanceStatement}</div>
        )}
      </div>
    </div>
  );
};

export default HomeProductionSpotLight;
