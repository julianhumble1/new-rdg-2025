import { format } from "date-fns";
import { Link, useNavigate } from "react-router-dom";
import EditDeleteButtons from "../common/EditDeleteButtons.jsx";

const PerformanceRow = ({ performanceData, handleDelete }) => {
  const navigate = useNavigate();

  const formattedDate = format(
    new Date(performanceData.time),
    "MMMM d, yyyy, h:mm a",
  );

  const handleEdit = () =>
    navigate(`/archive/performances/edit/${performanceData.id}`);

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
          <EditDeleteButtons
            handleEdit={handleEdit}
            handleDelete={() => handleDelete(performanceData)}
            itemToDelete={performanceData}
          />
        </div>
      </div>
    </div>
  );
};

export default PerformanceRow;
