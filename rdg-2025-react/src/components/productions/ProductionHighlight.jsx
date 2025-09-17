import { format } from "date-fns";
import { Badge } from "flowbite-react";
import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "./HighlightTemplate.jsx";

const ProductionHighlight = ({ productionData, setEditMode, handleDelete }) => {
  if (!productionData) return null;

  return (
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
  );
};

export default ProductionHighlight;
