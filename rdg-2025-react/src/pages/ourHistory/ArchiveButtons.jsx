import { useNavigate } from "react-router-dom";
import RedButton from "../../components/common/RedButton.jsx";

const ArchiveButtons = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col  gap-2">
      <RedButton onClick={() => navigate("productions")}>
        See Past Productions
      </RedButton>
      <RedButton onClick={() => navigate("people")} altColor="blue">
        See Our People
      </RedButton>
      <RedButton onClick={() => navigate("festivals")} altColor="yellow">
        See Past Festivals
      </RedButton>
    </div>
  );
};

export default ArchiveButtons;
