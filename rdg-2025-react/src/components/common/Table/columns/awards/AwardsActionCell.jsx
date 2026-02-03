import { useNavigate } from "react-router-dom";
import EditDeleteButtons from "../../../EditDeleteButtons.jsx";

// component wrapper so hook rules are respected
export const AwardsActionCell = ({ row }) => {
  const navigate = useNavigate();
  return (
    <EditDeleteButtons
      handleEdit={() => navigate(`/archive/awards/edit/${row.id}`)}
    />
  );
};
