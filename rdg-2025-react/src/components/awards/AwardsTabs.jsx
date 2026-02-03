import { TrophyIcon } from "@heroicons/react/16/solid";
import { Tabs } from "flowbite-react";
import AwardsTable from "./AwardsTable.jsx";

const AwardsTabs = ({ awards, handleDelete }) => {
  return (
    <Tabs variant="underline" className="m-3 mb-0">
      {awards.length > 0 && (
        <Tabs.Item active title="Awards" icon={TrophyIcon}>
          <div className="m-2 overflow-auto">
            <AwardsTable awards={awards} handleDelete={handleDelete} />
          </div>
        </Tabs.Item>
      )}
    </Tabs>
  );
};

export default AwardsTabs;
