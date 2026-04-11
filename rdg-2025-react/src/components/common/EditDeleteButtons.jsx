import { useState } from "react";
import Cookies from "js-cookie";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";

const EditDeleteButtons = ({ handleEdit, handleDelete }) => {
    const [showConfirmDelete, setShowConfirmDelete] = useState(false);

    const role = Cookies.get("role");

    if (role === "ROLE_ADMIN" || role === "ROLE_SUPERADMIN") {
        return (
            <>
                <div className="flex gap-2 font-bold">
                    <button className="text-sm hover:underline" onClick={handleEdit}>
                        Edit
                    </button>
                    {handleDelete && (
                        <button className="text-sm hover:underline" onClick={() => setShowConfirmDelete(true)}>
                            Delete
                        </button>
                    )}
                </div>
                {showConfirmDelete && (
                    <ConfirmDeleteModal
                        onConfirm={async () => {
                            await handleDelete();
                            setShowConfirmDelete(false);
                        }}
                        onCancel={() => setShowConfirmDelete(false)}
                    />
                )}
            </>
        );
    }
};

export default EditDeleteButtons;
