import { format } from "date-fns";
import { Link } from "react-router-dom";

const HighlightFooter = ({ name, createdAt, updatedAt }) => {
  return (
    <div>
      <div className="flex justify-between mt-2">
        <div>
          {createdAt && (
            <div className="flex text-sm gap-1">
              <div className="font-bold italic">Created:</div>
              <div>{format(new Date(createdAt), "dd-MM-yyyy")}</div>
            </div>
          )}
          {updatedAt && (
            <div className="flex text-sm gap-1">
              <div className="font-bold italic">Updated:</div>
              <div>{format(new Date(updatedAt), "dd-MM-yyyy")}</div>
            </div>
          )}
        </div>
        <Link
          to={`/archive/${name.toLowerCase()}`}
          className="text-sm hover:underline font-bold text-end my-auto"
        >
          See All {name}
        </Link>
      </div>
    </div>
  );
};

export default HighlightFooter;
