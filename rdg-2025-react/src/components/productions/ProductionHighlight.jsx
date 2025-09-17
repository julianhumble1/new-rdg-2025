import { format } from "date-fns";
import { Badge } from "flowbite-react";
import { Link } from "react-router-dom";
import Card from "../common/Card.jsx";

const ProductionHighlight = ({ productionData, setEditMode, handleDelete }) => {
  if (productionData)
    return (
      <div>
        <div className="text-black text-xl font-bold flex justify-between">
          <div>{productionData.name}</div>
          <div className="flex gap-2">
            <button
              className="text-sm hover:underline"
              onClick={() => setEditMode(true)}
            >
              Edit
            </button>
            <button
              className="text-sm hover:underline"
              onClick={() => handleDelete(productionData)}
            >
              Delete
            </button>
          </div>
        </div>
        <div className="flex flex-col justify-between h-full">
          <div className="flex flex-col gap-2">
            {productionData.venue && (
              <div className="flex flex-col">
                <div className="font-bold italic">Venue</div>
                <Link
                  className=" hover:underline font-bold"
                  to={`/venues/${productionData.venue.id}`}
                >
                  {productionData.venue.name}
                </Link>
              </div>
            )}
            {productionData.author && (
              <div className="flex flex-col">
                <div className="font-bold italic">Author</div>
                <div>{productionData.author}</div>
              </div>
            )}
            <div className="flex flex-col">
              <div className="font-bold italic">Sundowners</div>
              <div>{productionData.sundowners ? "Yes" : "No"}</div>
            </div>

            {productionData.description && (
              <div className="flex flex-col h-40 overflow-auto">
                <div className="font-bold italic">Description</div>
                <div>{productionData.description}</div>
              </div>
            )}

            {productionData.auditionDate && (
              <div className="flex flex-col">
                <div className="font-bold italic">Audition Date</div>
                <div>
                  {format(
                    new Date(productionData.auditionDate),
                    "MMMM d, yyyy, h:mm a",
                  )}
                </div>
              </div>
            )}
            {productionData.notConfirmed && (
              <Badge color="warning" className="w-fit">
                Not Yet Confirmed
              </Badge>
            )}
          </div>
          <div className="flex justify-between mt-2">
            <div>
              <div className="flex text-sm gap-1">
                <div className="font-bold italic">Created:</div>
                <div>
                  {format(new Date(productionData.createdAt), "dd-MM-yyyy")}
                </div>
              </div>
              <div className="flex text-sm gap-1">
                <div className="font-bold italic">Last Updated:</div>
                <div>
                  {format(new Date(productionData.updatedAt), "dd-MM-yyyy")}
                </div>
              </div>
            </div>
            <Link
              to="/productions"
              className="text-sm hover:underline font-bold text-end my-auto"
            >
              See All Productions
            </Link>
          </div>
        </div>
      </div>
    );
};

export default ProductionHighlight;
