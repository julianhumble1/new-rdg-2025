import { useNavigate } from "react-router-dom";

const TableLink = ({ text, link }) => {
  const fullLink = "/archive" + link;

  const navigate = useNavigate();
  return (
    <button
      onClick={() => navigate(fullLink)}
      className="font-bold hover:underline text-start"
    >
      {text}
    </button>
  );
};

export default TableLink;
