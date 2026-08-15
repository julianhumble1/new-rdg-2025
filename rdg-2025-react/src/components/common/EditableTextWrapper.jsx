import Cookies from "js-cookie";
import { useState } from "react";
import { PencilIcon } from "@heroicons/react/16/solid";
import RoleHelper from "../../utils/RoleHelper.js";
import TextEditor from "./TextEditor/TextEditor.jsx";

const EditableTextWrapper = ({ children, contentKey }) => {
  const [isEditing, setIsEditing] = useState(false);
  const role = Cookies.get("role");
  const admin = RoleHelper.isAdmin(role);

  if (isEditing) {
    return (
      <TextEditor
        contentKey={contentKey}
        onSaved={() => setIsEditing(false)}
        onCancel={() => setIsEditing(false)}
      />
    );
  }

  return (
    <div className="flex flex-col gap-2">
      {children}
      {admin && (
        <button
          onClick={() => setIsEditing(true)}
          className="flex text-sm text-gray-600 hover:bg-gray-200 w-fit rounded-xl p-1 px-3"
        >
          <PencilIcon className="h-3 aspect-square mx-1 my-auto" />
          <div>Edit Text Content</div>
        </button>
      )}
    </div>
  );
};

export default EditableTextWrapper;
