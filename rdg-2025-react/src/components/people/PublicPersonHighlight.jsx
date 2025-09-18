import HighlightTemplate from "../common/HighlightTemplate.jsx";
import HighlightListItem from "../common/HighlightListItem.jsx";

const PublicPersonHighlight = ({ personData, image }) => {
  if (!personData) return null;

  const fullName = personData.firstName + " " + personData.lastName;

  return (
    <HighlightTemplate
      title={fullName}
      type="People"
      handleEdit={() => {
        return;
      }}
      handleDelete={() => {
        return;
      }}
      createdAt={personData.createdAt}
      updatedAt={[personData.updatedAt]}
    >
      <HighlightListItem value={personData.summary} />
    </HighlightTemplate>
  );
};

export default PublicPersonHighlight;
