import { Link } from "react-router-dom";
import ContentCard from "./ContentCard.jsx";

const FormTemplate = ({
  children,
  customSubmit,
  newOrEdit = "new",
  cancelEdit,
  type,
}) => {
  return (
    <ContentCard>
      <form className="flex flex-col gap-2 max-w-md" onSubmit={customSubmit}>
        {children}
        <div className="grid grid-cols-2 justify-end px-2">
          {newOrEdit === "edit" ? (
            <div>
              <button
                className="text-sm hover:underline font-bold text-center col-span-1"
                onClick={cancelEdit}
                type="button"
              >
                Cancel Edit Mode
              </button>
              <button
                className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm"
                onClick={customSubmit}
              >
                Confirm Edit
              </button>
            </div>
          ) : (
            <div>
              <Link
                to={`/${type.toLowerCase()}`}
                className="text-sm hover:underline font-bold text-center col-span-1 my-auto"
              >
                Cancel
              </Link>
              <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
                Submit
              </button>
            </div>
          )}
        </div>
      </form>
    </ContentCard>
  );
};

export default FormTemplate;
