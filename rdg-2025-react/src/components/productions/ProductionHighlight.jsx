import { format, isValid } from "date-fns";
import { useEffect, useState } from "react";
import { Badge } from "flowbite-react";
import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import CloudinaryService from "../../services/CloudinaryService.js";

const ProductionHighlight = ({ productionData, setEditMode, handleDelete }) => {
  const [flyerUrl, setFlyerUrl] = useState(null);

  useEffect(() => {
    if (!productionData?.id) return;
    CloudinaryService.getUrl(productionData.id, "flyers")
      .then((response) => setFlyerUrl(response.data.url))
      .catch(() => {});
  }, [productionData?.id]);

  if (!productionData) return null;

  return (
    <HighlightTemplate
      title={productionData.name}
      type="Productions"
      handleEdit={() => setEditMode(true)}
      handleDelete={() => handleDelete(productionData)}
      createdAt={productionData.createdAt}
      updatedAt={productionData.updatedAt}
      sundowners={productionData.sundowners}
    >
      <div>
        {flyerUrl && (
          <img
            src={flyerUrl}
            alt="Production flyer"
            className="float-right ml-4 mb-2 h-40 w-60 object-cover rounded"
          />
        )}
        <HighlightListItem
          label="Venue"
          value={productionData?.venue?.name}
          link={`/archive/venues/${productionData?.venue?.id}`}
        />

        <HighlightListItem label="Author" value={productionData.author} />

        <HighlightListItem
          label="Description"
          value={productionData.description}
        />

        {productionData?.auditionDate && (
          <HighlightListItem
            label="Audition Date"
            value={format(
              new Date(productionData.auditionDate),
              "MMMM d, yyyy, h:mm a",
            )}
          />
        )}
        {productionData.notConfirmed && (
          <Badge color="warning" className="w-fit">
            Not Yet Confirmed
          </Badge>
        )}
        {productionData?.sundowners && (
          <div className="px-2 rounded-full italic md:hidden bg-gradient-to-r from-rdg-red to-black font-bold text-white text-center w-fit">
            SUNDOWNERS
          </div>
        )}
      </div>
    </HighlightTemplate>
  );
};

export default ProductionHighlight;
