import MonthDateUtils from "../../utils/MonthDateUtils.js";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import HighlightListItem from "../common/HighlightListItem.jsx";

const FestivalHighlight = ({ festivalData, setEditMode, handleDelete }) => {
  if (!festivalData) return null;

  return (
    <HighlightTemplate
      title={festivalData.name}
      type="Festivals"
      handleEdit={() => setEditMode(false)}
      handleDelete={handleDelete}
      createdAt={festivalData.createdAt}
      updatedAt={festivalData.updatedAt}
    >
      <HighlightListItem
        label="Venue"
        value={festivalData?.venue?.name}
        link={`/venues/${festivalData?.venue?.id}`}
      />

      <HighlightListItem
        label="Date"
        value={`${
          festivalData.month
            ? MonthDateUtils.monthMapping[festivalData.month]
            : ""
        } ${festivalData.year}`}
      />

      <HighlightListItem label="Description" value={festivalData.description} />
    </HighlightTemplate>
  );
};

export default FestivalHighlight;
