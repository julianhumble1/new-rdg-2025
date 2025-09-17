const EditDeleteButtons = ({ handleEdit, handleDelete }) => {
  return (
    <div className="flex gap-2">
      <button className="text-sm hover:underline" onClick={handleEdit}>
        Edit
      </button>
      <button className="text-sm hover:underline" onClick={handleDelete}>
        Delete
      </button>
    </div>
  );
};

export default EditDeleteButtons;
