import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";

const EventHighlight = ({ eventData }) => {
  if (eventData) {
    return (
      <HighlightTemplate title={eventData.name} type="Events">
        <HighlightListItem label="Description" value={eventData.description} />
      </HighlightTemplate>
    );
  }

  return <div>EventHighlight</div>;
};

export default EventHighlight;
