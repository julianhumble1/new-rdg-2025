import HighlightFooter from "../common/HighlightFooter.jsx";
import HighlightTitleBar from "../common/HighlightTitleBar.jsx";

const HighlightTemplate = ({
  children,
  title,
  type,
  handleEdit,
  handleDelete,
  createdAt,
  updatedAt,
}) => {
  return (
    <div className="w-full flex flex-col">
      <HighlightTitleBar
        title={title}
        handleDelete={handleDelete}
        handleEdit={handleEdit}
      />
      <div className="flex flex-col gap-2 flex-1 overflow-auto">{children}</div>
      <HighlightFooter
        name={type}
        createdAt={createdAt}
        updatedAt={updatedAt}
      />
    </div>
  );
};

export default HighlightTemplate;
