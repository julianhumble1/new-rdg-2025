import PersonImageWithUploadBox from "../photo_components/PersonImageWithUploadBox.jsx";
import ContactDetailsBox from "./ContactDetailsBox.jsx";
import Card from "../common/Card.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import HighlightListItem from "../common/HighlightListItem.jsx";

const DetailedPersonHighlight = ({
  personData,
  setEditMode,
  handleDelete,
  image,
  fetchPersonData,
}) => {
  if (!personData) return null;

  const fullName = personData.firstName + " " + personData.lastName;

  return (
    <HighlightTemplate
      title={fullName}
      type="People"
      handleEdit={() => setEditMode(true)}
      handleDelete={handleDelete}
      createdAt={personData.createdAt}
      updatedAt={[personData.updatedAt]}
    >
      <div className="flex justify-center">
        <PersonImageWithUploadBox
          image={image}
          personData={personData}
          fetchPersonData={fetchPersonData}
        />
        <ContactDetailsBox personData={personData} />
      </div>
      <HighlightListItem value={personData.summary} />
    </HighlightTemplate>
  );
};

export default DetailedPersonHighlight;
