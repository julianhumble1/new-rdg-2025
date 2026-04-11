import { useNavigate } from "react-router-dom";
import EditDeleteButtons from "../../../EditDeleteButtons.jsx";
import { useCredits } from "../../../../../hooks/useCredits.js";

// component wrapper so hook rules are respected
export const CreditsActionCell = ({ row, onAfterDelete }) => {
    const navigate = useNavigate();
    const { deleteCredit } = useCredits();
    return (
        <EditDeleteButtons
            handleEdit={() => navigate(`/archive/credits/edit/${row.id}`)}
            handleDelete={async () => {
                await deleteCredit.mutateAsync({ creditId: row.id });
                onAfterDelete?.();
            }}
        />
    );
};
