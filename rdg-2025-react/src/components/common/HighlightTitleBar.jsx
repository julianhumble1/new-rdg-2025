import EditDeleteButtons from "./EditDeleteButtons.jsx";

const HighlightTitleBar = ({ title, handleEdit, handleDelete }) => {
  return (
    <div className="text-black text-xl font-bold flex justify-between pb-2">
      <div>{title}</div>
      <EditDeleteButtons handleEdit={handleEdit} handleDelete={handleDelete} />
    </div>
  );
};

export default HighlightTitleBar;
