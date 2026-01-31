import { useState } from "react";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import Cookies from "js-cookie"

const EditDeleteButtons = ({ handleEdit, handleDelete, itemToDelete }) => {
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);

  const role = Cookies.get("role")

  if (role === "ROLE_ADMIN") {
    return (
      <>
      <div className="flex gap-2 font-bold">
        <button className="text-sm hover:underline" onClick={handleEdit}>
          Edit
        </button>
        <button className="text-sm hover:underline" onClick={handleDelete}>
          Delete
        </button>
      </div>
      {/* <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} handleConfirmDelete={handleDelete} /> */}
    </>
  );
}
};

export default EditDeleteButtons;
