import { format } from "date-fns";
import { Badge } from "flowbite-react";
import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import ProductionImageWithUploadBox from "../photo_components/ProductionImageWithUploadBox.jsx";

const ProductionHighlight = ({ productionData, setEditMode, handleDelete }) => {
  if (!productionData) return null;

  return (
    <div className="flex flex-col md:flex-row gap-4">
      {/* Production Image */}
      <div className="w-full md:w-1/3">
        <ProductionImageWithUploadBox productionId={productionData.id} />
      </div>
      
      {/* Production Details */}
      <div className="w-full md:w-2/3">
        <HighlightTemplate
          title={productionData.name}
          type="Productions"
          handleEdit={() => setEditMode(true)}
          handleDelete={() => handleDelete(productionData)}
          createdAt={productionData.createdAt}
          updatedAt={productionData.updatedAt}
        >
          <HighlightListItem
            label="Venue"
            value={productionData?.venue?.name}
            link={`/venues/${productionData?.venue?.id}`}
          />

          <HighlightListItem label="Author" value={productionData.author} />

          <HighlightListItem
            label="Sundowners"
            value={productionData.sundowners ? "Yes" : "No"}
          />

          <HighlightListItem
            label="Description"
            value={productionData.description}
          />

          <HighlightListItem
            label="Audition Date"
            value={format(
              new Date(productionData.auditionDate),
              "MMMM d, yyyy, h:mm a",
            )}
          />
          {productionData.notConfirmed && (
            <Badge color="warning" className="w-fit">
              Not Yet Confirmed
            </Badge>
          )}
        </HighlightTemplate>
      </div>
    </div>
  );
};

export default ProductionHighlight;
