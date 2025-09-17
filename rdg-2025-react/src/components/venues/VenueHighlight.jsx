import AddressHelper from "../../utils/AddressHelper.js";
import HighlightTemplate from "../productions/HighlightTemplate.jsx";
import HighlightListItem from "../common/HighlightListItem.jsx";

const VenueHighlight = ({ venueData, setEditMode, handleDelete }) => {
  const fullAddress = AddressHelper.getFullAddress(
    venueData.address,
    venueData.town,
    venueData.postcode,
  );

  if (venueData)
    return (
      <HighlightTemplate
        title={venueData.name}
        type="Venues"
        handleDelete={() => handleDelete(venueData)}
        handleEdit={() => setEditMode(true)}
        createdAt={venueData.createdAt}
        updatedAt={venueData.updatedAt}
      >
        <HighlightListItem label="Address" value={fullAddress} />

        <HighlightListItem label="Notes" value={venueData.notes} />

        <HighlightListItem
          label="Website"
          value={venueData.url}
          link={venueData.url}
          linkType="external"
        />
      </HighlightTemplate>
    );
};

export default VenueHighlight;
