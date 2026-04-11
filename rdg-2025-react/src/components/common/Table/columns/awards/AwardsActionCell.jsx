import { useNavigate } from "react-router-dom";
import EditDeleteButtons from "../../../EditDeleteButtons.jsx";
import { useAwards } from "../../../../../hooks/useAwards.js";

// component wrapper so hook rules are respected
export const AwardsActionCell = ({ row, onAfterDelete }) => {
    const navigate = useNavigate();
    const { deleteAward } = useAwards();
    return (
        <EditDeleteButtons
            handleEdit={() => navigate(`/archive/awards/edit/${row.id}`)}
            handleDelete={async () => {
                await deleteAward.mutateAsync({ awardId: row.id });
                onAfterDelete?.();
            }}
        />
    );
};
