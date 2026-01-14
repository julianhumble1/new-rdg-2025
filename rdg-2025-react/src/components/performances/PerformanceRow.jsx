import { format } from "date-fns";
import { Link } from "react-router-dom";

const PerformanceRow = ({ performanceData, handleDelete }) => {
  const formattedDate = format(
    new Date(performanceData.time),
    "MMMM d, yyyy, h:mm a",
  );

  return (
    <div className="flex flex-col text-sm p-2 hover:bg-gray-200 bg-gray-100 border gap-1 md:gap-0">
      <div className="font-bold">{formattedDate}</div>
      <div>
        <Link
          to={`/archive/productions/${performanceData.production.id}`}
          className="hover:underline"
        >
          {performanceData.production.name}
        </Link>
      </div>
      <div>
        <Link
          to={`/archive/venues/${performanceData.venue.id}`}
          className="hover:underline"
        >
          {performanceData.venue.name}
        </Link>
      </div>
      <div>
        {performanceData.festival ? (
          <Link
            to={`/archive/festivals/${performanceData.festival.id}`}
            className="hover:underline"
          >
            {performanceData.festival.name} ({performanceData.festival.year})
          </Link>
        ) : (
          ""
        )}
      </div>
      <div>
        {performanceData.boxOffice && (
          <a
            className="text-blue-500 hover:text-blue-700 hover:underline"
            target="_blank"
            href={`${performanceData.boxOffice}`}
          >
            {performanceData.boxOffice}
          </a>
        )}
      </div>
      <div className="italic">{performanceData.description}</div>
      <div className="flex justify-end">
        <div className="flex flex-row gap-1">
          <Link
            className="font-bold hover:underline"
            to={`/archive/performances/edit/${performanceData.id}`}
          >
            Edit
          </Link>
          <button
            className="font-bold hover:underline"
            onClick={() => handleDelete(performanceData)}
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

export default PerformanceRow;
