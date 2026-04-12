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
      <div className="flex gap-4">
        <div className="flex flex-col gap-2 flex-1">
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
        {flyerUrl && (
          <div className="w-28 h-[158px] shrink-0">
            <img
              src={flyerUrl}
              alt="Production flyer"
              className="w-full h-full object-cover rounded"
            />
          </div>
        )}
      </div>
    </HighlightTemplate>
  );
};

export default ProductionHighlight;
