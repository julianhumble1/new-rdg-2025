import { useNavigate } from "react-router-dom";
import EditDeleteButtons from "../../../EditDeleteButtons.jsx";

// component wrapper so hook rules are respected
export const CreditsActionCell = ({ row }) => {
  const navigate = useNavigate();
  return (
    <EditDeleteButtons
      handleEdit={() => navigate(`/archive/credits/edit/${row.id}`)}
    />
  );
};
