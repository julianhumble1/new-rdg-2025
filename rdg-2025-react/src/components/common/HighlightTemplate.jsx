import HighlightFooter from "./HighlightFooter.jsx";
import HighlightTitleBar from "./HighlightTitleBar.jsx";
import "./styles.css"

const HighlightTemplate = ({
  children,
  title,
  type,
  handleEdit,
  handleDelete,
  createdAt,
  updatedAt,
  sundowners=false
}) => {
  return (
    <div className="w-full flex flex-col relative overflow-visible">
      {sundowners &&
        <div className="ribbon">Sundowners</div>
      }
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
