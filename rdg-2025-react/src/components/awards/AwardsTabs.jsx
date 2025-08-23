import { TrophyIcon } from "@heroicons/react/16/solid"
import { Tabs } from "flowbite-react"
import FestivalAwardsTable from "./FestivalAwardsTable.jsx"

const AwardsTabs = ({ awards, handleDelete }) => {
    return (
        <Tabs variant="underline" className="m-3 mb-0">
            {awards.length > 0 &&
                <Tabs.Item active title="Awards" icon={TrophyIcon}>
                    <div className="m-2 overflow-auto">
                        <FestivalAwardsTable awards={awards} handleDelete={handleDelete} />
                    </div>
                </Tabs.Item>
            }
        </Tabs>
    )
}

export default AwardsTabs
