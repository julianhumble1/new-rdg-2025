import ContactDetailsBox from "./ContactDetailsBox.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import HighlightListItem from "../common/HighlightListItem.jsx";

const DetailedPersonHighlight = ({ personData, setEditMode, handleDelete }) => {
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
      <ContactDetailsBox personData={personData} />
      <HighlightListItem value={personData.summary} />
    </HighlightTemplate>
  );
};

export default DetailedPersonHighlight;